package com.integra.service;

import com.integra.config.IntegraConfig;
import com.integra.domain.ReportData;
import com.integra.splunk.SplunkParserService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Report;
import org.openapitools.model.ReportInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportService {

    private final SplunkParserService splunkParserService;
    private final CacheManager cacheManager;

    public ReportInfo parse() {
        ReportData reportData = splunkParserService.parse();
        Cache cache = cacheManager.getCache(IntegraConfig.REPORT_CACHE);
        cache.put(reportData.getReport().getId(), reportData);
        return ReportInfo.builder()
            .id(reportData.getReport().getId())
            .build();
    }

    public Report read(String reportId) {
        Cache cache = cacheManager.getCache(IntegraConfig.REPORT_CACHE);
        ReportData reportData = cache.get(reportId, ReportData.class);
        if (reportData == null) {
            throw new IllegalStateException(String.format("Report with id %s not found!", reportId));
        }
        return reportData.getReport();
    }
}
