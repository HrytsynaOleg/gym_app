package com.gym.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;

@Log4j2
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T parseJsonString(String json, TypeReference<T> typeReference) {
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error(String.format("Could not parse json: %s%n", json));
        }
        return null;
    }

    public static <T> T parseInputStream(InputStream input, TypeReference<T> typeReference) {
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(input, typeReference);
        } catch (JsonProcessingException e) {
            log.error(String.format("Could not parse json%n"));
        } catch (IOException e) {
            log.error(String.format("Error reading input stream%n"));
        }
        return null;
    }

    public static <T> T parseResource(String resourceName, TypeReference<T> typeReference) {
        try (InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(resourceName)) {
            mapper.registerModule(new JavaTimeModule());
            try {
                return mapper.readValue(inputStream, typeReference);
            } catch (JsonProcessingException e) {
                log.error(String.format("Could not parse json%n"));
            }
        } catch (IOException e) {
            log.error(String.format("Error reading resource file: %s%n", resourceName));
        }
        return null;
    }

    public static String convertObjectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(String.format("Could not parse json%n"));
        }
        return "";
    }
}
