package com.integra.domain;

import lombok.Data;
import org.openapitools.model.Report;
import org.openapitools.model.Span;

import java.util.Map;

@Data
public class ReportData {
    Report report;
    Map<String, SpanData> spanMap;

    @Data
    public static class SpanData {
        Span span;
        String request;
        String response;
    }
}
