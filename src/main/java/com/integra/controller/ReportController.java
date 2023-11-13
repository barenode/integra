package com.integra.controller;

import com.integra.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.ReportApi;
import org.openapitools.model.Report;
import org.openapitools.model.ReportInfo;
import org.openapitools.model.SpanDetail;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RegisterReflectionForBinding(classes = {
    ReportInfo.class,
    Report.class,
    SpanDetail.class })
public class ReportController implements ReportApi {
    final ReportService service;

    @Override
    public Mono<ResponseEntity<ReportInfo>> parseReport(Flux<Part> file, ServerWebExchange exchange) {
        return file
            .flatMap(MULTIPART_TO_BYTES)
            .reduce(new ByteArrayOutputStream(), REDUCER)
            .map(ByteArrayOutputStream::toByteArray)
            .map(service::parse)
            .map(this::toResponseEntity);
    }

    private <T> ResponseEntity<T> toResponseEntity(T entity) {
        return ResponseEntity.ok().body(entity);
    }

    private static final Function<Part, Flux<byte[]>> MULTIPART_TO_BYTES = part -> part.content().map(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            return bytes;
        }
    );

    private static final BiFunction<ByteArrayOutputStream, byte[], ByteArrayOutputStream> REDUCER = (a, b) -> {
        try { a.write(b); } catch (Exception e) { log.error(e.getMessage(), e); }
        return a;
    };

    @Override
    public Mono<ResponseEntity<Report>> readReport(
        String reportId,
        ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity.ok().body(
            service.read(reportId)));
    }

    @Override
    public Mono<ResponseEntity<Report>> readReportRange(
        String reportId,
        String startIndex, 
        String endIndex,
        ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity.ok().body(
            service.readRange(reportId, Integer.parseInt(startIndex), Integer.parseInt(endIndex))));
    }
    

    @Override
    public Mono<ResponseEntity<SpanDetail>> readSpan(
        String reportId,
        String spanId,
        ServerWebExchange exchange)
    {
        return Mono.just(ResponseEntity.ok().body(
            service.readSpan(reportId, spanId)));
    }
}
