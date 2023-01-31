package com.example.demo;

import com.example.demo.domain.SplunkRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private ObjectMapper mapper;

	@Test
	void contextLoads() throws Exception {
		Files.lines(Path.of("/tmp/session.json"))
			.map(this::parseRecord)
			.map(SplunkRecord::getResult)
			.sorted(Comparator.comparing(SplunkRecord.Result::getTimestamp))
			.filter(r -> r.getTraceId() != null)
			//.filter(r -> r.getTraceId().equals("9587700cf81bdbf0"))
			//.map(r -> String.format("%s %s %s %s %s %s /t/t %s", r.getTimestamp(), r.getServiceName(), r.getTraceId(), r.getSpanId(), r.getHttpMethod(), r.getHttpStatus(), r.getMessage()))
			.collect(
				Collectors.groupingBy(
					SplunkRecord.Result::getTraceId,
					LinkedHashMap::new,
					new SplunkRecordCollector()
				))
				.forEach((k, v) -> {
					System.out.println("");
					System.out.println("====================================================");
					v.print();
//					v.getSpans().forEach(r ->
//						System.out.println(
//							String.format("%s %s %s %s %s %s /t/t %s", r.getTimestamp(), r.getServiceName(), r.getTraceId(), r.getSpanId(), r.getHttpMethod(), r.getHttpStatus(), r.getMessage()))
//					);
			});
	}

	private SplunkRecord parseRecord(String raw) {
		try {
			return mapper.readValue(raw, SplunkRecord.class);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Slf4j
	private static final class SplunkRecordCollector implements Collector<SplunkRecord.Result, SplunkRecordAggregator, SplunkRecordAggregator> {
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
		public Function<SplunkRecordAggregator, SplunkRecordAggregator> finisher() {
			return Function.identity();
		}
		@Override
		public Set<Characteristics> characteristics() {
			return Collections.emptySet();
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
//			log.info("\t {} add {} - {}", current != null ? (current.type + ":" + current.request.getServiceName() + ":" + current.request.getSpanId()) : "null", (type + ":" + span.getServiceName() + ":" + span.getSpanId()), span.getMessage());
			switch (type) {
				case Other -> {
					if (!requests.containsKey(span.getIdent())) {
						throw new IllegalStateException("no parent for OTHER span: " + span);
					}
					Container parent = requests.get(span.getIdent());
					Container other = new OtherContainer(type, span);
					parent.addChild(other);
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
							throw new IllegalStateException();
						}
					} else if (crs.size() == 1) {
						crs.get(0).addChild(requestContainer);
					} else {
						throw new IllegalStateException();
					}
					if (requests.containsKey(span.getIdent())) {
						throw new IllegalStateException(String.format("Duplicate request %s", span.getIdent()));
					}
					requests.put(span.getIdent(), requestContainer);
				}
				case ResponseSent -> {
					if (!requests.containsKey(span.getIdent())) {
						throw new IllegalStateException("no parent for request response: " + span);
					}
					Container request = requests.get(span.getIdent());
					request.responseSent(span);
				}
				case ClientRequest -> {
					Container clientRequest = new ClientRequestContainer(type, span);
					Container requestContainer = requests.get(span.getIdent());
					requestContainer.addChild(clientRequest);
					clientRequests.add(clientRequest);
				}
				case ClientResponded -> {
					List<Container> crs = clientRequests.stream()
						.filter(c -> c.matchClientResponse(span)).collect(Collectors.toList());
					if (crs.size() == 1) {
						crs.get(0).clientResponded(span);
					} else {
						throw new IllegalStateException(
							String.format("Multiple client request found for response! Requests {}, response {}  ", span, crs)
						);
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
				!request.getEvent().equals(response.getEvent())
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
