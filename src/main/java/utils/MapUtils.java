package utils;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.FileInputStream;
import java.io.IOException;

public class MapUtils {
    public static <T> T buildMapFromFile(String filePath) {
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            return JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
