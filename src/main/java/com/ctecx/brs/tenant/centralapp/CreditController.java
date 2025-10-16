package com.ctecx.brs.tenant.centralapp;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/credit-reports")
@AllArgsConstructor
public class CreditController {

    private final CustomBrsService customBrsService;

    /**
     * GET /api/v1/credit-reports/customer-summary
     * Retrieves a summarized credit report for all active customers for a given date range.
     *
     * @param startDate Optional start date in 'YYYY-MM-DD' format. The SP defaults if null.
     * @param endDate   Optional end date in 'YYYY-MM-DD' format. The SP defaults if null.
     * @return A JSON array of customer credit summaries.
     */
    @GetMapping("/customer-summary")
    public ResponseEntity<List<Map<String, Object>>> getCustomerCreditSummary(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        List<Map<String, Object>> reportData = customBrsService.getCustomerCreditSummary(startDate, endDate);
        return ResponseEntity.ok(reportData);
    }


}