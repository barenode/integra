package com.example.demo.controller;

import org.openapitools.api.BookApi;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openapitools.model.Book;
import org.openapitools.model.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;


@Validated
@Tag(name = "Book", description = "Book controller")
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController implements BookApi {

    @Override
    public Mono<ResponseEntity<SuccessResponse>> _getAllBook(
            @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity
                .ok()
                .body(new SuccessResponse(Collections.emptyList(), "result found")));
    }

    @Override
    public Mono<ResponseEntity<SuccessResponse>> _getOneBook(
            @Parameter(name = "id", description = "", required = true, schema = @Schema(description = "")) @PathVariable("id") String id,
            @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity
                .ok()
                .body(new SuccessResponse(Book.builder().id(id).build(), "result found")));
    }
}
