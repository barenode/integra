package com.integra.controller;

import com.integra.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ReportApi;
import org.openapitools.model.Report;
import org.openapitools.model.ReportInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController implements ReportApi {

    private final ReportService service;

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
}
