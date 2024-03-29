package com.integra.splunk.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Slf4j
public class SplunkRecordAggregator {
    private Map<String, Container> roots = new LinkedHashMap<>();
    private Map<String, Container> requests = new HashMap<>();
    private List<Container> clientRequests = new ArrayList<>();

    private Map<String, Container> containerMap = new HashMap<>();

    private Container newContainer(ContainerType type, SplunkRecord.Result span) {
        String id = UUID.randomUUID().toString();
        Container container = new Container(id, type, span);
        containerMap.put(id, container);
        return container;
    }

    public void add(SplunkRecord.Result span) {
        ContainerType type = span.resolveContainerType();
        switch (type) {
            case Other -> {
                Container other = newContainer(type, span);
                if (!requests.containsKey(span.getIdent())) {
                    if (!roots.containsKey(span.getSpanId())) {
                        roots.put(span.getSpanId(), other);
                    } else {
                        roots.get(span.getSpanId()).addChild(other);
                    }
                } else {
                    Container parent = requests.get(span.getIdent());
                    parent.addChild(other);
                }
            }
            case RequestReceived -> {
                Container requestContainer = newContainer(type, span);
                List<Container> crs = clientRequests.stream()
                        .filter(c -> c.matchClientRequest(span)).collect(Collectors.toList());
                if (crs.isEmpty()) {
                    if (!roots.containsKey(span.getSpanId())) {
                        roots.put(span.getSpanId(), requestContainer);
                    } else {
                        roots.get(span.getSpanId()).addChild(requestContainer);
                    }
                } else if (crs.size() == 1) {
                    crs.get(0).addChild(requestContainer);
                } else {
                    Container cr = crs.stream()
                        .filter(c -> c.getChildren() == null || c.getChildren().isEmpty())
                        .findFirst()
                        .orElse(null);   
                    if (cr != null) {
                        cr.addChild(requestContainer);
                    } else {
                        log.warn("DUPLICATE SPAN {} ", requestContainer);
                        roots.put(span.getSpanId(), requestContainer);
                    }                                             
                }
                requests.put(span.getIdent(), requestContainer);
            }
            case ResponseSent -> {
                if (!requests.containsKey(span.getIdent())) {
                    log.warn("no parent for request response: " + span);
                } else {
                    Container request = requests.get(span.getIdent());
                    request.setResponse(span);
                }
            }
            case ClientRequest -> {
                Container clientRequest = newContainer(type, span);
                if (requests.containsKey(span.getIdent())) {
                    Container requestContainer = requests.get(span.getIdent());
                    requestContainer.addChild(clientRequest);
                    clientRequests.add(clientRequest);
                } else {
                    if (!roots.containsKey(span.getSpanId())) {
                        roots.put(span.getSpanId(), clientRequest);
                    } else {
                        roots.get(span.getSpanId()).addChild(clientRequest);
                    }
                    clientRequests.add(clientRequest);
                }
            }
            case ClientResponded -> {
                List<Container> crs = clientRequests.stream()
                    .filter(c -> c.matchClientResponse(span)).collect(Collectors.toList());
                if (crs.size() == 1) {
                    crs.get(0).setResponse(span);
                } else {
                    Container cr = crs.stream()
                            .filter(c -> c.getResponse() == null)
                            .findFirst()
                            .orElseThrow(IllegalStateException::new);
                    cr.setResponse(span);
                }
            }
        }
    }

    public void print() {
        roots.values().forEach(r -> r.print(""));
    }
}
