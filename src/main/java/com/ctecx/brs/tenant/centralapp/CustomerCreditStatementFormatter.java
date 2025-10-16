// src/main/java/com/ctecx/brs/tenant/centralapp/CustomerCreditStatementFormatter.java
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
public class CustomerCreditStatementFormatter {

    private final ObjectMapper objectMapper;

    /**
     * Parses the raw data from the GetCustomerCreditStatement stored procedure.
     *
     * @param rawDataList The raw list of results from the database.
     * @return A List of Maps representing the fully parsed credit statement lines.
     */
    public List<Map<String, Object>> format(List<Map<String, Object>> rawDataList) {
        if (rawDataList == null || rawDataList.isEmpty()) {
            return Collections.emptyList();
        }

        for (Map<String, Object> transaction : rawDataList) {
            parseJsonField(transaction, "items", new TypeReference<List<Map<String, Object>>>() {});
        }

        return rawDataList;
    }

    private <T> void parseJsonField(Map<String, Object> dataMap, String key, TypeReference<T> typeRef) {
        Object value = dataMap.get(key);
        if (value instanceof String jsonString && !jsonString.trim().isEmpty()) {
            try {
                T parsedObject = objectMapper.readValue(jsonString, typeRef);
                dataMap.put(key, parsedObject);
            } catch (JsonProcessingException e) {
                System.err.println("Failed to parse JSON for key '" + key + "': " + e.getMessage());
                dataMap.put(key, Collections.emptyList());
            }
        }
    }
}