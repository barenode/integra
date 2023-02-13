package com.integra.splunk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integra.config.IntegraConfig;
import com.integra.splunk.domain.Container;
import com.integra.splunk.domain.SplunkRecord;
import com.integra.splunk.domain.SplunkRecordAggregator;
import com.integra.splunk.domain.SplunkReport;
import com.integra.splunk.mapper.SplunkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Report;
import org.openapitools.model.ReportInfo;
import org.openapitools.model.SpanDetail;
import org.springframework.cache.Cache;
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
public class SplunkService {

    private final ObjectMapper mapper;
    private final CacheManager cacheManager;
    private final SplunkMapper splunkMapper;

    public ReportInfo parse() {
        try {
            String id = UUID.randomUUID().toString();
            SplunkReport report = Files.lines(Path.of("/tmp/1676122736_230398_A12409EF-F969-44D4-8F0D-C6D2B7B7091F.json"))
                .map(this::parseRecord)
                .map(SplunkRecord::getResult)
                .sorted(Comparator.comparing(SplunkRecord.Result::getTimestamp))
                .filter(r -> r.getTraceId() != null)
                .collect(Collectors.groupingBy(
                    SplunkRecord.Result::getTraceId,
                    LinkedHashMap::new,
                    new SplunkRecordCollector()
                )).values().stream()
                .reduce(new SplunkReport(), SplunkReport::merge);
            Cache cache = cacheManager.getCache(IntegraConfig.REPORT_CACHE);
            cache.put(id, report);
            return ReportInfo.builder()
                .id(id)
                .build();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Report read(String id) {
        Cache cache = cacheManager.getCache(IntegraConfig.REPORT_CACHE);
        SplunkReport report = cache.get(id, SplunkReport.class);
        return splunkMapper.mapReport(id, report);
    }

    public SpanDetail readSpan(String id, String spanId) {
        Cache cache = cacheManager.getCache(IntegraConfig.REPORT_CACHE);
        SplunkReport report = cache.get(id, SplunkReport.class);
        return splunkMapper.mapSpanDetail(report.getContainerMap().get(spanId));
    }

    private SplunkRecord parseRecord(String raw) {
        try {
            return mapper.readValue(raw, SplunkRecord.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Slf4j
    private static final class SplunkRecordCollector implements Collector<SplunkRecord.Result, SplunkRecordAggregator, SplunkReport> {
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
        public Function<SplunkRecordAggregator, SplunkReport> finisher() {
            return this::mapSpans;
        }
        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }

        private SplunkReport mapSpans(SplunkRecordAggregator aggregator) {
            SplunkReport report = new SplunkReport();
            report.getRoots().addAll(aggregator.getRoots().values());
            report.getContainerMap().putAll(aggregator.getContainerMap());
            return report;
        }
    }
}
