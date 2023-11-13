package com.integra;

import com.integra.splunk.domain.SplunkRecord;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class DemoApplicationTests {

	@Autowired
	private ObjectMapper mapper;

	@Data
	public static class RawContent {
		@JsonProperty("payload-json")
		Object payloadJson;
	}


	@Test
	void testParse() throws Exception {
		String data = "{\"preview\":false,\"result\":{\"Accept-Language\":\"en\",\"_raw\":\"{\\\"message\\\":\\\"Response sent\\\",\\\"logger\\\":\\\"cz.vodafone.dxl.web.logging.MvcLoggingFilter\\\",\\\"process-thread\\\":\\\"http-nio-8080-exec-4\\\",\\\"severity\\\":\\\"INFO\\\",\\\"level_value\\\":20000,\\\"traceId\\\":\\\"9587700cf81bdbf0\\\",\\\"spanId\\\":\\\"9f682f1613922c3e\\\",\\\"vf-trace-transaction-id\\\":\\\"61d5fdce-29b4-47a8-8cac-4954dd56acd0\\\",\\\"user-session-id\\\":\\\"27eed615-d086-49dc-946b-43689538be34\\\",\\\"user-id\\\":\\\"yurzikumlu@gufum.com\\\",\\\"Accept-Language\\\":\\\"en\\\",\\\"event\\\":\\\"POST /graphql\\\",\\\"type\\\":\\\"srv-reqresp\\\",\\\"http-status\\\":{\\\"xxx\\\": \\\"yyy\\\"},\\\"payload-json\\\":{\\\"data\\\":{\\\"productOfferingQualificationItems\\\":{\\\"productOfferingQualificationItem\\\":[{\\\"productOfferingQualificationItem\\\":[{\\\"productOffering\\\":{\\\"id\\\":\\\"PRFB0186\\\",\\\"name\\\":\\\"Unlimited Basic One\\\",\\\"prodSpecCharValueUse\\\":[{\\\"id\\\":\\\"DT_SIEBEL_PARAMS_PRICE.timestamp\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"2022-07-26T14:29:06.775682+02:00\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ecProductName\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Digital NeomezenÃ½ Basic\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.PriceType\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Recurring\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ServiceLength\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"24\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ecSection\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"FB\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.Timestamp\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"2022-07-26T12:41:13.585Z\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ProductType\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Subscriber Bundle\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ProductTypeCode\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Promotion\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ServiceLengthUoM\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Month\\\"}]},{\\\"id\\\":\\\"DT_SPEEDCAP.downloadSpeedDescription\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Speed up to 2Mb/s\\\"}]},{\\\"id\\\":\\\"DT_FC_OFFERING_PARAMS.Timestamp\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"2022-07-26T09:59:12.220039+02:00\\\"}]},{\\\"id\\\":\\\"DT_PRODUCT_OFFERING_CARDINALITY.SU.numberRelOfferUpperLimit\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"1\\\"}]},{\\\"id\\\":\\\"DT_ORDER_LEVEL_PARAMS.deliveryMethods\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"ShipmentWithCollection\\\"},{\\\"value\\\":\\\"CzechPost\\\"},{\\\"value\\\":\\\"Courrier\\\"},{\\\"value\\\":\\\"ByTechnician\\\"},{\\\"value\\\":\\\"ClickAndCollect\\\"},{\\\"value\\\":\\\"TakeAway\\\"}]},{\\\"id\\\":\\\"ItemType\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"TYPE_FLEXI_BUNDLE_TARIFF\\\"}]}],\\\"productOfferingPrice\\\":[{\\\"price\\\":{\\\"taxIncludedAmount\\\":{\\\"unit\\\":\\\"CZK\\\",\\\"value\\\":599}}}]}},{\\\"productOffering\\\":{\\\"id\\\":\\\"PRFB0187\\\",\\\"name\\\":\\\"Unlimited Super One\\\",\\\"prodSpecCharValueUse\\\":[{\\\"id\\\":\\\"DT_FC_OFFERING_PARAMS.Timestamp\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"2022-07-26T09:59:12.220398+02:00\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ecProductName\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Digital NeomezenÃ½ Super\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ServiceLengthUoM\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Month\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.Timestamp\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"2022-07-26T09:07:05.481Z\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.PriceType\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Recurring\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ProductType\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Subscriber Bundle\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ServiceLength\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"24\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ecSection\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"FB\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ProductTypeCode\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Promotion\\\"}]},{\\\"id\\\":\\\"DT_SPEEDCAP.downloadSpeedDescription\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Speed up to 10 Mb/s\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS_PRICE.timestamp\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"2022-10-20T12:54:12.517072+02:00\\\"}]},{\\\"id\\\":\\\"DT_PRODUCT_OFFERING_CARDINALITY.SU.numberRelOfferUpperLimit\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"1\\\"}]},{\\\"id\\\":\\\"DT_ORDER_LEVEL_PARAMS.deliveryMethods\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"ShipmentWithCollection\\\"},{\\\"value\\\":\\\"CzechPost\\\"},{\\\"value\\\":\\\"Courrier\\\"},{\\\"value\\\":\\\"ByTechnician\\\"},{\\\"value\\\":\\\"ClickAndCollect\\\"},{\\\"value\\\":\\\"TakeAway\\\"}]},{\\\"id\\\":\\\"ItemType\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"TYPE_FLEXI_BUNDLE_TARIFF\\\"}]}],\\\"productOfferingPrice\\\":[{\\\"price\\\":{\\\"taxIncludedAmount\\\":{\\\"unit\\\":\\\"CZK\\\",\\\"value\\\":799}}}]}},{\\\"productOffering\\\":{\\\"id\\\":\\\"PRFB0188\\\",\\\"name\\\":\\\"Unlimited Premium 5G One\\\",\\\"prodSpecCharValueUse\\\":[{\\\"id\\\":\\\"DT_FC_OFFERING_PARAMS.Timestamp\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"2022-07-26T09:59:12.220725+02:00\\\"}]},{\\\"id\\\":\\\"DT_SPEEDCAP.downloadSpeedDescription\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"5G speed\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS_PRICE.timestamp\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"2022-08-01T07:49:47.320435+02:00\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ServiceLengthUoM\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Month\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ServiceLength\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"24\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.PriceType\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Recurring\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ecProductName\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Digital NeomezenÃ½ Premium 5G\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.Timestamp\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"2022-07-26T09:07:05.481Z\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ecSection\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"FB\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ProductType\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Subscriber Bundle\\\"}]},{\\\"id\\\":\\\"DT_SIEBEL_PARAMS.ProductTypeCode\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"Promotion\\\"}]},{\\\"id\\\":\\\"DT_PRODUCT_OFFERING_CARDINALITY.SU.numberRelOfferUpperLimit\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"1\\\"}]},{\\\"id\\\":\\\"DT_ORDER_LEVEL_PARAMS.deliveryMethods\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"ShipmentWithCollection\\\"},{\\\"value\\\":\\\"CzechPost\\\"},{\\\"value\\\":\\\"Courrier\\\"},{\\\"value\\\":\\\"ByTechnician\\\"},{\\\"value\\\":\\\"ClickAndCollect\\\"},{\\\"value\\\":\\\"TakeAway\\\"}]},{\\\"id\\\":\\\"ItemType\\\",\\\"productSpecCharacteristicValue\\\":[{\\\"value\\\":\\\"TYPE_FLEXI_BUNDLE_TARIFF\\\"}]}],\\\"productOfferingPrice\\\":[{\\\"price\\\":{\\\"taxIncludedAmount\\\":{\\\"unit\\\":\\\"CZK\\\",\\\"value\\\":1199}}}]}}]}]}}},\\\"duration\\\":13434,\\\"hostname\\\":\\\"graphql-5db5b4c968-8l28t\\\",\\\"environment\\\":\\\"PRE\\\",\\\"service-name\\\":\\\"dxl-graphql-gateway\\\",\\\"timestamp\\\":\\\"2023-01-30T07:53:39.787Z\\\",\\\"version\\\":\\\"1\\\"}\",\"_time\":\"2023-01-30T08:53:39.787+0100\",\"cribl_pipe\":\"CR-235202_VF-CZ_DXL_Microservices_PoC\",\"duration\":\"13434\",\"environment\":\"PRE\",\"event\":\"POST /graphql\",\"host\":\"vgcl58dr\",\"hostname\":\"graphql-5db5b4c968-8l28t\",\"http-status\":\"200\",\"index\":\"kafka_dxl_vfcz_pp\",\"level_value\":\"20000\",\"linecount\":\"1\",\"logger\":\"cz.vodafone.dxl.web.logging.MvcLoggingFilter\",\"message\":\"Response sent\",\"payload-json.data.productOfferingQualificationItems.productOfferingQualificationItem{}.productOfferingQualificationItem{}.productOffering.id\":[\"PRFB0186\",\"PRFB0187\",\"PRFB0188\"],\"payload-json.data.productOfferingQualificationItems.productOfferingQualificationItem{}.productOfferingQualificationItem{}.productOffering.name\":[\"Unlimited Basic One\",\"Unlimited Super One\",\"Unlimited Premium 5G One\"],\"payload-json.data.productOfferingQualificationItems.productOfferingQualificationItem{}.productOfferingQualificationItem{}.productOffering.prodSpecCharValueUse{}.id\":[\"DT_SIEBEL_PARAMS_PRICE.timestamp\",\"DT_SIEBEL_PARAMS.ecProductName\",\"DT_SIEBEL_PARAMS.PriceType\",\"DT_SIEBEL_PARAMS.ServiceLength\",\"DT_SIEBEL_PARAMS.ecSection\",\"DT_SIEBEL_PARAMS.Timestamp\",\"DT_SIEBEL_PARAMS.ProductType\",\"DT_SIEBEL_PARAMS.ProductTypeCode\",\"DT_SIEBEL_PARAMS.ServiceLengthUoM\",\"DT_SPEEDCAP.downloadSpeedDescription\",\"DT_FC_OFFERING_PARAMS.Timestamp\",\"DT_PRODUCT_OFFERING_CARDINALITY.SU.numberRelOfferUpperLimit\",\"DT_ORDER_LEVEL_PARAMS.deliveryMethods\",\"ItemType\",\"DT_FC_OFFERING_PARAMS.Timestamp\",\"DT_SIEBEL_PARAMS.ecProductName\",\"DT_SIEBEL_PARAMS.ServiceLengthUoM\",\"DT_SIEBEL_PARAMS.Timestamp\",\"DT_SIEBEL_PARAMS.PriceType\",\"DT_SIEBEL_PARAMS.ProductType\",\"DT_SIEBEL_PARAMS.ServiceLength\",\"DT_SIEBEL_PARAMS.ecSection\",\"DT_SIEBEL_PARAMS.ProductTypeCode\",\"DT_SPEEDCAP.downloadSpeedDescription\",\"DT_SIEBEL_PARAMS_PRICE.timestamp\",\"DT_PRODUCT_OFFERING_CARDINALITY.SU.numberRelOfferUpperLimit\",\"DT_ORDER_LEVEL_PARAMS.deliveryMethods\",\"ItemType\",\"DT_FC_OFFERING_PARAMS.Timestamp\",\"DT_SPEEDCAP.downloadSpeedDescription\",\"DT_SIEBEL_PARAMS_PRICE.timestamp\",\"DT_SIEBEL_PARAMS.ServiceLengthUoM\",\"DT_SIEBEL_PARAMS.ServiceLength\",\"DT_SIEBEL_PARAMS.PriceType\",\"DT_SIEBEL_PARAMS.ecProductName\",\"DT_SIEBEL_PARAMS.Timestamp\",\"DT_SIEBEL_PARAMS.ecSection\",\"DT_SIEBEL_PARAMS.ProductType\",\"DT_SIEBEL_PARAMS.ProductTypeCode\",\"DT_PRODUCT_OFFERING_CARDINALITY.SU.numberRelOfferUpperLimit\",\"DT_ORDER_LEVEL_PARAMS.deliveryMethods\",\"ItemType\"],\"payload-json.data.productOfferingQualificationItems.productOfferingQualificationItem{}.productOfferingQualificationItem{}.productOffering.prodSpecCharValueUse{}.productSpecCharacteristicValue{}.value\":[\"2022-07-26T14:29:06.775682+02:00\",\"Digital NeomezenÃ½ Basic\",\"Recurring\",\"24\",\"FB\",\"2022-07-26T12:41:13.585Z\",\"Subscriber Bundle\",\"Promotion\",\"Month\",\"Speed up to 2Mb/s\",\"2022-07-26T09:59:12.220039+02:00\",\"1\",\"ShipmentWithCollection\",\"CzechPost\",\"Courrier\",\"ByTechnician\",\"ClickAndCollect\",\"TakeAway\",\"TYPE_FLEXI_BUNDLE_TARIFF\",\"2022-07-26T09:59:12.220398+02:00\",\"Digital NeomezenÃ½ Super\",\"Month\",\"2022-07-26T09:07:05.481Z\",\"Recurring\",\"Subscriber Bundle\",\"24\",\"FB\",\"Promotion\",\"Speed up to 10 Mb/s\",\"2022-10-20T12:54:12.517072+02:00\",\"1\",\"ShipmentWithCollection\",\"CzechPost\",\"Courrier\",\"ByTechnician\",\"ClickAndCollect\",\"TakeAway\",\"TYPE_FLEXI_BUNDLE_TARIFF\",\"2022-07-26T09:59:12.220725+02:00\",\"5G speed\",\"2022-08-01T07:49:47.320435+02:00\",\"Month\",\"24\",\"Recurring\",\"Digital NeomezenÃ½ Premium 5G\",\"2022-07-26T09:07:05.481Z\",\"FB\",\"Subscriber Bundle\",\"Promotion\",\"1\",\"ShipmentWithCollection\",\"CzechPost\",\"Courrier\",\"ByTechnician\",\"ClickAndCollect\",\"TakeAway\",\"TYPE_FLEXI_BUNDLE_TARIFF\"],\"payload-json.data.productOfferingQualificationItems.productOfferingQualificationItem{}.productOfferingQualificationItem{}.productOffering.productOfferingPrice{}.price.taxIncludedAmount.unit\":[\"CZK\",\"CZK\",\"CZK\"],\"payload-json.data.productOfferingQualificationItems.productOfferingQualificationItem{}.productOfferingQualificationItem{}.productOffering.productOfferingPrice{}.price.taxIncludedAmount.value\":[\"599\",\"799\",\"1199\"],\"process-thread\":\"http-nio-8080-exec-4\",\"service-name\":\"dxl-graphql-gateway\",\"severity\":\"INFO\",\"source\":\"caas-log-nonprod-eks\",\"sourcetype\":\"kafka:topicEvent\",\"spanId\":\"9f682f1613922c3e\",\"splunk_server\":\"vgcl52dr\",\"splunk_server_group\":\"default_indexer\",\"timestamp\":\"2023-01-30T07:53:39.787Z\",\"traceId\":\"9587700cf81bdbf0\",\"type\":\"srv-reqresp\",\"user-id\":\"yurzikumlu@gufum.com\",\"user-session-id\":\"27eed615-d086-49dc-946b-43689538be34\",\"version\":\"1\",\"vf-trace-transaction-id\":\"61d5fdce-29b4-47a8-8cac-4954dd56acd0\"}}";
//		Map map = mapper.readValue(data, Map.class);
//		Map result = (Map)map.get("result");
//		Map raw = (Map)result.get("_raw");
//		log.info("payload {} ", raw.get("payload-json"));

		//Assertions.assertNotNull(parseRecord(data).getResult().getHttpMethod());
		SplunkRecord record = parseRecord(data);
		log.info("RECORD {} ", record);
		log.info("getHttpStatus {} ", record.getResult().getHttpStatus());
		log.info("raw {} ", record.getResult().getRaw());
		RawContent raw = mapper.readValue(record.getResult().getRaw(), RawContent.class);
		log.info("raw {}", raw);
		log.info("payload {}", mapper.writeValueAsString(raw.getPayloadJson()));
//		log.info("METHOD {} ", parseRecord(data).getResult().getHttpMethod());
//		log.info("PAYLOAD {} ", parseRecord(data).getResult().getPayloadJson());
//		log.info("RAW {} ", parseRecord(data).getResult().getRaw());
	}


	@Test
	void contextLoads() throws Exception {
		Files.lines(Path.of("/tmp/sesx.json"))
			.map(this::parseRecord)
			.map(SplunkRecord::getResult)
			.filter(r -> r.getTimestamp() != null)
			.sorted(Comparator.comparing(SplunkRecord.Result::getTimestamp))
			.filter(r -> r.getTraceId() != null)
			//.filter(r -> r.getTraceId().equals("0f9ff5b4ec15e944"))
			//.map(r -> String.format("%s %s %s %s %s %s /t/t %s", r.getTimestamp(), r.getServiceName(), r.getTraceId(), r.getSpanId(), r.getHttpMethod(), r.getHttpStatus(), r.getMessage()))
			.collect(
				Collectors.groupingBy(
					SplunkRecord.Result::getTraceId,
					LinkedHashMap::new,
					new SplunkRecordCollector()
				))
				.forEach((k, v) -> {
					System.out.println("");
					System.out.println("====================================================");
					v.print();
//					v.getSpans().forEach(r ->
//						System.out.println(
//							String.format("%s %s %s %s %s %s /t/t %s", r.getTimestamp(), r.getServiceName(), r.getTraceId(), r.getSpanId(), r.getHttpMethod(), r.getHttpStatus(), r.getMessage()))
//					);
			});
	}

	private SplunkRecord parseRecord(String raw) {
		try {
			SplunkRecord record = mapper.readValue(raw, SplunkRecord.class);
//			if (record.getResult().isRaw()) {
//				record.setResult(mapper.readValue(record.getResult().getRaw(), SplunkRecord.Result.class));
//			}
			return record;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private final class SplunkRecordCollector implements Collector<SplunkRecord.Result, SplunkRecordAggregator, SplunkRecordAggregator> {
		@Override
		public Supplier<SplunkRecordAggregator> supplier() {
			return SplunkRecordAggregator::new;
		}
		@Override
		public BiConsumer<SplunkRecordAggregator, SplunkRecord.Result> accumulator() {
			return SplunkRecordAggregator::add;
		}
		@Override
		public BinaryOperator<SplunkRecordAggregator> combiner() {
			return (a, b) -> a;
		}
		@Override
		public Function<SplunkRecordAggregator, SplunkRecordAggregator> finisher() {
			return Function.identity();
		}
		@Override
		public Set<Characteristics> characteristics() {
			return Collections.emptySet();
		}
	}


	private enum ContainerType {
		RequestReceived,
		ResponseSent,
		ClientRequest,
		ClientResponded,
		Other
	}

	@Getter
	private final class SplunkRecordAggregator {
		private Map<String, Container> roots = new LinkedHashMap<>();
		private Map<String, Container> requests = new HashMap<>();
		private List<Container> clientRequests = new ArrayList<>();

		public void add(SplunkRecord.Result span) {
			ContainerType type = resolveContainerType(span);
//			log.info("add {} {} {} - {}", type, span.getIdent(), span.getMessage(), span.getTarget());
//			log.info("\t {} add {} - {}", current != null ? (current.type + ":" + current.request.getServiceName() + ":" + current.request.getSpanId()) : "null", (type + ":" + span.getServiceName() + ":" + span.getSpanId()), span.getMessage());
			switch (type) {
				case Other -> {
					Container other = new OtherContainer(type, span);
					if (!requests.containsKey(span.getIdent())) {
						//throw new IllegalStateException("no parent for OTHER span: " + span);
						if (!roots.containsKey(span.getSpanId())) {
							roots.put(span.getSpanId(), other);
						} else {
							roots.get(span.getSpanId()).addChild(other);
						}
					} else {
						Container parent = requests.get(span.getIdent());
						parent.addChild(other);
					}
				}
				case RequestReceived -> {
					Container requestContainer = new RequestContainer(type, span);
					List<Container> crs = clientRequests.stream()
						.filter(c -> c.matchClientRequest(span)).collect(Collectors.toList());
					if (crs.size() == 0) {
						if (!roots.containsKey(span.getSpanId())) {
							roots.put(span.getSpanId(), requestContainer);
						} else {
							roots.get(span.getSpanId()).addChild(requestContainer);
						}
					} else if (crs.size() == 1) {
						crs.get(0).addChild(requestContainer);
					} else {
						Container cr = crs.stream()
							.filter(c -> c.getChildren() == null || c.getChildren().isEmpty())
							.findFirst()
							.orElseThrow(IllegalStateException::new);
						cr.addChild(requestContainer);
					}
					if (requests.containsKey(span.getIdent())) {
						// throw new IllegalStateException(String.format("Duplicate request %s", span.getIdent()));
					}
					requests.put(span.getIdent(), requestContainer);
				}
				case ResponseSent -> {
					if (!requests.containsKey(span.getIdent())) {
						// throw new IllegalStateException("no parent for request response: " + span);
						log.warn("no parent for request response: " + span);
					} else {
						Container request = requests.get(span.getIdent());
						request.responseSent(span);
					}
				}
				case ClientRequest -> {
					Container clientRequest = new ClientRequestContainer(type, span);
					if (requests.containsKey(span.getIdent())) {
						Container requestContainer = requests.get(span.getIdent());
						requestContainer.addChild(clientRequest);
						clientRequests.add(clientRequest);
					} else {
						if (!roots.containsKey(span.getSpanId())) {
							roots.put(span.getSpanId(), clientRequest);
						} else {
							roots.get(span.getSpanId()).addChild(clientRequest);
						}
						clientRequests.add(clientRequest);
					}
				}
				case ClientResponded -> {
					List<Container> crs = clientRequests.stream()
						.filter(c -> c.matchClientResponse(span)).collect(Collectors.toList());
					if (crs.size() == 1) {
						crs.get(0).clientResponded(span);
					} else {
						Container cr = crs.stream()
							.filter(c -> c.response == null)
							.findFirst()
							.orElseThrow(IllegalStateException::new);
						cr.clientResponded(span);
//						throw new IllegalStateException(
//							String.format("Multiple client request found for response! Requests %s, response %s  ", span, crs)
//						);
					}
				}
			}
		}

		private ContainerType resolveContainerType(SplunkRecord.Result request) {
			if (request.getMessage() == null) {
				return ContainerType.Other;
			}
			String sanitized = request.getMessage().trim().toLowerCase();
			if (sanitized.startsWith("request received")) {
				return ContainerType.RequestReceived;
			} else if (sanitized.startsWith("client request")) {
				return ContainerType.ClientRequest;
			} else if (sanitized.startsWith("response sent")) {
				return ContainerType.ResponseSent;
			} else if (sanitized.startsWith("client responded")) {
				return ContainerType.ClientResponded;
			} else {
				return ContainerType.Other;
			}
		}

		public void print() {
			roots.values().forEach(r -> r.print(""));
		}
	}

	private interface ContainerState {

		void requestReceived(SplunkRecord.Result request);

		void responseSent(SplunkRecord.Result reponse);

		void clientRequested(SplunkRecord.Result request);

		void clientResponded(SplunkRecord.Result reponse);
	}

	@Getter
	@RequiredArgsConstructor
	private class Container implements ContainerState {
		private final ContainerType type;
//		private final Container parent;
		private final SplunkRecord.Result request;
		private SplunkRecord.Result response;

		private List<Container> children;

		public void addChild(Container container) {
			if (children == null) {
				children = new ArrayList<>();
			}
			children.add(container);
		}

		protected void setResponse(SplunkRecord.Result response) {
			this.response = response;
		}

		protected void tracingMatch(SplunkRecord.Result response) {
			if (
				!request.getServiceName().equals(response.getServiceName()) ||
				!request.getTraceId().equals(response.getTraceId()) ||
				!request.getSpanId().equals(response.getSpanId()) ||
				(request.getEvent()!=null && response.getEvent()!=null && !request.getEvent().equals(response.getEvent()))
			) {
				log.warn(
					String.format("Tracing do not match:\n\t%s\n\t%s", request, response));
				//throw new IllegalStateException("Tracing do not match!");
			}
		}

		private boolean matchClientRequest(SplunkRecord.Result request) {
			String[] parts = request.getEvent().split(" ");
			if (parts.length != 2) {
				return false;
			}
			return this.request.getMessage().indexOf(parts[0]) > -1 && this.request.getMessage().indexOf(parts[1]) > -1;
		}

		private boolean matchClientResponse(SplunkRecord.Result response) {
			return this.request.getIdent().equals(response.getIdent()) && this.request.getTarget().equals(response.getTarget());
		}

		@Override
		public void requestReceived(SplunkRecord.Result request) {
			//throw new UnsupportedOperationException("requestReceived is not supported");
		}

		@Override
		public void responseSent(SplunkRecord.Result response) {
			//throw new UnsupportedOperationException("responseSent is not supported");
		}

		@Override
		public void clientRequested(SplunkRecord.Result request) {
			//throw new UnsupportedOperationException("clientRequested is not supported");
		}

		@Override
		public void clientResponded(SplunkRecord.Result response) {
//			throw new UnsupportedOperationException(
//				String.format("clientResponded is not supported for %s ", this));
		}

		public void print(String indent) {
			System.out.println(String.format(
				"%s %s %s:%s", indent, type, request, response));
//			System.out.println(String.format("\t  %s %s", indent, getRequestPayload()));
//			System.out.println(String.format("\t  %s %s", indent, getResponsePayload()));
			Optional.ofNullable(children).orElse(Collections.emptyList()).forEach(ch -> ch.print(indent + "\t"));
		}

		@Override
		public String toString() {
			return request.toString();
		}

		private String getRequestPayload() {
			if (request == null || request.getRaw() == null) {
				return null;
			}
			try {
				RawContent raw = mapper.readValue(request.getRaw(), RawContent.class);
				return mapper.writeValueAsString(raw.getPayloadJson());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private String getResponsePayload() {
			if (response == null || response.getRaw() == null) {
				return null;
			}
			try {
				RawContent raw = mapper.readValue(response.getRaw(), RawContent.class);
				return mapper.writeValueAsString(raw.getPayloadJson());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private final class OtherContainer extends Container {
		public OtherContainer(ContainerType type, SplunkRecord.Result request) {
			super(type, request);
		}

		@Override
		public void requestReceived(SplunkRecord.Result request) {}

		@Override
		public void clientRequested(SplunkRecord.Result request) {}
	}

	private final class RequestContainer extends Container {
		public RequestContainer(ContainerType type, SplunkRecord.Result request) {
			super(type, request);
		}

		@Override
		public void clientRequested(SplunkRecord.Result request) {}

		@Override
		public void responseSent(SplunkRecord.Result response) {
			tracingMatch(response);
			setResponse(response);
		}
	}

	private final class ClientRequestContainer extends Container {
		public ClientRequestContainer(ContainerType type, SplunkRecord.Result request) {
			super(type, request);
		}

		@Override
		public void requestReceived(SplunkRecord.Result request) {}

		@Override
		public void clientRequested(SplunkRecord.Result request) {}

		@Override
		public void clientResponded(SplunkRecord.Result response) {
			tracingMatch(response);
			setResponse(response);
		}
	}
}
