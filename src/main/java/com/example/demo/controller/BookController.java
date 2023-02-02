package com.example.demo.controller;

import com.example.demo.service.SplunkParserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.DefaultApi;
import org.openapitools.model.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BookController implements DefaultApi {

    private final SplunkParserService service;

    @Override
    public Mono<ResponseEntity<SuccessResponse>> _find(
        @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity.ok().body(
            SuccessResponse.builder()
                .report(service.parse())
            .build()));
    }
}
