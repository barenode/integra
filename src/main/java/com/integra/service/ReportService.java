package com.integra.service;

import com.integra.splunk.SplunkService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Report;
import org.openapitools.model.ReportInfo;
import org.openapitools.model.SpanDetail;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportService {

    private final SplunkService splunkParserService;

    public ReportInfo parse(byte[] input) {
        return splunkParserService.parse(input);
    }

    public Report read(String reportId) {
        return splunkParserService.read(reportId);
    }

    public Report readRange(String reportId, Integer startIndex, Integer endIndex) {
        return splunkParserService.readRange(reportId, startIndex, endIndex);
    }

    public SpanDetail readSpan(String reportId, String spanId) {
        return splunkParserService.readSpan(reportId, spanId);
    }
}
