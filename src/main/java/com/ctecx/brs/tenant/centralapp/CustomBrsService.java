package com.ctecx.brs.tenant.centralapp;


import com.ctecx.brs.tenant.exception.EntityNotFoundException;

import com.ctecx.brs.tenant.users.User;
import com.ctecx.brs.tenant.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomBrsService {
    private final CustomBrsRepository customBrsRepository;
    private final SalesAnalyticsFormatter salesAnalyticsFormatter;

    private final SalesReceiptFormatter salesReceiptFormatter;
    private final DashboardAnalyticsFormatter dashboardAnalyticsFormatter;
    private final SalesTransactionSummaryFormatter salesTransactionSummaryFormatter;
    private final ResultSetFormatter resultSetFormatter;
    private final CreditTransactionSummaryFormatter creditTransactionSummaryFormatter;
    private final CustomerCreditStatementFormatter customerCreditStatementFormatter;
    private final PaidCreditOrdersFormatter paidCreditOrdersFormatter;
    private final PendingCreditOrdersFormatter pendingCreditOrdersFormatter;
    private final CreditDashboardStatsFormatter creditDashboardStatsFormatter;
    private final CustomerCreditSummaryFormatter customerCreditSummaryFormatter;
    private final StockReportFormatter stockReportFormatter;

    private final UserRepository userRepository;


    /**
     * Retrieves and formats a summarized credit report for all active customers
     * within a given date range.
     *
     * @param startDate The start date of the report.
     * @param endDate   The end date of the report.
     * @return A list of structured maps, each representing a customer's credit summary.
     */
    public List<Map<String, Object>> getCustomerCreditSummary(String startDate, String endDate) {
        List<Map<String, Object>> rawData = customBrsRepository.getCustomerCreditSummary(startDate, endDate);
        return customerCreditSummaryFormatter.format(rawData);
    }


    /**
     * Deletes a stock transaction and its associated records by its serial number.
     * This operation is transactional.
     *
     * @param serialNumber The serial number of the transaction to be deleted.
     */
    @Transactional
    public void deleteStockTransaction(String serialNumber) {
        customBrsRepository.deleteStockTransaction(serialNumber);
    }


    /**
     * Retrieves and formats the credit dashboard statistics for a given date range.
     *
     * @param startDate The start date of the report.
     * @param endDate   The end date of the report.
     * @return A structured map containing all credit dashboard analytics.
     */
    public Map<String, Object> getCreditDashboardStats(String startDate, String endDate) {
        List<Map<String, Object>> rawData = customBrsRepository.getCreditDashboardStats(startDate, endDate);
        return creditDashboardStatsFormatter.format(rawData);
    }


    /**
     * Searches for customers with pagination. It receives a list where each row contains
     * the total record count and formats it into the final response structure.
     */
    public Map<String, Object> searchPaginateCustomers(String searchTerm, int pageNumber, int pageSize) {
        // 1. Get the raw list from the repository
        List<Map<String, Object>> rawData = customBrsRepository.searchPaginateCustomers(searchTerm, pageNumber, pageSize);

        int totalRecords = 0;
        // 2. If the list is not empty, get the total count from the first row
        if (rawData != null && !rawData.isEmpty()) {
            Object countValue = rawData.get(0).get("TotalCount");
            if (countValue instanceof Number) {
                totalRecords = ((Number) countValue).intValue();
            }
            // Optional: Remove the redundant TotalCount from each row for a cleaner API response
            rawData.forEach(row -> row.remove("TotalCount"));
        }

        // 3. Build and return the final, structured response map
        return Map.of(
                "data", rawData != null ? rawData : Collections.emptyList(),
                "totalRecords", totalRecords
        );
    }

    /**
     * Retrieves and formats pending credit orders for a given date range.
     */
    public List<Map<String, Object>> getPendingCreditOrders(String startDate, String endDate) {
        List<Map<String, Object>> rawData = customBrsRepository.getPendingCreditOrders(startDate, endDate);
        return pendingCreditOrdersFormatter.format(rawData);
    }

    /**
     * Retrieves and formats paid credit orders for a given date range.
     */
    public List<Map<String, Object>> getPaidCreditOrders(String startDate, String endDate) {
        List<Map<String, Object>> rawData = customBrsRepository.getPaidCreditOrders(startDate, endDate);
        return paidCreditOrdersFormatter.format(rawData);
    }

    /**
     * Retrieves and formats a detailed credit statement for a customer.
     */
    public List<Map<String, Object>> getCustomerCreditStatement(Long customerId, String startDate, String endDate) {
        List<Map<String, Object>> rawData = customBrsRepository.getCustomerCreditStatement(customerId, startDate, endDate);
        return customerCreditStatementFormatter.format(rawData);
    }

    /**
     * Retrieves and formats a credit transaction summary report for a given date range.
     *
     * @param startDate The start date of the report.
     * @param endDate   The end date of the report.
     * @return A list of structured maps, each representing a credit transaction.
     */
    public List<Map<String, Object>> getCreditTransactionSummary(String startDate, String endDate) {
        List<Map<String, Object>> rawData = customBrsRepository.getCreditTransactionSummary(startDate, endDate);
        return creditTransactionSummaryFormatter.format(rawData);
    }

    /**
     * Retrieves and formats a sales transaction summary report for a given date range.
     *
     * @param startDate The start date of the report.
     * @param endDate   The end date of the report.
     * @return A list of structured maps, each representing a sales transaction.
     */
    public List<Map<String, Object>> getSalesTransactionSummary(String startDate, String endDate) {
        List<Map<String, Object>> rawData = customBrsRepository.getSalesTransactionSummary(startDate, endDate);
        return salesTransactionSummaryFormatter.format(rawData);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            Optional<User> user = userRepository.findByUserName(userDetails.getUsername());
            if (user.isPresent()) {
                return user.get().getFullName();
            }
        } else if (principal != null) {
            return principal.toString();
        }
        return "system"; // Fallback user
    }




    public Map<String, Object> getSalesAnalyticsReport(String startDate, String endDate) {
        // 1. Get the raw data from the repository
        List<Map<String, Object>> rawData = customBrsRepository.getSalesAnalyticsReport(startDate, endDate);

        // 2. Use the formatter to parse the raw data and return a structured map
        return salesAnalyticsFormatter.formatSalesAnalytics(rawData);
    }

    public List<Map<String, Object>> SearchPaginateChartOfAccounts(String searchTerm, int pageNumber) {
        return customBrsRepository.SearchPaginateChartOfAccounts(searchTerm, pageNumber);
    }

    public List<Map<String, Object>> SearchPaginateProducts(String searchTerm, int pageNumber) {
        return customBrsRepository.SearchPaginateProducts(searchTerm, pageNumber);
    }

    public List<Map<String, Object>> searchProductsWithPositiveStock(String searchTerm, int pageNumber) {
        return customBrsRepository.searchProductsWithPositiveStock(searchTerm, pageNumber);
    }

    public List<Map<String, Object>> searchAllProducts(String searchTerm, int pageNumber) {
        return customBrsRepository.searchAllProducts(searchTerm, pageNumber);
    }

    public String generateSerialNumber(String billType) {
        List<Map<String, Object>> result = customBrsRepository.generateSerialNumber(billType);
        if (result != null && !result.isEmpty()) {
            return (String) result.get(0).get("generated_serial_number");
        }
        throw new RuntimeException("Failed to generate serial number for bill type: " + billType);
    }

    // New service methods

    public List<Map<String, Object>> searchProductsByStockLevelAndCategory(String searchKey, String categoryIds, int pageNumber, int pageSize) {
        return customBrsRepository.searchProductsByStockLevelAndCategory(searchKey, categoryIds, pageNumber, pageSize);
    }

    public List<Map<String, Object>> searchProductsByStockLevel(String searchKey, int pageNumber, int pageSize) {
        return customBrsRepository.searchProductsByStockLevel(searchKey, pageNumber, pageSize);
    }

    public List<Map<String, Object>> searchProductsByStockLevelAndLocation(String searchKey, String locationIds, String categoryIds, int pageNumber, int pageSize) {
        return customBrsRepository.searchProductsByStockLevelAndLocation(searchKey, locationIds, categoryIds, pageNumber, pageSize);
    }

    public List<Map<String, Object>> searchProductsByStockLevelAndExpiry(String searchKey, String locationIds, String categoryIds, int pageNumber, int pageSize) {
        return customBrsRepository.searchProductsByStockLevelAndExpiry(searchKey, locationIds, categoryIds, pageNumber, pageSize);
    }

    public List<Map<String, Object>> searchProductsByStockLevelAndSubmodule(String searchKey, String submodules, int pageNumber, int pageSize) {
        return customBrsRepository.searchProductsByStockLevelAndSubmodule(searchKey, submodules, pageNumber, pageSize);
    }

    public List<Map<String, Object>> getProductStockByStorePaginated(String searchTerm, Long storeId, int pageNumber, int pageSize) {
        return customBrsRepository.getProductStockByStorePaginated(searchTerm, storeId, pageNumber, pageSize);
    }
    
    public List<Map<String, Object>> getDailyStockMovementReport(String startDate, String endDate, int branchId) {
        return customBrsRepository.getDailyStockMovementReport(startDate, endDate, branchId);
    }

    public List<Map<String, Object>> getMonthlySalesReport(int year, int month, int branchId) {
        return customBrsRepository.getMonthlySalesReport(year, month, branchId);
    }

    public List<Map<String, Object>> getInventoryProducts(String searchKey, int pageNumber, int pageSize) {
        return customBrsRepository.getInventoryProducts(searchKey, pageNumber, pageSize);
    }
    public List<Map<String, Object>> getInventoryValuation(String searchKey, int pageNumber, int pageSize) {
        return customBrsRepository.getInventoryValuation(searchKey, pageNumber, pageSize);
    }
    public List<Map<String, Object>> getSalesReport(String startDate, String endDate) {
        return customBrsRepository.getSalesReport(startDate, endDate);
    }

    public List<Map<String, Object>> getPurchaseReport(String startDate, String endDate) {
        return customBrsRepository.getPurchaseReport(startDate, endDate);
    }
    public List<Map<String, Object>> getStockAddReport(String startDate, String endDate) {
        return customBrsRepository.getStockAddReport(startDate, endDate);
    }

    public List<Map<String, Object>> getStockSubtractReport(String startDate, String endDate) {
        return customBrsRepository.getStockSubtractReport(startDate, endDate);
    }
    public List<Map<String, Object>> getExpiryAnalysis(Integer warnDays) {
        return customBrsRepository.getExpiryAnalysis(warnDays);
    }
    public List<Map<String, Object>> getSalesTransactionsReport(String startDate, String endDate) {
        return customBrsRepository.getSalesTransactionsReport(startDate, endDate);
    }

    public List<Map<String, Object>> getPaymentModeAnalysisByUser(String startDate, String endDate) {
        return customBrsRepository.getPaymentModeAnalysisByUser(startDate, endDate);
    }

    public List<Map<String, Object>> getSalesProfitAnalysis(String startDate, String endDate, String groupByOption) {
        return customBrsRepository.getSalesProfitAnalysis(startDate, endDate, groupByOption);
    }

    public List<Map<String, Object>> getMonthlySalesBreakdown(String startDate, String endDate) {
        return customBrsRepository.getMonthlySalesBreakdown(startDate, endDate);
    }

    public List<Map<String, Object>> getTopSellingProducts(String startDate, String endDate, Integer limitCount) {
        return customBrsRepository.getTopSellingProducts(startDate, endDate, limitCount);
    }

    public List<Map<String, Object>> getDayOfWeekSalesAnalysis(String startDate, String endDate) {
        return customBrsRepository.getDayOfWeekSalesAnalysis(startDate, endDate);
    }
    public Integer getProductStockByCode(String productCode) {
        // Call the repository method and throw a standard exception if the product isn't found.
        // This is a clean way to handle the "not found" case at the business logic level.
        return customBrsRepository.getProductStockByCode(productCode)
                .orElseThrow(() -> new EntityNotFoundException("Product with code '" + productCode + "' not found."));
    }

    /**
     * Retrieves and formats a single sales receipt by its serial number.
     *
     * @param serialNumber The serial number of the receipt to fetch.
     * @return A structured map representing the sales receipt.
     */
    public Map<String, Object> getSalesReceipt(String serialNumber) {
        List<Map<String, Object>> rawData = customBrsRepository.getSalesReceipt(serialNumber);
        return salesReceiptFormatter.formatSalesReceipt(rawData);
    }


    /**
     * Retrieves and formats the main dashboard analytics data for a given year and month.
     *
     * @param year  The year for the dashboard data.
     * @param month The month for the dashboard data (1-12).
     * @return A structured map containing all dashboard analytics.
     */
    public Map<String, Object> getDashboardAnalytics(int year, int month) {
        List<Map<String, Object>> rawData = customBrsRepository.getDashboardAnalytics(year, month);
        return dashboardAnalyticsFormatter.format(rawData);
    }


    /**
     * Searches for suppliers with pagination. Formats the response to include total records.
     */
    public Map<String, Object> searchPaginateSuppliers(String searchTerm, int pageNumber, int pageSize) {
        List<Map<String, Object>> rawData = customBrsRepository.searchPaginateSuppliers(searchTerm, pageNumber, pageSize);

        int totalRecords = 0;
        if (rawData != null && !rawData.isEmpty()) {
            // The SP includes 'TotalRecords' in every row; get it from the first one.
            Object countValue = rawData.get(0).get("TotalRecords");
            if (countValue instanceof Number) {
                totalRecords = ((Number) countValue).intValue();
            }
        }

        // Remove the redundant TotalRecords column from each item for a cleaner response
        if (rawData != null) {
            rawData.forEach(row -> row.remove("TotalRecords"));
        }

        return Map.of("data", rawData != null ? rawData : Collections.emptyList(), "totalRecords", totalRecords);
    }

    /**
     * Retrieves supplier transactions and a summary for a given period.
     */
    public Map<String, Object> getSupplierTransactions(Long supplierId, String startDate, String endDate) {
        Map<String, Object> rawData = customBrsRepository.getSupplierTransactions(supplierId, startDate, endDate);
        return resultSetFormatter.formatTransactionWithSummaryResponse(rawData);
    }

    /**
     * Retrieves supplier dashboard analytics.
     */
    public List<Map<String, Object>> getSupplierDashboardAnalytics(int year, Integer month) {
        return customBrsRepository.getSupplierDashboardAnalytics(year, month);
    }

    // --- New Customer Service Methods ---



    /**
     * Retrieves transactions for a specific customer for a given period.
     */
    public List<Map<String, Object>> getCustomerTransactions(Long customerId, int year, Integer month) {
        return customBrsRepository.getCustomerTransactions(customerId, year, month);
    }

    /**
     * Retrieves customer dashboard analytics.
     */
    public List<Map<String, Object>> getCustomerDashboardAnalytics(int year, Integer month) {
        return customBrsRepository.getCustomerDashboardAnalytics(year, month);
    }

    public List<Map<String, Object>> getStockReturnReport(String startDate, String endDate) {
        return customBrsRepository.getStockReturnReport(startDate, endDate);
    }


    public List<Map<String, Object>> getMonthlySalesAnalysis(int reportYear) {
        return customBrsRepository.getMonthlySalesAnalysis(reportYear);
    }

    public List<Map<String, Object>> getMonthlyProfitAnalysis(int reportYear) {
        return customBrsRepository.getMonthlyProfitAnalysis(reportYear);
    }

    public List<Map<String, Object>> getWeeklySalesAnalysis(String weekStartDate) {
        return customBrsRepository.getWeeklySalesAnalysis(weekStartDate);
    }

    /**
     * Retrieves and formats a stock report for a given date range.
     *
     * @param startDate The start date for the report. Can be null.
     * @param endDate   The end date for the report. Can be null.
     * @return A list of structured maps representing the stock report.
     */
    public List<Map<String, Object>> getStockReport(String startDate, String endDate) {
        List<Map<String, Object>> rawData = customBrsRepository.getStockReport(startDate, endDate);
        return stockReportFormatter.format(rawData);
    }



    /**
     * Retrieves a monthly stock valuation report for a specific year, month, and store.
     * The stored procedure already formats the data, so it's returned directly.
     *
     * @param year    The year for the report.
     * @param month   The month for the report (1-12).
     * @param storeId The ID of the store. Can be null for all stores.
     * @return A list of daily stock valuation data with a monthly total.
     */
    public List<Map<String, Object>> getStockValuationReportMonthlyData(int year, int month, Integer storeId) {
        return customBrsRepository.getStockValuationReportMonthlyData(year, month, storeId);
    }

    /**
     * Retrieves a counter stock sheet for a given date range and store.
     * The stored procedure handles all calculations and formatting, including the summary row.
     *
     * @param startDate The start date for the report (YYYY-MM-DD).
     * @param endDate   The end date for the report (YYYY-MM-DD).
     * @param storeId   The ID of the store.
     * @return A list of product stock sheet data with a final total row.
     */
    public List<Map<String, Object>> getCounterStockSheet(String startDate, String endDate, Integer storeId) {
        return customBrsRepository.getCounterStockSheet(startDate, endDate, storeId);
    }

    /**
     * Retrieves a category profitability report.
     */
    public List<Map<String, Object>> getCategoryProfitability(Integer storeId, String startDate, String endDate) {
        return customBrsRepository.getCategoryProfitability(storeId, startDate, endDate);
    }

    /**
     * Retrieves a stock velocity analysis report.
     */
    public List<Map<String, Object>> getStockVelocityAnalysis(Integer storeId, String startDate, String endDate) {
        return customBrsRepository.getStockVelocityAnalysis(storeId, startDate, endDate);
    }

    /**
     * Retrieves a demand forecast for a specific product.
     */
    public List<Map<String, Object>> getDemandForecast(Integer productId, Integer storeId, int forecastMonths) {
        return customBrsRepository.getDemandForecast(productId, storeId, forecastMonths);
    }

    /**
     * Retrieves an inventory ABC analysis report.
     */
    public List<Map<String, Object>> getInventoryABCAnalysis(Integer storeId, String analysisDate) {
        return customBrsRepository.getInventoryABCAnalysis(storeId, analysisDate);
    }

    /**
     * Retrieves a stock trend analysis for a specific product.
     */
    public List<Map<String, Object>> getStockTrendAnalysis(Integer productId, Integer storeId, int analysisPeriodMonths) {
        return customBrsRepository.getStockTrendAnalysis(productId, storeId, analysisPeriodMonths);
    }


    /**
     * Retrieves a list of all products that are low on stock.
     *
     * @return A list of maps, where each map represents a product with low stock.
     */
    public List<Map<String, Object>> getLowStockProducts() {
        return customBrsRepository.getLowStockProducts();
    }
}