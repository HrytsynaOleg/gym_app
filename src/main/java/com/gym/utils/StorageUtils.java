package com.gym.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.model.User;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Log4j2
public class StorageUtils {
    public static <T> T buildMapFromFile(String filePath, TypeReference<T> typeReference) {
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            T result = JsonUtils.parseInputStream(inputStream, typeReference);
            log.info(String.format("Storage from %s was built successfully", filePath));
            return result;
        } catch (IOException e) {
            log.error("Source file error - " + filePath);
            return (T) Map.of();
        }
    }

    public static <T> long generateId(Map<String, T> storageMap) {
        Set<String> keys = storageMap.keySet();
        long maxValue = keys.stream()
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0);
        return maxValue + 1;
    }

    public static <T extends User> long getUserCountByUserName(Map<String, T> storageMap, String userName) {
        return storageMap.values().stream()
                .filter(s -> StringUtils.isMatch(s.getUserName(), "^" + userName + "\\d*+\\z"))
                .count();
    }

    public static String generateUserName(String firstName, String lastName, long existUserCount) {
        String userName = firstName + "." + lastName;
        if (existUserCount > 0) {
            userName = userName + existUserCount;
        }
        return userName;
    }
}
