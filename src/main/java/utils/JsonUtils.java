package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T parseJsonString(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            System.out.printf("Could not parse json: %s%n", json);
        }
        return null;
    }

    public static <T> T parseInputStream(InputStream input, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(input, typeReference);
        } catch (JsonProcessingException e) {
            System.out.println("Could not parse json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
