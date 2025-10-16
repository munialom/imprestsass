package com.ctecx.brs.tenant.centralapp;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/brs")
@RequiredArgsConstructor
public class CustomBrsController {
    private final CustomBrsService customBrsService;
    


    // Existing Endpoints
    @GetMapping("/ledger-accounts/search")
    public ResponseEntity<List<Map<String, Object>>> SearchPaginateChartOfAccounts(
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        return ResponseEntity.ok(customBrsService.SearchPaginateChartOfAccounts(searchTerm, pageNumber));
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Map<String, Object>>> SearchPaginateProducts(
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        return ResponseEntity.ok(customBrsService.SearchPaginateProducts(searchTerm, pageNumber));
    }

    @GetMapping("/products/search/positive")
    public ResponseEntity<List<Map<String, Object>>> searchProductsWithPositiveStock(
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        return ResponseEntity.ok(customBrsService.searchProductsWithPositiveStock(searchTerm, pageNumber));
    }

    @GetMapping("/products/search/all")
    public ResponseEntity<List<Map<String, Object>>> searchAllProducts(
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        return ResponseEntity.ok(customBrsService.searchAllProducts(searchTerm, pageNumber));
    }

    // New Endpoints for the added stored procedures

    @GetMapping("/products/stock/search-by-category")
    public ResponseEntity<List<Map<String, Object>>> searchProductsByStockAndCategory(
            @RequestParam(required = false, defaultValue = "") String searchKey,
            @RequestParam(required = false, defaultValue = "") String categoryIds,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(customBrsService.searchProductsByStockLevelAndCategory(searchKey, categoryIds, pageNumber, pageSize));
    }

    @GetMapping("/products/stock/search")
    public ResponseEntity<List<Map<String, Object>>> searchProductsByStock(
            @RequestParam(required = false, defaultValue = "") String searchKey,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(customBrsService.searchProductsByStockLevel(searchKey, pageNumber, pageSize));
    }

    @GetMapping("/products/stock/search-by-location")
    public ResponseEntity<List<Map<String, Object>>> searchProductsByStockAndLocation(
            @RequestParam(required = false, defaultValue = "") String searchKey,
            @RequestParam(required = false, defaultValue = "") String locationIds,
            @RequestParam(required = false, defaultValue = "") String categoryIds,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(customBrsService.searchProductsByStockLevelAndLocation(searchKey, locationIds, categoryIds, pageNumber, pageSize));
    }

    @GetMapping("/products/stock/search-by-expiry")
    public ResponseEntity<List<Map<String, Object>>> searchProductsByStockAndExpiry(
            @RequestParam(required = false, defaultValue = "") String searchKey,
            @RequestParam(required = false, defaultValue = "") String locationIds,
            @RequestParam(required = false, defaultValue = "") String categoryIds,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(customBrsService.searchProductsByStockLevelAndExpiry(searchKey, locationIds, categoryIds, pageNumber, pageSize));
    }
    
    @GetMapping("/products/stock/search-by-submodule")
    public ResponseEntity<List<Map<String, Object>>> searchProductsByStockAndSubmodule(
            @RequestParam(required = false, defaultValue = "") String searchKey,
            @RequestParam(required = false, defaultValue = "") String submodules,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(customBrsService.searchProductsByStockLevelAndSubmodule(searchKey, submodules, pageNumber, pageSize));
    }

    @GetMapping("/products/stock/by-store")
    public ResponseEntity<List<Map<String, Object>>> getProductStockByStore(
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(required = false) Long storeId, // Can be null for all stores
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(customBrsService.getProductStockByStorePaginated(searchTerm, storeId, pageNumber, pageSize));
    }
    
    @GetMapping("/reports/stock-movement/daily")
    public ResponseEntity<List<Map<String, Object>>> getDailyStockMovementReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
            @RequestParam int branchId) {
        return ResponseEntity.ok(customBrsService.getDailyStockMovementReport(startDate, endDate, branchId));
    }

    @GetMapping("/reports/sales/monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthlySalesReport(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int branchId) {
        return ResponseEntity.ok(customBrsService.getMonthlySalesReport(year, month, branchId));
    }

