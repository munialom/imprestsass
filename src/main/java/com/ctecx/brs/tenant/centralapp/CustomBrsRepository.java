package com.ctecx.brs.tenant.centralapp;

import com.ctecx.brs.tenant.users.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomBrsRepository {



    /**
     * Generates a summarized credit report for all active, non-default customers.
     * The result is a single JSON array string containing detailed summaries.
     * Calls the `GetCustomerCreditSummary` stored procedure.
     *
     * @param startDate The start date of the reporting period (YYYY-MM-DD). Can be null.
     * @param endDate   The end date of the reporting period (YYYY-MM-DD). Can be null.
     * @return A list containing a single map with the key 'customer_credit_summary'.
     */
    List<Map<String, Object>> getCustomerCreditSummary(String startDate, String endDate);

    /**
     * Deletes a stock transaction and all its related sales and payment details.
     * Calls the `delete_stock_transaction` stored procedure.
     *
     * @param serialNumber The unique serial number of the transaction to delete.
     */
    void deleteStockTransaction(String serialNumber);

    /**
     * Retrieves credit dashboard analytics as a single JSON string.
     * Calls the `GetCreditDashboardStats` stored procedure.
     *
     * @param startDate The start date of the reporting period (YYYY-MM-DD).
     * @param endDate   The end date of the reporting period (YYYY-MM-DD).
     * @return A list containing a single map with the key 'dashboard_json'.
     */
    List<Map<String, Object>> getCreditDashboardStats(String startDate, String endDate);

    /**
     * Searches and paginates customers based on a search term.
     * Calls the `SearchPaginateCustomers` stored procedure which returns a single result set.
     */
    List<Map<String, Object>> searchPaginateCustomers(String searchTerm, int pageNumber, int pageSize);


    /**
     * Retrieves all pending credit orders within a date range.
     * Calls the `GetPendingCreditOrders` stored procedure.
     */
    List<Map<String, Object>> getPendingCreditOrders(String startDate, String endDate);

    /**
     * Retrieves all paid credit orders within a date range.
     * Calls the `GetPaidCreditOrders` stored procedure.
     */
    List<Map<String, Object>> getPaidCreditOrders(String startDate, String endDate);

    /**
     * Retrieves a detailed credit statement for a specific customer.
     * Calls the `GetCustomerCreditStatement` stored procedure.
     */
    List<Map<String, Object>> getCustomerCreditStatement(Long customerId, String startDate, String endDate);
    /**
     * Generates a detailed summary of CREDIT customer transactions for a given date range.
     * Calls the `GetCreditTransactionSummary` stored procedure.
     *
     * @param startDate The start date of the reporting period (YYYY-MM-DD).
     * @param endDate   The end date of the reporting period (YYYY-MM-DD).
     * @return A list of summarized credit transaction data.
     */
    List<Map<String, Object>> getCreditTransactionSummary(String startDate, String endDate);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserById(Long userId);
    List<Map<String, Object>> SearchPaginateChartOfAccounts(String searchTerm, int pageNumber);

    List<Map<String, Object>> SearchPaginateProducts(String searchTerm, int pageNumber);
    List<Map<String, Object>> searchProductsWithPositiveStock(String searchTerm, int pageNumber);
    List<Map<String, Object>> searchAllProducts(String searchTerm, int pageNumber);
    List<Map<String, Object>> generateSerialNumber(String billType);

    // New methods to call the added stored procedures

    /**
     * Searches products by stock level and a comma-separated list of category IDs.
     * Calls the `SearchProductsByStockLevelAndCategory` stored procedure.
     */
    List<Map<String, Object>> searchProductsByStockLevelAndCategory(String searchKey, String categoryIds, int pageNumber, int pageSize);

    /**
     * Searches products by stock level based on a search key.
     * Calls the `SearchProductsByStockLevel` stored procedure.
     */
    List<Map<String, Object>> searchProductsByStockLevel(String searchKey, int pageNumber, int pageSize);

    /**
     * Searches products by stock level, location, and category.
     * Calls the `SearchProductsByStockLevelAndLocation` stored procedure.
     */
    List<Map<String, Object>> searchProductsByStockLevelAndLocation(String searchKey, String locationIds, String categoryIds, int pageNumber, int pageSize);

    /**
     * Searches products by stock level and expiry details.
     * Calls the `SearchProductsByStockLevelAndExpiry` stored procedure.
     */
    List<Map<String, Object>> searchProductsByStockLevelAndExpiry(String searchKey, String locationIds, String categoryIds, int pageNumber, int pageSize);

    /**
     * Searches products by stock level and transaction submodule.
     * Calls the `SearchProductsByStockLevelAndSubmodule` stored procedure.
     */
    List<Map<String, Object>> searchProductsByStockLevelAndSubmodule(String searchKey, String submodules, int pageNumber, int pageSize);

    /**
     * Retrieves paginated product stock details for a specific or all stores.
     * Calls the `GetProductStockByStorePaginated` stored procedure.
     */
    List<Map<String, Object>> getProductStockByStorePaginated(String searchTerm, Long storeId, int pageNumber, int pageSize);

    /**
     * Generates a daily stock movement report for a given date range and branch.
     * Calls the `DailyStockMovement` stored procedure.
     */
    List<Map<String, Object>> getDailyStockMovementReport(String startDate, String endDate, int branchId);

    /**
     * Generates a monthly sales report for a given year, month, and branch.
     * Calls the `GetMonthlySalesReport` stored procedure.
     */
    List<Map<String, Object>> getMonthlySalesReport(int year, int month, int branchId);

    /**
     * Retrieves paginated inventory product details based on a search key.
     * Calls the `GetInventoryProducts` stored procedure.
     */
    List<Map<String, Object>> getInventoryProducts(String searchKey, int pageNumber, int pageSize);

    /**
     * Retrieves a paginated inventory valuation report based on a search key.
     * Includes a summary total row.
     * Calls the `GetInventoryValuation` stored procedure.
     */
    List<Map<String, Object>> getInventoryValuation(String searchKey, int pageNumber, int pageSize);
    /**
     * Generates a detailed sales report for a given date range.
     * Calls the `GetSalesReport` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return a list of detailed sales transaction data.
     */
    List<Map<String, Object>> getSalesReport(String startDate, String endDate);


    /**
     * Generates a detailed purchase report for a given date range.
     * Calls the `GetPurchaseReport` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return a list of detailed purchase transaction data.
     */
    List<Map<String, Object>> getPurchaseReport(String startDate, String endDate);

    /**
     * Generates a report for stock added manually within a given date range.
     * Calls the `GetStockAddReport` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return a list of stock addition data.
     */
    List<Map<String, Object>> getStockAddReport(String startDate, String endDate);

    /**
     * Generates a report for stock subtracted manually within a given date range.
     * Calls the `GetStockSubtractReport` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return a list of stock subtraction data.
     */
    List<Map<String, Object>> getStockSubtractReport(String startDate, String endDate);


    /**
     * Generates a detailed expiry analysis report for all product batches.
     * Calls the `GetExpiryAnalysis` stored procedure.
     *
     * @param warnDays The number of days before expiry to trigger a 'Warning' status.
     *                 If null, defaults to 90 days in the stored procedure.
     * @return a list of batch expiry data, including detailed summaries.
     */
    List<Map<String, Object>> getExpiryAnalysis(Integer warnDays);

    /**
     * Generates a summarized sales transactions report for a given date range.
     * Calls the `GetSalesTransactionsReport` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return a list of summarized sales transaction data.
     */
    List<Map<String, Object>> getSalesTransactionsReport(String startDate, String endDate);



    /**
     * Generates a payment mode analysis report grouped by user for a given date range.
     * Calls the `PaymentModeAnalysisByUser` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return a list of payment mode analysis data grouped by user.
     */
    List<Map<String, Object>> getPaymentModeAnalysisByUser(String startDate, String endDate);

    /**
     * Generates a sales profit analysis report for a given date range, grouped by a specified time period.
     * Calls the `GetSalesProfitAnalysis` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @param groupByOption the time period to group by ('daily', 'weekly', 'monthly').
     * @return a list of sales profit analysis data.
     */
    List<Map<String, Object>> getSalesProfitAnalysis(String startDate, String endDate, String groupByOption);


    /**
     * Generates a monthly sales breakdown report, showing daily performance and deltas.
     * Calls the `GetMonthlySalesBreakdown` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return a list of daily sales breakdown data.
     */
    List<Map<String, Object>> getMonthlySalesBreakdown(String startDate, String endDate);

    /**
     * Generates a report of the top-selling products within a date range.
     * Calls the `GetTopSellingProducts` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @param limitCount the number of top products to return. Defaults to 15 if null or 0.
     * @return a ranked list of top-selling products and a grand total summary.
     */
    List<Map<String, Object>> getTopSellingProducts(String startDate, String endDate, Integer limitCount);

    /**
     * Generates a sales analysis report grouped by day of the week.
     * Calls the `GetDayOfWeekSalesAnalysis` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return a list of sales analysis data for each day of the week.
     */
    List<Map<String, Object>> getDayOfWeekSalesAnalysis(String startDate, String endDate);


    /**
     * Generates a comprehensive sales analytics report as a single JSON string.
     * Calls the `GetSalesAnalyticsReport` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return A list containing a single map with the key 'analytics_json'.
     */
    List<Map<String, Object>> getSalesAnalyticsReport(String startDate, String endDate);

    // Updated method (removed modifiedBy)
    // Fixed: 3 parameters for id, categoryName, active
    // Updated method to include modifiedBy
    int updateCategory(Integer id, String categoryName, boolean active, String modifiedBy);

    int updateBrand(Long id, String brandName, boolean active, String modifiedBy);

    // New method for updating UOM
    int updateUom(Long id, String unitName, boolean active, String modifiedBy);
    // New method for updating product
    int updateProduct(Long id, String name, String code, double price, double cost, int minimumStock,
                      Integer categoryId, Long brandId, Long uomId, Long productTypeId, boolean active, String modifiedBy);

    /**
     * Retrieves the current calculated stock for a single product by its code.
     * Calls the `get_product_stock_by_code` stored function.
     *
     * @param productCode The unique code of the product.
     * @return An Optional containing the stock count if the product is found, otherwise an empty Optional.
     */
    Optional<Integer> getProductStockByCode(String productCode);


    /**
     * Retrieves a complete sales receipt by its serial number.
     * Calls the `GetSalesReceipt` stored procedure.
     *
     * @param serialNumber The unique serial number of the sales transaction.
     * @return A list containing a single map with the raw receipt data.
     */
    List<Map<String, Object>> getSalesReceipt(String serialNumber);

    /**
     * Generates a summary report of sales transactions for a given date range.
     * Calls the `GetSalesTransactionSummary` stored procedure.
     *
     * @param startDate The start date of the reporting period (YYYY-MM-DD).
     * @param endDate   The end date of the reporting period (YYYY-MM-DD).
     * @return A list of summarized sales transaction data.
     */
    List<Map<String, Object>> getSalesTransactionSummary(String startDate, String endDate);

    /**
     * Retrieves dashboard analytics data for a given year and month.
     * Calls the `GetDashboardAnalytics` stored procedure.
     *
     * @param year  The year for the analytics report.
     * @param month The month for the analytics report (1-12).
     * @return A list containing a single map with the key 'dashboard_json'.
     */
    List<Map<String, Object>> getDashboardAnalytics(int year, int month);


    /**
     * Searches and paginates suppliers based on a search term.
     * Calls the `SearchPaginateSuppliers` stored procedure.
     */
    List<Map<String, Object>> searchPaginateSuppliers(String searchTerm, int pageNumber, int pageSize);

    /**
     * Retrieves transactions for a specific supplier within a date range.
     * Returns multiple result sets (transactions and summary).
     * Calls the `GetSupplierTransactions` stored procedure.
     */
    Map<String, Object> getSupplierTransactions(Long supplierId, String startDate, String endDate);

    /**
     * Retrieves dashboard analytics for suppliers for a given period.
     * Calls the `GetSupplierDashboardAnalytics` stored procedure.
     */
    List<Map<String, Object>> getSupplierDashboardAnalytics(int year, Integer month);



    /**
     * Retrieves transactions for a specific customer for a given period.
     * Calls the `GetCustomerTransactions` stored procedure.
     */
    List<Map<String, Object>> getCustomerTransactions(Long customerId, int year, Integer month);

    /**
     * Retrieves dashboard analytics for customers for a given period.
     * Calls the `GetCustomerDashboardAnalytics` stored procedure.
     */
    List<Map<String, Object>> getCustomerDashboardAnalytics(int year, Integer month);


    /**
     * Generates a report for stock returned by customers within a given date range.
     * Calls the `GetStockReturnReport` stored procedure.
     *
     * @param startDate the start date of the reporting period (YYYY-MM-DD).
     * @param endDate the end date of the reporting period (YYYY-MM-DD).
     * @return a list of stock return data with a total summary row.
     */
    List<Map<String, Object>> getStockReturnReport(String startDate, String endDate);



    /**
     * Generates a monthly sales analysis report for a specific year, including month-over-month changes.
     * Calls the `GetMonthlySalesAnalysis` stored procedure.
     *
     * @param reportYear The year for the analysis (e.g., 2025).
     * @return A list of maps, where each map represents a month's sales data.
     */
    List<Map<String, Object>> getMonthlySalesAnalysis(int reportYear);

    /**
     * Generates a monthly profit analysis report for a specific year, including month-over-month changes.
     * Calls the `GetMonthlyProfitAnalysis` stored procedure.
     *
     * @param reportYear The year for the analysis (e.g., 2025).
     * @return A list of maps, where each map represents a month's profit data.
     */
    List<Map<String, Object>> getMonthlyProfitAnalysis(int reportYear);

    /**
     * Generates a weekly sales analysis report for a specific week.
     * If no start date is provided, it defaults to the current week.
     * Calls the `GetWeeklySalesAnalysis` stored procedure.
     *
     * @param weekStartDate The start date of the week (a Sunday). Can be null.
     * @return A list of maps, where each map represents a day's sales data for the week.
     */
    List<Map<String, Object>> getWeeklySalesAnalysis(String weekStartDate);



    /**
     * Generates a stock report with transaction summaries for a given date range.
     * Calls the `GetStockReport` stored procedure.
     *
     * @param startDate The start date of the reporting period (YYYY-MM-DD). Can be null.
     * @param endDate   The end date of the reporting period (YYYY-MM-DD). Can be null.
     * @return A list of stock data, including a final summary row.
     */
    List<Map<String, Object>> getStockReport(String startDate, String endDate);



    /**
     * Generates a monthly stock valuation report, detailing daily stock movements and values.
     * Calls the `GetStockValuationReportMonthlyData` stored procedure.
     *
     * @param year    The year for the report.
     * @param month   The month for the report (1-12).
     * @param storeId The ID of the store to filter by. Can be null for all stores.
     * @return A list of daily stock valuation data, including a final monthly total row.
     */
    List<Map<String, Object>> getStockValuationReportMonthlyData(int year, int month, Integer storeId);

    /**
     * Generates a counter stock sheet report for a given date range and store.
     * Details opening stock, transactions during the period, and closing stock for each product.
     * Calls the `GetCounterStockSheet` stored procedure.
     *
     * @param startDate The start date of the reporting period (YYYY-MM-DD).
     * @param endDate   The end date of the reporting period (YYYY-MM-DD).
     * @param storeId   The ID of the store to filter by.
     * @return A list of product stock sheet data, including a final total summary row.
     */
    List<Map<String, Object>> getCounterStockSheet(String startDate, String endDate, Integer storeId);


    /**
     * Analyzes the profitability of each product category within a given date range.
     * Calls the `GetCategoryProfitability` stored procedure.
     *
     * @param storeId   The ID of the store to filter by. Can be null for all stores.
     * @param startDate The start date of the reporting period.
     * @param endDate   The end date of the reporting period.
     * @return A list of categories ranked by profitability.
     */
    List<Map<String, Object>> getCategoryProfitability(Integer storeId, String startDate, String endDate);

    /**
     * Analyzes the movement speed (velocity) of stock for each product.
     * Calls the `GetStockVelocityAnalysis` stored procedure.
     *
     * @param storeId   The ID of the store to filter by. Can be null for all stores.
     * @param startDate The start date for the analysis period.
     * @param endDate   The end date for the analysis period.
     * @return A list of products with their stock turnover ratios and velocity categories.
     */
    List<Map<String, Object>> getStockVelocityAnalysis(Integer storeId, String startDate, String endDate);

    /**
     * Forecasts future demand for a specific product based on historical sales data.
     * Calls the `GetDemandForecast` stored procedure.
     *
     * @param productId      The ID of the product to forecast.
     * @param storeId        The ID of the store. Can be null for all stores.
     * @param forecastMonths The number of future months to forecast.
     * @return A list of forecasted sales data for the specified number of future months.
     */
    List<Map<String, Object>> getDemandForecast(Integer productId, Integer storeId, int forecastMonths);

    /**
     * Performs an ABC analysis on the inventory to classify products based on their value.
     * Calls the `GetInventoryABCAnalysis` stored procedure.
     *
     * @param storeId      The ID of the store. Can be null for all stores.
     * @param analysisDate The date for which the inventory value is calculated.
     * @return A list of products classified into 'A', 'B', or 'C' categories.
     */
    List<Map<String, Object>> getInventoryABCAnalysis(Integer storeId, String analysisDate);

    /**
     * Analyzes the sales and stock trends for a specific product over a period of months.
     * Calls the `GetStockTrendAnalysis` stored procedure.
     *
     * @param productId            The ID of the product to analyze.
     * @param storeId              The ID of the store. Can be null for all stores.
     * @param analysisPeriodMonths The number of past months to include in the trend analysis.
     * @return A list of monthly trend data for the specified product.
     */
    List<Map<String, Object>> getStockTrendAnalysis(Integer productId, Integer storeId, int analysisPeriodMonths);


    /**
     * Retrieves all products whose available stock is less than or equal to the minimum stock level.
     * Calls the `GetLowStockProducts` stored procedure.
     *
     * @return A list of products that are low on stock.
     */
    List<Map<String, Object>> getLowStockProducts();



}