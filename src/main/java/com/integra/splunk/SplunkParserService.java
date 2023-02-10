package com.integra.splunk;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Report;
import org.openapitools.model.Span;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SplunkParserService {

    private final ObjectMapper mapper;


    public Report parse() {
        try {
            return Report.builder()
                .spans(
                    new ArrayList<>(Files.lines(Path.of("/tmp/session.json"))
                        .map(this::parseRecord)
                        .map(SplunkRecord::getResult)
                        .sorted(Comparator.comparing(SplunkRecord.Result::getTimestamp))
                        .filter(r -> r.getTraceId() != null)
                        .collect(Collectors.groupingBy(
                            SplunkRecord.Result::getTraceId,
                            LinkedHashMap::new,
                            new SplunkRecordCollector()
                        )).values()))
                 .build();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private SplunkRecord parseRecord(String raw) {
        try {
            return mapper.readValue(raw, SplunkRecord.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Slf4j
    private static final class SplunkRecordCollector implements Collector<SplunkRecord.Result, SplunkRecordAggregator, Span> {
        @Override
        public Supplier<SplunkRecordAggregator> supplier() {
            return SplunkRecordAggregator::new;
        }
        @Override
        public BiConsumer<SplunkRecordAggregator, SplunkRecord.Result> accumulator() {
            return SplunkRecordAggregator::add;
        }
        @Override
        public BinaryOperator<SplunkRecordAggregator> combiner() {
            return (a, b) -> a;
        }
        @Override
        public Function<SplunkRecordAggregator, Span> finisher() {
            return this::mapSpan;
        }
        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }

        private Span mapSpan(SplunkRecordAggregator aggregator) {
            return mapSpan(aggregator.root);
        }

        private Span mapSpan(Container container) {
            SplunkRecord.Result request = container.request;
            SplunkRecord.Result response = container.response;
            return Span.builder()
                .spanId(get(container, SplunkRecord.Result::getSpanId))
                .serviceName(get(container, SplunkRecord.Result::getServiceName))
                .traceId(get(container, SplunkRecord.Result::getTraceId))
                .childSpans(Optional.ofNullable(container.getChildren()).orElse(Collections.emptyList())
                    .stream()
                    .map(this::mapSpan)
                    .collect(Collectors.toList()))
                .build();
        }

        private <T> T get(Container container, Function<SplunkRecord.Result, T> resolver) {
            SplunkRecord.Result request = container.request;
            SplunkRecord.Result response = container.response;
            return Optional.ofNullable(resolver.apply(request))
                .orElse(Optional.ofNullable(response).map(resolver).orElse(null));
        }
    }

    private enum ContainerType {
        RequestReceived,
        ResponseSent,
        ClientRequest,
        ClientResponded,
        Other
    }

    @Getter
    @Slf4j
    private static final class SplunkRecordAggregator {

        private Container root;
        private Map<String, Container> requests = new HashMap<>();
        private List<Container> clientRequests = new ArrayList<>();

        public void add(SplunkRecord.Result span) {
            ContainerType type = resolveContainerType(span);
            log.info("add {} {} {} - {}", type, span.getIdent(), span.getMessage(), span.getTarget());
            switch (type) {
                case Other -> {
                    Container other = new OtherContainer(type, span);
                    if (!requests.containsKey(span.getIdent())) {
                        //throw new IllegalStateException("no parent for OTHER span: " + span);
                        if (root == null) {
                            root = other;
                        } else {
                            root.addChild(other);
                        }
                    } else {
                        Container parent = requests.get(span.getIdent());
                        parent.addChild(other);
                    }
                }
                case RequestReceived -> {
                    Container requestContainer = new RequestContainer(type, span);
                    List<Container> crs = clientRequests.stream()
                            .filter(c -> c.matchClientRequest(span)).collect(Collectors.toList());
                    if (crs.size() == 0) {
                        if (root == null) {
                            root = requestContainer;
                        } else {
                            log.warn("Checking client request {} ", span);
                            clientRequests.forEach(cr -> {
                                String[] parts = span.getEvent().split(" ");
                                log.warn("\tmessage {} , parts {}", cr.getRequest().getMessage(), parts);
                            });
                            //throw new IllegalStateException();
                            root.addChild(requestContainer);
                        }
                    } else if (crs.size() == 1) {
                        crs.get(0).addChild(requestContainer);
                    } else {
                        Container cr = crs.stream()
                                .filter(c -> c.getChildren() == null || c.getChildren().isEmpty())
                                .findFirst()
                                .orElseThrow(IllegalStateException::new);
                        cr.addChild(requestContainer);
                    }
                    if (requests.containsKey(span.getIdent())) {
                        throw new IllegalStateException(String.format("Duplicate request %s", span.getIdent()));
                    }
                    requests.put(span.getIdent(), requestContainer);
                }
                case ResponseSent -> {
                    if (!requests.containsKey(span.getIdent())) {
                        //throw new IllegalStateException("no parent for request response: " + span);
                        log.warn("no parent for request response: " + span);
                    } else {
                        Container request = requests.get(span.getIdent());
                        request.responseSent(span);
                    }
                }
                case ClientRequest -> {
                    Container clientRequest = new ClientRequestContainer(type, span);
                    if (requests.containsKey(span.getIdent())) {
                        Container requestContainer = requests.get(span.getIdent());
                        requestContainer.addChild(clientRequest);
                        clientRequests.add(clientRequest);
                    } else {
                        root.addChild(clientRequest);
                        clientRequests.add(clientRequest);
                    }
                }
                case ClientResponded -> {
                    List<Container> crs = clientRequests.stream()
                            .filter(c -> c.matchClientResponse(span)).collect(Collectors.toList());
                    if (crs.size() == 1) {
                        crs.get(0).clientResponded(span);
                    } else {
                        Container cr = crs.stream()
                                .filter(c -> c.response == null)
                                .findFirst()
                                .orElseThrow(IllegalStateException::new);
                        cr.clientResponded(span);
//                        throw new IllegalStateException(
//                                String.format("Multiple client request found for response! Requests {}, response {}  ", span, crs)
//                        );
                    }
                }
            }
        }

        private ContainerType resolveContainerType(SplunkRecord.Result request) {
            if (request.getMessage() == null) {
                return ContainerType.Other;
            }
            String sanitized = request.getMessage().trim().toLowerCase();
            if (sanitized.startsWith("request received")) {
                return ContainerType.RequestReceived;
            } else if (sanitized.startsWith("client request")) {
                return ContainerType.ClientRequest;
            } else if (sanitized.startsWith("response sent")) {
                return ContainerType.ResponseSent;
            } else if (sanitized.startsWith("client responded")) {
                return ContainerType.ClientResponded;
            } else {
                return ContainerType.Other;
            }
        }

        public void print() {
            root.print("");
        }
    }

    private interface ContainerState {

        void requestReceived(SplunkRecord.Result request);

        void responseSent(SplunkRecord.Result reponse);

        void clientRequested(SplunkRecord.Result request);

        void clientResponded(SplunkRecord.Result reponse);
    }

    @Slf4j
    @Getter
    @RequiredArgsConstructor
    private static class Container implements ContainerState {
        private final ContainerType type;
        //		private final Container parent;
        private final SplunkRecord.Result request;
        private SplunkRecord.Result response;

        private List<Container> children;

        public void addChild(Container container) {
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(container);
        }

        protected void setResponse(SplunkRecord.Result response) {
            this.response = response;
        }

        protected void tracingMatch(SplunkRecord.Result response) {
            if (
                    !request.getServiceName().equals(response.getServiceName()) ||
                            !request.getTraceId().equals(response.getTraceId()) ||
                            !request.getSpanId().equals(response.getSpanId()) ||
                            (request.getEvent()!=null && response.getEvent()!=null && !request.getEvent().equals(response.getEvent()))
            ) {
                log.warn(
                        String.format("Tracing do not match:\n\t%s\n\t%s", request, response));
                //throw new IllegalStateException("Tracing do not match!");
            }
        }

        private boolean matchClientRequest(SplunkRecord.Result request) {
            String[] parts = request.getEvent().split(" ");
            if (parts.length != 2) {
                return false;
            }
            return this.request.getMessage().indexOf(parts[0]) > -1 && this.request.getMessage().indexOf(parts[1]) > -1;
        }

        private boolean matchClientResponse(SplunkRecord.Result response) {
            return this.request.getIdent().equals(response.getIdent()) && this.request.getTarget().equals(response.getTarget());
        }

        @Override
        public void requestReceived(SplunkRecord.Result request) {
            //throw new UnsupportedOperationException("requestReceived is not supported");
        }

        @Override
        public void responseSent(SplunkRecord.Result response) {
            //throw new UnsupportedOperationException("responseSent is not supported");
        }

        @Override
        public void clientRequested(SplunkRecord.Result request) {
            //throw new UnsupportedOperationException("clientRequested is not supported");
        }

        @Override
        public void clientResponded(SplunkRecord.Result response) {
//			throw new UnsupportedOperationException(
//				String.format("clientResponded is not supported for %s ", this));
        }

        public void print(String indent) {
            System.out.println(String.format(
                    "%s %s %s:%s", indent, type, request, response));
            Optional.ofNullable(children).orElse(Collections.emptyList()).forEach(ch -> ch.print(indent + "\t"));
        }
    }

    private static final class OtherContainer extends Container {
        public OtherContainer(ContainerType type, SplunkRecord.Result request) {
            super(type, request);
        }

        @Override
        public void requestReceived(SplunkRecord.Result request) {}

        @Override
        public void clientRequested(SplunkRecord.Result request) {}
    }

    private static final class RequestContainer extends Container {
        public RequestContainer(ContainerType type, SplunkRecord.Result request) {
            super(type, request);
        }

        @Override
        public void clientRequested(SplunkRecord.Result request) {}

        @Override
        public void responseSent(SplunkRecord.Result response) {
            tracingMatch(response);
            setResponse(response);
        }
    }

    private static final class ClientRequestContainer extends Container {
        public ClientRequestContainer(ContainerType type, SplunkRecord.Result request) {
            super(type, request);
        }

        @Override
        public void requestReceived(SplunkRecord.Result request) {}

        @Override
        public void clientRequested(SplunkRecord.Result request) {}

        @Override
        public void clientResponded(SplunkRecord.Result response) {
            tracingMatch(response);
            setResponse(response);
        }
    }
}
