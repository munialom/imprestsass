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
public class SalesTransactionSummaryFormatter {

    private final ObjectMapper objectMapper;

    /**
     * Parses the raw data list from the GetSalesTransactionSummary stored procedure.
     * It iterates through each transaction and parses the 'items', 'paymentSummary',
     * and 'summary' JSON strings into nested objects.
     *
     * @param rawDataList The raw list of results from the database.
     * @return A List of Maps representing the fully parsed sales transactions.
     */
    public List<Map<String, Object>> format(List<Map<String, Object>> rawDataList) {
        if (rawDataList == null || rawDataList.isEmpty()) {
            return Collections.emptyList();
        }

        // Iterate over each transaction record in the list
        for (Map<String, Object> transaction : rawDataList) {
            // Parse the JSON string fields for the current transaction
            parseJsonField(transaction, "items", new TypeReference<List<Map<String, Object>>>() {});
            parseJsonField(transaction, "paymentSummary", new TypeReference<List<Map<String, Object>>>() {});
            parseJsonField(transaction, "summary", new TypeReference<Map<String, Object>>() {});
        }

        return rawDataList;
    }

    /**
     * A generic helper method to parse a JSON string field within a map.
     *
     * @param dataMap The map containing the data.
     * @param key     The key of the field containing the JSON string.
     * @param typeRef The Jackson TypeReference describing the target type (e.g., List or Map).
     */
    private <T> void parseJsonField(Map<String, Object> dataMap, String key, TypeReference<T> typeRef) {
        Object value = dataMap.get(key);
        if (value instanceof String jsonString && !jsonString.trim().isEmpty()) {
            try {
                // Parse the JSON string into the specified generic type
                T parsedObject = objectMapper.readValue(jsonString, typeRef);
                dataMap.put(key, parsedObject); // Replace the string with the parsed object
            } catch (JsonProcessingException e) {
                // If parsing fails, log the error and decide on a fallback
                // For a list, an empty list is a safe default. For an object, an empty map.
                System.err.println("Failed to parse JSON for key '" + key + "': " + e.getMessage());
                if (typeRef.getType().getTypeName().startsWith("java.util.List")) {
                    dataMap.put(key, Collections.emptyList());
                } else {
                    dataMap.put(key, Collections.emptyMap());
                }
            }
        }
    }
}