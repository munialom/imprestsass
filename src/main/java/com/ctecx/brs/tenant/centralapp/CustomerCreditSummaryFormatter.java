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
public class CustomerCreditSummaryFormatter {

    private final ObjectMapper objectMapper;

    /**
     * Parses the raw JSON string from the GetCustomerCreditSummary stored procedure.
     * The procedure returns a single row with a single column containing a JSON array.
     *
     * @param rawData The raw list of results from the database, expected to contain one map.
     * @return A List of Maps representing the fully parsed customer credit summaries.
     */
    public List<Map<String, Object>> format(List<Map<String, Object>> rawData) {
        // 1. Validate that the raw data structure is as expected
        if (rawData == null || rawData.isEmpty() || rawData.get(0) == null) {
            return Collections.emptyList();
        }

        // 2. Extract the JSON string from the first row's 'customer_credit_summary' column
        Object jsonData = rawData.get(0).get("customer_credit_summary");
        if (!(jsonData instanceof String jsonString) || jsonString.trim().isEmpty() || "null".equalsIgnoreCase(jsonString)) {
            return Collections.emptyList();
        }

        try {
            // 3. Parse the JSON string into the target structure (a List of Maps)
            return objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            // 4. Handle JSON parsing errors gracefully
            // In a real application, you should log this exception
            System.err.println("Failed to parse customer credit summary JSON: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}