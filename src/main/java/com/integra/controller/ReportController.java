package com.integra.controller;

import com.integra.service.ReportService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ReportApi;
import org.openapitools.model.Report;
import org.openapitools.model.ReportInfo;
import org.openapitools.model.SpanDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.image.DataBuffer;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController implements ReportApi {

    private final ReportService service;

    @Override
    public Mono<ResponseEntity<Void>> upload(Flux<Part> file, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok().build());
    }

//    @RequestMapping(
//            method = RequestMethod.POST,
//            value = "/api/v1/upload",
//            produces = { "application/json" }
//    )
//    public Mono<Void> upload(
//            @RequestPart("file") Mono<FilePart> file
//    ) {
//        return Mono.empty();
//    }

    @Override
    public Mono<ResponseEntity<ReportInfo>> parseReport(
        ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity.ok().body(
            service.parse()));
    }

    @Override
    public Mono<ResponseEntity<Report>> readReport(
        String reportId,
        ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity.ok().body(
            service.read(reportId)));
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
