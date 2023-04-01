package com.integra.splunk.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integra.splunk.domain.Container;
import com.integra.splunk.domain.SplunkRecord;
import com.integra.splunk.domain.SplunkReport;
import lombok.AllArgsConstructor;
import org.openapitools.model.Report;
import org.openapitools.model.Severity;
import org.openapitools.model.Span;
import org.openapitools.model.SpanDetail;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SplunkMapper {

    private final ObjectMapper objectMapper;

    public Report mapReport(String id, SplunkReport input) {
        return Report.builder()
            .id(id)
            .spans(input.getRoots().stream().map(this::mapSpan).collect(Collectors.toList()))
            .build();
    }

    public Report mapReport(String id, SplunkReport input, Integer startIndex, Integer endIndex) {
        if (input.getRoots().isEmpty()) {
            return mapReport(id, input);
        }
        return Report.builder()
            .id(id)
            .spans(input.getRoots()
                .subList(Math.min(startIndex, input.getRoots().size() - 1), Math.min(endIndex, input.getRoots().size()))
                .stream().map(this::mapSpan).collect(Collectors.toList()))
            .build();
    }

    public SpanDetail mapSpanDetail(Container container) {
        return SpanDetail.builder()
            .id(container.getId())
            .spanId(get(container, SplunkRecord.Result::getSpanId))
            .serviceName(get(container, SplunkRecord.Result::getServiceName))
            .traceId(get(container, SplunkRecord.Result::getTraceId))
            .childSpans(Optional.ofNullable(container.getChildren()).orElse(Collections.emptyList())
                .stream()
                .map(this::mapSpan)
                .collect(Collectors.toList()))
            .request(getRequestPayload(container))
            .response(getResponsePayload(container))
            .build();
    }

    private Span mapSpan(Container container) {
        String httpStatus = get(container, SplunkRecord.Result::getHttpStatus);
        Severity severity = httpStatus != null && !httpStatus.startsWith("2") ? Severity.ERROR : Severity.INFO;

        return Span.builder()
            .id(container.getId())
            .spanId(get(container, SplunkRecord.Result::getSpanId))
            .serviceName(get(container, SplunkRecord.Result::getServiceName))
            .traceId(get(container, SplunkRecord.Result::getTraceId))
            .label(get(container, SplunkRecord.Result::getMessage))
            .timestamp(get(container, SplunkRecord.Result::getTimestamp))
            .severity(severity)
            .childSpans(Optional.ofNullable(container.getChildren()).orElse(Collections.emptyList())
                .stream()
                .map(this::mapSpan)
                .collect(Collectors.toList()))
            .build();
    }

    private <T> T get(Container container, Function<SplunkRecord.Result, T> resolver) {
        SplunkRecord.Result request = container.getRequest();
        SplunkRecord.Result response = container.getResponse();
        return Optional.ofNullable(resolver.apply(request))
            .orElse(Optional.ofNullable(response).map(resolver).orElse(null));
    }

    private String getRequestPayload(Container container) {
        if (container.getRequest() == null || container.getRequest().getRaw() == null) {
            return null;
        }
        try {
            SplunkRecord.RawContent raw = objectMapper.readValue(container.getRequest().getRaw(), SplunkRecord.RawContent.class);
            if (raw.getPayloadJson() != null) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(raw.getPayloadJson()).replace("\\n", "\n");
            } else {
                return raw.getPayload();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String getResponsePayload(Container container) {
        if (container.getResponse() == null || container.getResponse().getRaw() == null) {
            return null;
        }
        try {
            SplunkRecord.RawContent raw = objectMapper.readValue(container.getResponse().getRaw(), SplunkRecord.RawContent.class);
            if (raw.getPayloadJson() != null) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(raw.getPayloadJson()).replace("\\n", "\n");
            } else {
                return raw.getPayload();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
