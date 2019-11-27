package org.coderdreams.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public final class EntityUtil {

    private static final Logger log = LoggerFactory.getLogger( EntityUtil.class ) ;

	private static final ObjectMapper mapper;
	private static final ObjectReader reader;
	private static final ObjectWriter writer;

	private EntityUtil() {
        throw new IllegalStateException("Utility class");
    }

	static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        SimpleModule module = new SimpleModule();
        mapper.registerModule(module);

		reader = mapper.reader();
		writer = mapper.writer();
	}

	public static String objectToJson(Object object) {
	    if(object == null) {
	        return "";
        }
		try {
			return writer.writeValueAsString(object);
		} catch (Exception e) {
			log.error("FAILED objectToJson {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
    }
	public static <V> V jsonToObject(String data, Class<V> valueType) {
		if(StringUtils.isBlank(data)) {
		    return null;
        }
		try {
			return mapper.readValue(data, valueType);
		} catch (Exception e) {
			log.error("FAILED jsonToObject ({}) {}", data, e.getMessage());
			return null;
		}
    }

	
	public static JsonNode toJsonNode(String value) {
		if(StringUtils.isBlank(value)) {
		    return null;
        }
        try {
			return reader.readTree(value);
        } catch (Exception e) {        	
        	log.error("FAILED toJsonNode ({}) {}", value, e.getMessage());
			return null;
        }
	}

	public static <T> T clone(T value) {
		if(value == null) {
			return null;
		}
		return jsonToObject(objectToJson(value), (Class<T>) value.getClass());
	}


}
