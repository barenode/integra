package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * {
 *    "preview":false,
 *    "result":{
 *       "Accept-Language":"en",
 *       "_raw":"{\"message\":\"Client request GET http://product-inventory-fs:8080/tmf-api/productInventory/v4/product?relatedParty%5B%3F%28%40.role%3D%3D%27subscriber%27%29%5D.id=420770089102&productCharacteristic%5B%3F%28%40.name%3D%3D%27assetSource%27%29%5D%21=Mte HTTP/1.1\",\"logger\":\"cz.vodafone.dxl.feign.logging.VodafoneFeignLogger\",\"process-thread\":\"default-pool-3\",\"severity\":\"INFO\",\"level_value\":20000,\"traceId\":\"9587700cf81bdbf0\",\"spanId\":\"9697151ca3964210\",\"vf-trace-transaction-id\":\"61d5fdce-29b4-47a8-8cac-4954dd56acd0\",\"user-session-id\":\"27eed615-d086-49dc-946b-43689538be34\",\"user-id\":\"yurzikumlu@gufum.com\",\"Accept-Language\":\"en\",\"event\":\"POST /tmf-api/productOfferingQualification/v4/productOfferingQualification\",\"type\":\"client-reqresp\",\"target\":\"ProductInventoryDxlAuthApi#getProductsByServiceNumber(String,String,List)\",\"http-method\":\"GET\",\"hostname\":\"product-offering-qualification-fs-c7668656f-ffjn4\",\"environment\":\"PRE\",\"service-name\":\"product-offering-qualification-fs\",\"timestamp\":\"2023-01-30T07:53:26.656Z\",\"version\":\"1\"}",
 *       "_time":"2023-01-30T08:53:26.656+0100",
 *       "cribl_pipe":"CR-235202_VF-CZ_DXL_Microservices_PoC",
 *       "environment":"PRE",
 *       "event":"POST /tmf-api/productOfferingQualification/v4/productOfferingQualification",
 *       "host":"vgcl58dr",
 *       "hostname":"product-offering-qualification-fs-c7668656f-ffjn4",
 *       "http-method":"GET",
 *       "index":"kafka_dxl_vfcz_pp",
 *       "level_value":"20000",
 *       "linecount":"1",
 *       "logger":"cz.vodafone.dxl.feign.logging.VodafoneFeignLogger",
 *       "message":"Client request GET http://product-inventory-fs:8080/tmf-api/productInventory/v4/product?relatedParty%5B%3F%28%40.role%3D%3D%27subscriber%27%29%5D.id=420770089102&productCharacteristic%5B%3F%28%40.name%3D%3D%27assetSource%27%29%5D%21=Mte HTTP/1.1",
 *       "process-thread":"default-pool-3",
 *       "service-name":"product-offering-qualification-fs",
 *       "severity":"INFO",
 *       "source":"caas-log-nonprod-eks",
 *       "sourcetype":"kafka:topicEvent",
 *       "spanId":"9697151ca3964210",
 *       "splunk_server":"vgcl53dr",
 *       "splunk_server_group":"default_indexer",
 *       "target":"ProductInventoryDxlAuthApi#getProductsByServiceNumber(String,String,List)",
 *       "timestamp":"2023-01-30T07:53:26.656Z",
 *       "traceId":"9587700cf81bdbf0",
 *       "type":"client-reqresp",
 *       "user-id":"yurzikumlu@gufum.com",
 *       "user-session-id":"27eed615-d086-49dc-946b-43689538be34",
 *       "version":"1",
 *       "vf-trace-transaction-id":"61d5fdce-29b4-47a8-8cac-4954dd56acd0"
 *    }
 * }
 */
@Data
@ToString
public class SplunkRecord {
    private SplunkRecord.Result result;

    @Data
    public static class Result {
        private OffsetDateTime timestamp;
        private String traceId;
        private String spanId;
        @JsonProperty("service-name")
        private String serviceName;
        @JsonProperty("http-method")
        private String httpMethod;
        @JsonProperty("http-status")
        private String httpStatus;
        private String event;
        private String message;
        private String target;
        @JsonProperty("_raw")
        private String raw;

        public String getBaseIdent() {
            return String.format("%s:%s:%s", serviceName, traceId, spanId);
        }

        public String getIdent() {
            return String.format("%s:%s:%s:%s", serviceName, traceId, spanId, event);
        }

        @JsonProperty("payload-json")
        public void setPayloadJson(Map data) {
            System.out.println("!!!!!!!!!!!" + data);
        }

        @Override
        public String toString() {
            return String.format("%s:%s:%s:%s:%s:%s", timestamp, serviceName, traceId, spanId, event, message);
//            return "Result{" +
//                    "timestamp=" + timestamp +
//                    ", traceId='" + traceId + '\'' +
//                    ", spanId='" + spanId + '\'' +
//                    ", serviceName='" + serviceName + '\'' +
//                    ", httpMethod='" + httpMethod + '\'' +
//                    ", httpStatus='" + httpStatus + '\'' +
//                    ", event='" + event + '\'' +
//                    ", message='" + message + '\'' +
//                    '}';
        }

        public boolean isRaw() {
            return false;//spanId == null && raw != null;
        }
    }


}
