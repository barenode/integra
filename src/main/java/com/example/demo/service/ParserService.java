package com.example.demo.service;

import com.example.demo.domain.SplunkRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ParserService {

    private final ObjectMapper mapper;

    public Flux<SplunkRecord> parse() {
        try {
            return Flux.fromStream(Files.lines(Path.of("/tmp/session.json"))
                .map(this::parseRecord));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private SplunkRecord parseRecord(String raw) {
        try {
            return mapper.readValue(raw, SplunkRecord.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
