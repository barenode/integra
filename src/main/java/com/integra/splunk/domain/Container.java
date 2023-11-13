package com.integra.splunk.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@RequiredArgsConstructor
public class Container {
    private final String id;
    private final ContainerType type;
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
        tracingMatch(response);
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
                String.format("Tracing do not match:%n\t%s%n\t%s", request, response));
        }
    }

    public boolean matchClientRequest(SplunkRecord.Result request) {
        String[] parts = request.getEvent().split(" ");
        if (parts.length != 2) {
            return false;
        }
        return this.request.getMessage().indexOf(parts[0]) > -1 && this.request.getMessage().indexOf(parts[1]) > -1;
    }

    public boolean matchClientResponse(SplunkRecord.Result response) {
        return this.request.getIdent().equals(response.getIdent()) && this.request.getTarget().equals(response.getTarget());
    }

    public void print(String indent) {
        log.info(String.format(
            "%s %s %s:%s", indent, type, request, response));
        Optional.ofNullable(children).orElse(Collections.emptyList()).forEach(ch -> ch.print(indent + "\t"));
    }
}
