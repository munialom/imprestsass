package com.ctecx.brs.tenant.centralapp;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResultSetFormatter {

    /**
     * Formats the output of a stored procedure that returns two result sets:
     * 1. A list of data for the current page.
     * 2. A single row with a total count.
     *
     * @param rawData The raw map returned from SimpleJdbcCall.
     * @return A map containing "data" (List) and "totalRecords" (Integer).
     */
    public Map<String, Object> formatPaginatedResponse(Map<String, Object> rawData) {
        if (rawData == null || rawData.isEmpty()) {
            return Map.of("data", Collections.emptyList(), "totalRecords", 0);
        }

        // Spring JDBC often keys result sets with "#result-set-N"
        List<Map<String, Object>> data = (List<Map<String, Object>>) rawData.get("#result-set-1");
        List<Map<String, Object>> countResult = (List<Map<String, Object>>) rawData.get("#result-set-2");

        int totalRecords = 0;
        if (countResult != null && !countResult.isEmpty()) {
            // The column name is 'TotalCount' in the SearchPaginateCustomers SP
            Object countValue = countResult.get(0).get("TotalCount");
            if (countValue instanceof Number) {
                totalRecords = ((Number) countValue).intValue();
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", data != null ? data : Collections.emptyList());
        response.put("totalRecords", totalRecords);
        return response;
    }

    /**
     * Formats the output of a stored procedure that returns two result sets:
     * 1. A list of transaction details.
     * 2. A single row with summary data.
     *
     * @param rawData The raw map returned from SimpleJdbcCall.
     * @return A map containing "transactions" (List) and "summary" (Map).
     */
    public Map<String, Object> formatTransactionWithSummaryResponse(Map<String, Object> rawData) {
        if (rawData == null || rawData.isEmpty()) {
            return Map.of("transactions", Collections.emptyList(), "summary", Collections.emptyMap());
        }

        List<Map<String, Object>> transactions = (List<Map<String, Object>>) rawData.get("#result-set-1");
        List<Map<String, Object>> summaryResult = (List<Map<String, Object>>) rawData.get("#result-set-2");

        Map<String, Object> summary = Collections.emptyMap();
        if (summaryResult != null && !summaryResult.isEmpty()) {
            summary = summaryResult.get(0);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions != null ? transactions : Collections.emptyList());
        response.put("summary", summary);
        return response;
    }
}