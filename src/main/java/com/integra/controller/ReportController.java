package com.integra.controller;

import com.integra.splunk.SplunkParserService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ReportApi;
import org.openapitools.model.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController implements ReportApi {

    private final SplunkParserService service;

    @Override
    public Mono<ResponseEntity<Report>> readReport(
        String reportId,
        ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity.ok().body(
            service.parse()));
    }
}
