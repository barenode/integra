package com.example.demo;

import com.example.demo.domain.SplunkRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Set;
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
			//.map(r -> String.format("%s %s %s %s %s %s /t/t %s", r.getTimestamp(), r.getServiceName(), r.getTraceId(), r.getSpanId(), r.getHttpMethod(), r.getHttpStatus(), r.getMessage()))
			.collect(
				Collectors.groupingBy(
					SplunkRecord.Result::getTraceId,
					LinkedHashMap::new,
					new SplunkRecordCollector()
				))
				;
//			.forEach((k, v) -> {
//				System.out.println("");
//				System.out.println("====================================================");
//				System.out.println(k);
//				v.forEach(r ->
//					System.out.println(
//						String.format("%s %s %s %s %s %s /t/t %s", r.getTimestamp(), r.getServiceName(), r.getTraceId(), r.getSpanId(), r.getHttpMethod(), r.getHttpStatus(), r.getMessage()))
//				);
//			});
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

	private static final class SplunkRecordAggregator {

		public void add(SplunkRecord.Result span) {

		}
	}
}