    // New Endpoint for GetInventoryProducts
    @GetMapping("/products/inventory/search")
    public ResponseEntity<List<Map<String, Object>>> getInventoryProducts(
            @RequestParam(required = false, defaultValue = "") String searchKey,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(customBrsService.getInventoryProducts(searchKey, pageNumber, pageSize));
    }

    @GetMapping("/reports/inventory/valuation")
    public ResponseEntity<List<Map<String, Object>>> getInventoryValuation(
            @RequestParam(required = false, defaultValue = "") String searchKey,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(customBrsService.getInventoryValuation(searchKey, pageNumber, pageSize));
    }

    @GetMapping("/reports/sales/details")
    public ResponseEntity<List<Map<String, Object>>> getDetailedSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getSalesReport(startDate, endDate));
    }

    @GetMapping("/reports/purchase/details")
    public ResponseEntity<List<Map<String, Object>>> getDetailedPurchaseReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getPurchaseReport(startDate, endDate));
    }

    @GetMapping("/reports/stock/add-details")
    public ResponseEntity<List<Map<String, Object>>> getDetailedStockAddReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getStockAddReport(startDate, endDate));
    }

    @GetMapping("/reports/stock/subtract-details")
    public ResponseEntity<List<Map<String, Object>>> getDetailedStockSubtractReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getStockSubtractReport(startDate, endDate));
    }

    @GetMapping("/reports/stock/expiry-analysis")
    public ResponseEntity<List<Map<String, Object>>> getExpiryAnalysisReport(
            @RequestParam(required = false) Integer warnDays) {
        return ResponseEntity.ok(customBrsService.getExpiryAnalysis(warnDays));
    }

    @GetMapping("/reports/sales/transactions")
    public ResponseEntity<List<Map<String, Object>>> getSalesTransactionsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getSalesTransactionsReport(startDate, endDate));
    }

    @GetMapping("/reports/sales/payment-mode-analysis-by-user")
    public ResponseEntity<List<Map<String, Object>>> getPaymentModeAnalysisByUser(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getPaymentModeAnalysisByUser(startDate, endDate));
    }

    @GetMapping("/reports/sales/profit-analysis")
    public ResponseEntity<List<Map<String, Object>>> getSalesProfitAnalysisReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
            @RequestParam(defaultValue = "daily") String groupBy) { // 'daily', 'weekly', or 'monthly'
        return ResponseEntity.ok(customBrsService.getSalesProfitAnalysis(startDate, endDate, groupBy));
    }

    @GetMapping("/reports/sales/monthly-breakdown")
    public ResponseEntity<List<Map<String, Object>>> getMonthlySalesBreakdownReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getMonthlySalesBreakdown(startDate, endDate));
    }


    @GetMapping("/reports/sales/top-selling-products")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingProductsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(customBrsService.getTopSellingProducts(startDate, endDate, limit));
    }

    @GetMapping("/reports/sales/day-of-week-analysis")
    public ResponseEntity<List<Map<String, Object>>> getDayOfWeekSalesAnalysisReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getDayOfWeekSalesAnalysis(startDate, endDate));
    }

    @GetMapping("/reports/sales/analytics")
    public ResponseEntity<Map<String, Object>> getSalesAnalyticsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getSalesAnalyticsReport(startDate, endDate));
    }

    @GetMapping("/products/stock/{productCode}")
    public ResponseEntity<Map<String, Object>> getProductStockByCode(
            @PathVariable String productCode) {
        // Call the service to get the stock value
        Integer currentStock = customBrsService.getProductStockByCode(productCode);

        // Return a structured JSON response, which is better practice than a raw value.
        Map<String, Object> response = Map.of(
                "productCode", productCode,
                "currentStock", currentStock
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/brs/receipts/{serialNumber} : Retrieves a single sales receipt.
     *
     * @param serialNumber The unique serial number of the sales transaction (e.g., "SALE-20250915-00002").
     * @return A ResponseEntity containing the structured sales receipt data.
     */
    @GetMapping("/receipts/{serialNumber}")
    public ResponseEntity<Map<String, Object>> getSalesReceipt(@PathVariable String serialNumber) {
        return ResponseEntity.ok(customBrsService.getSalesReceipt(serialNumber));
    }

    /**
     * GET /api/v1/brs/reports/sales/transaction-summary : Retrieves a summary of sales transactions.
     *
     * @param startDate The start date for the report (format: YYYY-MM-DD).
     * @param endDate   The end date for the report (format: YYYY-MM-DD).
     * @return A ResponseEntity containing a list of sales transaction summaries.
     */
    @GetMapping("/reports/sales/transaction-summary")
    public ResponseEntity<List<Map<String, Object>>> getSalesTransactionSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getSalesTransactionSummary(startDate, endDate));
    }

    /**
     * GET /api/v1/brs/dashboard/analytics : Retrieves dashboard analytics data.
     *
     * @param year  The year for the analytics report (e.g., 2024).
     * @param month The month for the analytics report (e.g., 9 for September).
     * @return A ResponseEntity containing the structured dashboard data.
     */
    @GetMapping("/dashboard/analytics")
    public ResponseEntity<Map<String, Object>> getDashboardAnalytics(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(customBrsService.getDashboardAnalytics(year, month));
    }

    @GetMapping("/suppliers/search")
    public ResponseEntity<Map<String, Object>> searchPaginateSuppliers(
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "15") int pageSize) {
        return ResponseEntity.ok(customBrsService.searchPaginateSuppliers(searchTerm, pageNumber, pageSize));
    }

    @GetMapping("/suppliers/{supplierId}/transactions")
    public ResponseEntity<Map<String, Object>> getSupplierTransactions(
            @PathVariable Long supplierId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getSupplierTransactions(supplierId, startDate, endDate));
    }

    @GetMapping("/suppliers/dashboard-analytics")
    public ResponseEntity<List<Map<String, Object>>> getSupplierDashboardAnalytics(
            @RequestParam int year,
            @RequestParam(required = false) Integer month) {
        return ResponseEntity.ok(customBrsService.getSupplierDashboardAnalytics(year, month));
    }



    @GetMapping("/customers/{customerId}/transactions")
    public ResponseEntity<List<Map<String, Object>>> getCustomerTransactions(
            @PathVariable Long customerId,
            @RequestParam int year,
            @RequestParam(required = false) Integer month) {
        return ResponseEntity.ok(customBrsService.getCustomerTransactions(customerId, year, month));
    }

    @GetMapping("/customers/dashboard-analytics")
    public ResponseEntity<List<Map<String, Object>>> getCustomerDashboardAnalytics(
            @RequestParam int year,
            @RequestParam(required = false) Integer month) {
        return ResponseEntity.ok(customBrsService.getCustomerDashboardAnalytics(year, month));
    }
    /**
     * GET /api/v1/brs/reports/credit/transaction-summary : Retrieves a summary of credit sales transactions.
     *
     * @param startDate The start date for the report (format: YYYY-MM-DD).
     * @param endDate   The end date for the report (format: YYYY-MM-DD).
     * @return A ResponseEntity containing a list of credit transaction summaries.
     */
    @GetMapping("/reports/credit/transaction-summary")
    public ResponseEntity<List<Map<String, Object>>> getCreditTransactionSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getCreditTransactionSummary(startDate, endDate));
    }

    /**
     * GET /api/v1/brs/customers/{customerId}/credit-statement : Retrieves a detailed credit statement for a customer.
     */
    @GetMapping("/customers/{customerId}/credit-statement")
    public ResponseEntity<List<Map<String, Object>>> getCustomerCreditStatement(
            @PathVariable Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getCustomerCreditStatement(customerId, startDate, endDate));
    }

    /**
     * GET /api/v1/brs/reports/credit/paid-orders : Retrieves all paid credit orders in a date range.
     */
    @GetMapping("/reports/credit/paid-orders")
    public ResponseEntity<List<Map<String, Object>>> getPaidCreditOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getPaidCreditOrders(startDate, endDate));
    }

    /**
     * GET /api/v1/brs/reports/credit/pending-orders : Retrieves all pending credit orders in a date range.
     */
    @GetMapping("/reports/credit/pending-orders")
    public ResponseEntity<List<Map<String, Object>>> getPendingCreditOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getPendingCreditOrders(startDate, endDate));
    }
    /**
     * GET /api/v1/brs/reports/stock/return-details : Retrieves all pending credit orders in a date range.
     */
    @GetMapping("/reports/stock/return-details")
    public ResponseEntity<List<Map<String, Object>>> getDetailedStockReturnReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getStockReturnReport(startDate, endDate));
    }

    @GetMapping("/customers/search")
    public ResponseEntity<Map<String, Object>> searchPaginateCustomers(
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "15") int pageSize) {
        return ResponseEntity.ok(customBrsService.searchPaginateCustomers(searchTerm, pageNumber, pageSize));
    }

    /**
     * GET /api/v1/brs/dashboard/credit-stats : Retrieves credit dashboard analytics.
     *
     * @param startDate The start date for the report (format: YYYY-MM-DD).
     * @param endDate   The end date for the report (format: YYYY-MM-DD).
     * @return A ResponseEntity containing the structured dashboard data.
     */
    @GetMapping("/dashboard/credit-stats")
    public ResponseEntity<Map<String, Object>> getCreditDashboardStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getCreditDashboardStats(startDate, endDate));
    }


    /**
     * DELETE /api/v1/brs/transactions/stock/{serialNumber} : Deletes a stock transaction and all related data.
     *
     * @param serialNumber The unique serial number of the transaction to delete.
     * @return A ResponseEntity with status 204 (No Content) on successful deletion.
     */
    @DeleteMapping("/transactions/stock/{serialNumber}")
    public ResponseEntity<Void> deleteStockTransaction(@PathVariable String serialNumber) {
        customBrsService.deleteStockTransaction(serialNumber);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content is standard for successful DELETE
    }


    /**
     * GET /api/v1/brs/reports/sales/monthly-analysis : Generates a monthly sales analysis for a given year.
     *
     * @param reportYear The year for the analysis (e.g., 2025).
     * @return A ResponseEntity containing the monthly sales breakdown.
     */
    @GetMapping("/reports/sales/monthly-analysis")
    public ResponseEntity<List<Map<String, Object>>> getMonthlySalesAnalysis(
            @RequestParam int reportYear) {
        return ResponseEntity.ok(customBrsService.getMonthlySalesAnalysis(reportYear));
    }

    /**
     * GET /api/v1/brs/reports/profit/monthly-analysis : Generates a monthly profit analysis for a given year.
     *
     * @param reportYear The year for the analysis (e.g., 2025).
     * @return A ResponseEntity containing the monthly profit breakdown.
     */
    @GetMapping("/reports/profit/monthly-analysis")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyProfitAnalysis(
            @RequestParam int reportYear) {
        return ResponseEntity.ok(customBrsService.getMonthlyProfitAnalysis(reportYear));
    }

    /**
     * GET /api/v1/brs/reports/sales/weekly-analysis : Generates a weekly sales analysis.
     *
     * @param weekStartDate The start date of the week (a Sunday, format YYYY-MM-DD). Optional, defaults to the current week.
     * @return A ResponseEntity containing the daily sales data for the specified week.
     */
    @GetMapping("/reports/sales/weekly-analysis")
    public ResponseEntity<List<Map<String, Object>>> getWeeklySalesAnalysis(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String weekStartDate) {
        return ResponseEntity.ok(customBrsService.getWeeklySalesAnalysis(weekStartDate));
    }


    /**
     * GET /api/v1/brs/reports/stock/summary : Generates a stock report.
     *
     * @param startDate The start date for the report (format: YYYY-MM-DD). Optional.
     * @param endDate   The end date for the report (format: YYYY-MM-DD). Optional.
     * @return A ResponseEntity containing the stock report data.
     */
    @GetMapping("/reports/stock/summary")
    public ResponseEntity<List<Map<String, Object>>> getStockReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getStockReport(startDate, endDate));
    }


    /**
     * GET /api/v1/brs/reports/stock/valuation/monthly : Generates a monthly stock valuation report.
     *
     * @param year    The year for the report (e.g., 2024).
     * @param month   The month for the report (1-12).
     * @param storeId The ID of the store. Can be null for an aggregated view.
     * @return A ResponseEntity containing the daily breakdown and monthly total for stock valuation.
     */
    @GetMapping("/reports/stock/valuation/monthly")
    public ResponseEntity<List<Map<String, Object>>> getStockValuationReportMonthlyData(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) Integer storeId) {
        return ResponseEntity.ok(customBrsService.getStockValuationReportMonthlyData(year, month, storeId));
    }

    /**
     * GET /api/v1/brs/reports/stock/counter-sheet : Generates a counter stock sheet report.
     *
     * @param startDate The start date for the report (format: YYYY-MM-DD).
     * @param endDate   The end date for the report (format: YYYY-MM-DD).
     * @param storeId   The ID of the store for which the report is generated.
     * @return A ResponseEntity containing the detailed stock sheet data for each product.
     */
    @GetMapping("/reports/stock/counter-sheet")
    public ResponseEntity<List<Map<String, Object>>> getCounterStockSheet(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
            @RequestParam Integer storeId) {
        return ResponseEntity.ok(customBrsService.getCounterStockSheet(startDate, endDate, storeId));
    }


    /**
     * GET /api/v1/brs/reports/profitability/by-category : Analyzes profitability for each product category.
     */
    @GetMapping("/reports/profitability/by-category")
    public ResponseEntity<List<Map<String, Object>>> getCategoryProfitability(
            @RequestParam(required = false) Integer storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getCategoryProfitability(storeId, startDate, endDate));
    }

    /**
     * GET /api/v1/brs/reports/stock/velocity-analysis : Analyzes how quickly stock is moving.
     */
    @GetMapping("/reports/stock/velocity-analysis")
    public ResponseEntity<List<Map<String, Object>>> getStockVelocityAnalysis(
            @RequestParam(required = false) Integer storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return ResponseEntity.ok(customBrsService.getStockVelocityAnalysis(storeId, startDate, endDate));
    }

    /**
     * GET /api/v1/brs/reports/stock/demand-forecast : Forecasts future demand for a product.
     */
    @GetMapping("/reports/stock/demand-forecast")
    public ResponseEntity<List<Map<String, Object>>> getDemandForecast(
            @RequestParam Integer productId,
            @RequestParam(required = false) Integer storeId,
            @RequestParam(defaultValue = "6") int forecastMonths) {
        return ResponseEntity.ok(customBrsService.getDemandForecast(productId, storeId, forecastMonths));
    }

    /**
     * GET /api/v1/brs/reports/inventory/abc-analysis : Classifies inventory using ABC analysis.
     */
    @GetMapping("/reports/inventory/abc-analysis")
    public ResponseEntity<List<Map<String, Object>>> getInventoryABCAnalysis(
            @RequestParam(required = false) Integer storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String analysisDate) {
        return ResponseEntity.ok(customBrsService.getInventoryABCAnalysis(storeId, analysisDate));
    }

    /**
     * GET /api/v1/brs/reports/stock/trend-analysis : Analyzes sales and stock trends for a product.
     */
    @GetMapping("/reports/stock/trend-analysis")
    public ResponseEntity<List<Map<String, Object>>> getStockTrendAnalysis(
            @RequestParam Integer productId,
            @RequestParam(required = false) Integer storeId,
            @RequestParam(defaultValue = "12") int analysisPeriodMonths) {
        return ResponseEntity.ok(customBrsService.getStockTrendAnalysis(productId, storeId, analysisPeriodMonths));
    }


    /**
     * GET /api/v1/brs/reports/stock/low-stock : Retrieves a report of all products with low stock levels.
     *
     * @return A ResponseEntity containing a list of low-stock products.
     */
    @GetMapping("/reports/stock/low-stock")
    public ResponseEntity<List<Map<String, Object>>> getLowStockProductsReport() {
        return ResponseEntity.ok(customBrsService.getLowStockProducts());
    }
}