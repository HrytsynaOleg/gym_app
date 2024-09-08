package com.gym.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.model.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StorageUtils {
    public static <T> T buildMapFromFile(String filePath, TypeReference<T> typeReference) {
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            return JsonUtils.parseInputStream(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> long generateId(Map<String, T> storageMap){
        Set<String> keys = storageMap.keySet();
        long maxValue = keys.stream()
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0);
        return  maxValue + 1;
    }

    public static <T extends User> List<T> findByUserName(Map<String, T> storageMap, String userName){
        return storageMap.values().stream()
                .filter(s -> StringUtils.isMatch(s.getUserName(), "^" + userName + "\\d*+\\z"))
                .toList();
    }
}
