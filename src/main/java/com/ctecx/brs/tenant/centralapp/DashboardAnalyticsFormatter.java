package com.ctecx.brs.tenant.centralapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DashboardAnalyticsFormatter {

    private final ObjectMapper objectMapper;

    /**
     * Parses the raw JSON string from the GetDashboardAnalytics stored procedure.
     * The procedure returns a single row with a single column containing the entire dashboard JSON.
     *
     * @param rawDataList The raw list from the database, expected to contain one map with the key 'dashboard_json'.
     * @return A Map representing the fully parsed dashboard analytics data, or an empty map if parsing fails or data is absent.
     */
    public Map<String, Object> format(List<Map<String, Object>> rawDataList) {
        if (rawDataList == null || rawDataList.isEmpty() || !rawDataList.get(0).containsKey("dashboard_json")) {
            return Collections.emptyMap();
        }

        Object value = rawDataList.get(0).get("dashboard_json");

        if (value instanceof String jsonString && !jsonString.trim().isEmpty()) {
            try {
                // Parse the JSON string into a Map<String, Object>
                return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
            } catch (JsonProcessingException e) {
                // Log the error and return an empty map as a safe default
                System.err.println("Failed to parse dashboard analytics JSON: " + e.getMessage());
                return Collections.emptyMap();
            }
        }

        return Collections.emptyMap();
    }
}