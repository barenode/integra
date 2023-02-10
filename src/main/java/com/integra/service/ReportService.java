package com.integra.service;

import com.integra.config.IntegraConfig;
import com.integra.domain.ReportData;
import com.integra.splunk.SplunkParserService;
import lombok.RequiredArgsConstructor;
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
        ReportData reportData = null;
        Cache cache = cacheManager.getCache(IntegraConfig.REPORT_CACHE);
        return null;
    }
}
