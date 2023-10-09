package com.kuddy.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ObjectMapperUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readValue(String json, Class<T> valueType) throws JsonProcessingException {
        return objectMapper.readValue(json, valueType);
    }

    public static <T> T readValue(String json, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        return objectMapper.readValue(json, valueTypeRef);
    }

    public static String writeValueAsString(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

}
