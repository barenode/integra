package com.integra.splunk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integra.config.IntegraConfig;
import com.integra.splunk.domain.SplunkRecord;
import com.integra.splunk.domain.SplunkRecordAggregator;
import com.integra.splunk.domain.SplunkReport;
import com.integra.splunk.mapper.SplunkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Report;
import org.openapitools.model.ReportInfo;
import org.openapitools.model.SpanDetail;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
@RegisterReflectionForBinding(classes = {
    SplunkRecord.class,
    SplunkRecord.RawContent.class,
    SplunkRecord.Result.class })
public class SplunkService {

    private final ObjectMapper mapper;
    private final CacheManager cacheManager;
    private final SplunkMapper splunkMapper;

    public ReportInfo parse(byte[] input) {        
        log.info("Parsing report ...");
        try (     
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input)));
            Stream<String> lines = reader.lines();
        ) {            
            String id = UUID.randomUUID().toString();
            SplunkReport report = lines 
                .map(this::parseRecord)
                .map(SplunkRecord::getResult)
                .filter(r -> r.getTimestamp() != null)
                .sorted(Comparator.comparing(SplunkRecord.Result::getTimestamp))
                .filter(r -> r.getTraceId() != null)
                .collect(Collectors.groupingBy(
                    SplunkRecord.Result::getTraceId,
                    LinkedHashMap::new,
                    new SplunkRecordCollector()
                )).values().stream()
                .reduce(new SplunkReport(), SplunkReport::merge);
            getCache().put(id, report);
            ReportInfo result = ReportInfo.builder()
                .id(id)
                .rootSpanCount(report.getRoots().size())
                .build();
            log.info("Result {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error processing content: " + e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    public Report read(String id) {
        SplunkReport report = getCache().get(id, SplunkReport.class);
        return splunkMapper.mapReport(id, report);
    }

    public Report readRange(String id, Integer startIndex, Integer endIndex) {
        SplunkReport report = getCache().get(id, SplunkReport.class);        
        return splunkMapper.mapReport(id, report, startIndex, endIndex);
    }

    public SpanDetail readSpan(String id, String spanId) {
        SplunkReport report = getCache().get(id, SplunkReport.class);
        return splunkMapper.mapSpanDetail(report.getContainerMap().get(spanId));
    }

    private Cache getCache() {
        Cache cache = cacheManager.getCache(IntegraConfig.REPORT_CACHE);
        if (cache == null) {
            throw new IllegalStateException("No cache found");
        }
        return cache;
    }

    private SplunkRecord parseRecord(String raw) {
        try {
            return mapper.readValue(raw, SplunkRecord.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

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
