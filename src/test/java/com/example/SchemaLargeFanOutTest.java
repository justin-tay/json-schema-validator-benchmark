package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.serialization.JsonMapperFactory;

public class SchemaLargeFanOutTest {
	static final String DATA = "{\r\n"
			+ "  \"variables\": {\r\n"
			+ "    \"eu7_\": {\r\n"
			+ "      \"name\": \"occaecat in\",\r\n"
			+ "      \"comment\": [\r\n"
			+ "        \"nisi magna minim\",\r\n"
			+ "        \"in Ut\"\r\n"
			+ "      ]\r\n"
			+ "    }\r\n"
			+ "  },\r\n"
			+ "  \"output\": {\r\n"
			+ "    \"progress\": true,\r\n"
			+ "    \"stages\": [\r\n"
			+ "      \"Excepteur in Ut exercitation\",\r\n"
			+ "      \"magna deserunt sit\",\r\n"
			+ "      \"in adipisicing esse commodo\",\r\n"
			+ "      \"velit tempor in\"\r\n"
			+ "    ]\r\n"
			+ "  },\r\n"
			+ "  \"useCache\": true,\r\n"
			+ "  \"comment\": [\r\n"
			+ "    \"nisi sit labore ad\"\r\n"
			+ "  ]\r\n"
			+ "}";

	public static void main(String[] args) {
		JsonSchema schema;
		JsonNode data;
		schema = JsonSchemaFactory.getInstance(VersionFlag.V202012).getSchema(
				SchemaLocation.of("classpath:schema/large-fan-out-schema.json"),
				SchemaValidatorsConfig.builder().cacheRefs(false).build());
		try {
			data = JsonMapperFactory.getInstance().readTree(DATA);
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException(e);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
		schema.validate(data);
	}
}
