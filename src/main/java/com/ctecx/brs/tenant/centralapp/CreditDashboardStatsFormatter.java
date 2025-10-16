// src/main/java/com/ctecx/brs/tenant/centralapp/CreditDashboardStatsFormatter.java
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
public class CreditDashboardStatsFormatter {

    private final ObjectMapper objectMapper;

    /**
     * Parses the raw data from the GetCreditDashboardStats stored procedure.
     * The procedure returns a single row with a single column 'dashboard_json'
     * containing a JSON string. This method extracts and parses that string.
     *
     * @param rawData The raw list of results from the database.
     * @return A Map representing the fully parsed dashboard data, or an empty map if parsing fails.
     */
    public Map<String, Object> format(List<Map<String, Object>> rawData) {
        // Expecting a single row with a single column
        if (rawData == null || rawData.isEmpty() || rawData.get(0) == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> firstRow = rawData.get(0);
        Object jsonValue = firstRow.get("dashboard_json");

        if (jsonValue instanceof String jsonString && !jsonString.trim().isEmpty()) {
            try {
                // Parse the JSON string into a generic Map structure
                return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
            } catch (JsonProcessingException e) {
                System.err.println("Failed to parse credit dashboard JSON: " + e.getMessage());
                // Return an empty map on parsing failure to prevent API errors
                return Collections.emptyMap();
            }
        }

        // Return empty map if the key is not found or the value is not a string
        return Collections.emptyMap();
    }
}