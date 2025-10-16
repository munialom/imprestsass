package com.ctecx.brs.tenant.centralapp; // Use your actual package name

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SalesAnalyticsFormatter {

    private final ObjectMapper objectMapper;

    /**
     * Parses the raw JSON string from the GetSalesAnalyticsReport stored procedure
     * into a structured Map.
     *
     * @param rawDataList The raw result from the database, expected to be a single-element list.
     * @return A Map representing the parsed JSON object.
     */
    public Map<String, Object> formatSalesAnalytics(List<Map<String, Object>> rawDataList) {
        // 1. Handle edge cases: if the database returns nothing, return an empty map.
        if (rawDataList == null || rawDataList.isEmpty()) {
            return Collections.emptyMap();
        }

        // 2. Get the single row and the 'analytics_json' column which contains the JSON string.
        Map<String, Object> rawData = rawDataList.get(0);
        String jsonString = (String) rawData.get("analytics_json");

        // 3. Handle cases where the JSON string itself is null or empty.
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            // 4. Use Jackson ObjectMapper to parse the string into a Map.
            return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            // 5. If parsing fails, throw a runtime exception for clear error reporting.
            throw new RuntimeException("Failed to parse sales analytics JSON: " + e.getMessage(), e);
        }
    }
}