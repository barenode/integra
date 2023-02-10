package com.integra.splunk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RawContent {
    @JsonProperty("payload-json")
    Object payloadJson;
}
