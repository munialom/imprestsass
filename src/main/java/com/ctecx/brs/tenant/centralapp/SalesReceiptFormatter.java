package com.ctecx.brs.tenant.centralapp;

import com.ctecx.brs.tenant.exception.EntityNotFoundException;
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
public class SalesReceiptFormatter {

    private final ObjectMapper objectMapper;

    /**
     * Parses the raw data from the GetSalesReceipt stored procedure into a structured Map.
     * It specifically parses the 'items' and 'paymentSummary' JSON strings into nested lists of objects.
     *
     * @param rawDataList The raw result from the database, expected to contain a single sales receipt.
     * @return A Map representing the fully parsed sales receipt.
     * @throws EntityNotFoundException if no receipt is found for the given serial number.
     */
    public Map<String, Object> formatSalesReceipt(List<Map<String, Object>> rawDataList) {
        if (rawDataList == null || rawDataList.isEmpty()) {
            throw new EntityNotFoundException("Sales receipt not found.");
        }

        // The procedure is designed to return one row for a unique serial number
        Map<String, Object> receipt = rawDataList.get(0);

        // Parse the 'items' JSON string into a List of Maps
        parseAndReplaceJsonString(receipt, "items");

        // Parse the 'paymentSummary' JSON string into a List of Maps
        parseAndReplaceJsonString(receipt, "paymentSummary");

        return receipt;
    }

    /**
     * Helper method to parse a JSON string field within the map and replace it with the parsed object.
     *
     * @param dataMap The map containing the data.
     * @param key     The key of the field containing the JSON string.
     */
    private void parseAndReplaceJsonString(Map<String, Object> dataMap, String key) {
        Object value = dataMap.get(key);
        if (value instanceof String jsonString) {
            try {
                // Parse the JSON string into a List<Map<String, Object>>
                List<Map<String, Object>> parsedObject = objectMapper.readValue(jsonString, new TypeReference<>() {});
                dataMap.put(key, parsedObject); // Replace the string with the parsed object
            } catch (JsonProcessingException e) {
                // If parsing fails, replace with an empty list and log the error for debugging
                System.err.println("Failed to parse JSON for key '" + key + "': " + e.getMessage());
                dataMap.put(key, Collections.emptyList());
            }
        }
    }
}