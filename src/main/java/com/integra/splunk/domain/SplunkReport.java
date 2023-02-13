package com.integra.splunk.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SplunkReport {
    List<Container> roots = new ArrayList<>();
    Map<String, Container> containerMap = new HashMap<>();

    public SplunkReport merge(SplunkReport other) {
        roots.addAll(other.roots);
        containerMap.putAll(other.containerMap);
        return this;
    }
}
