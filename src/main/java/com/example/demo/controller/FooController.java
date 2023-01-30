package com.example.demo.controller;

import com.example.demo.domain.SplunkRecord;
import com.example.demo.service.ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.BaseStream;

@RestController
@RequestMapping("/foo")
@RequiredArgsConstructor
public class FooController {

    private final ParserService parserService;

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/file"
    )
    public Flux<SplunkRecord> readFile() {
        return parserService.parse();
    }
}
