package com.ctecx.brs.tenant.centralapp;


import com.ctecx.brs.tenant.config.TenantJdbcTemplateConfig;

import com.ctecx.brs.tenant.mappers.UserMapper;
import com.ctecx.brs.tenant.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

@Repository
@RequiredArgsConstructor
public class CustomBrsRepositoryImpl implements CustomBrsRepository {
    private final TenantJdbcTemplateConfig tenantJdbcTemplateConfig;
    private final UserMapper userMapper;


    private static final int PAGE_SIZE = 10; // Default configurable page size

    private JdbcTemplate getJdbcTemplate() {
        return tenantJdbcTemplateConfig.getTenantJdbcTemplate();
    }


    @Override
    public List<Map<String, Object>> getCustomerCreditSummary(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetCustomerCreditSummary(?, ?)", startDate, endDate);
    }


    @Override
    public void deleteStockTransaction(String serialNumber) {
        // Use `update` for stored procedures that perform DML (INSERT, UPDATE, DELETE) and don't return a result set.
        getJdbcTemplate().update("CALL delete_stock_transaction(?)", serialNumber);
    }

    @Override
    public List<Map<String, Object>> getCreditDashboardStats(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetCreditDashboardStats(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> searchPaginateCustomers(String searchTerm, int pageNumber, int pageSize) {
        // Use the standard queryForList, as requested
        return getJdbcTemplate().queryForList("CALL SearchPaginateCustomers(?, ?, ?)",
                searchTerm, pageNumber, pageSize);
    }

    @Override
    public List<Map<String, Object>> getPendingCreditOrders(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetPendingCreditOrders(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getPaidCreditOrders(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetPaidCreditOrders(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getCustomerCreditStatement(Long customerId, String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetCustomerCreditStatement(?, ?, ?)", customerId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getCreditTransactionSummary(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetCreditTransactionSummary(?, ?)", startDate, endDate);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        try {
            var result = getJdbcTemplate().queryForMap(
                    "CALL GetUserByUsername(?)",
                    username
            );
            return userMapper.mapToUser(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        try {
            var result = getJdbcTemplate().queryForMap(
                    "CALL GetUserById(?)",
                    userId
            );
            return userMapper.mapToUser(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Map<String, Object>> SearchPaginateChartOfAccounts(String searchTerm, int pageNumber) {
        return getJdbcTemplate().queryForList("CALL SearchPaginateChartOfAccounts(?, ?)", searchTerm, pageNumber);
    }



    @Override
    public List<Map<String, Object>> SearchPaginateProducts(String searchTerm, int pageNumber) {
        return getJdbcTemplate().queryForList("CALL SearchPaginateProducts(?, ?)", searchTerm, pageNumber);
    }

    @Override
    public List<Map<String, Object>> searchProductsWithPositiveStock(String searchTerm, int pageNumber) {
        int offset = (pageNumber - 1) * PAGE_SIZE;
        return getJdbcTemplate().queryForList(
                "CALL search_products_with_positive_stock(?, ?, ?)",
                searchTerm, PAGE_SIZE, offset
        );
    }

    @Override
    public List<Map<String, Object>> searchAllProducts(String searchTerm, int pageNumber) {
        int offset = (pageNumber - 1) * PAGE_SIZE;
        return getJdbcTemplate().queryForList(
                "CALL search_products_with_positive_stock_all(?, ?, ?)",
                searchTerm, PAGE_SIZE, offset
        );
    }

    @Override
    public List<Map<String, Object>> generateSerialNumber(String billType) {
        return getJdbcTemplate().queryForList(
                "CALL GenerateUniqueSerialNumber(?)",
                billType
        );
    }
    
    // Implementation of new methods

    @Override
    public List<Map<String, Object>> searchProductsByStockLevelAndCategory(String searchKey, String categoryIds, int pageNumber, int pageSize) {
        return getJdbcTemplate().queryForList("CALL SearchProductsByStockLevelAndCategory(?, ?, ?, ?)", searchKey, categoryIds, pageNumber, pageSize);
    }

    @Override
    public List<Map<String, Object>> searchProductsByStockLevel(String searchKey, int pageNumber, int pageSize) {
        return getJdbcTemplate().queryForList("CALL SearchProductsByStockLevel(?, ?, ?)", searchKey, pageNumber, pageSize);
    }

    @Override
    public List<Map<String, Object>> searchProductsByStockLevelAndLocation(String searchKey, String locationIds, String categoryIds, int pageNumber, int pageSize) {
        return getJdbcTemplate().queryForList("CALL SearchProductsByStockLevelAndLocation(?, ?, ?, ?, ?)", searchKey, locationIds, categoryIds, pageNumber, pageSize);
    }

    @Override
    public List<Map<String, Object>> searchProductsByStockLevelAndExpiry(String searchKey, String locationIds, String categoryIds, int pageNumber, int pageSize) {
        return getJdbcTemplate().queryForList("CALL SearchProductsByStockLevelAndExpiry(?, ?, ?, ?, ?)", searchKey, locationIds, categoryIds, pageNumber, pageSize);
    }

    @Override
    public List<Map<String, Object>> searchProductsByStockLevelAndSubmodule(String searchKey, String submodules, int pageNumber, int pageSize) {
        return getJdbcTemplate().queryForList("CALL SearchProductsByStockLevelAndSubmodule(?, ?, ?, ?)", searchKey, submodules, pageNumber, pageSize);
    }

    @Override
    public List<Map<String, Object>> getProductStockByStorePaginated(String searchTerm, Long storeId, int pageNumber, int pageSize) {
        return getJdbcTemplate().queryForList("CALL GetProductStockByStorePaginated(?, ?, ?, ?)", searchTerm, storeId, pageNumber, pageSize);
    }

    @Override
    public List<Map<String, Object>> getDailyStockMovementReport(String startDate, String endDate, int branchId) {
        return getJdbcTemplate().queryForList("CALL DailyStockMovement(?, ?, ?)", startDate, endDate, branchId);
    }

    @Override
    public List<Map<String, Object>> getMonthlySalesReport(int year, int month, int branchId) {
        return getJdbcTemplate().queryForList("CALL GetMonthlySalesReport(?, ?, ?)", year, month, branchId);
    }
    @Override
    public List<Map<String, Object>> getInventoryProducts(String searchKey, int pageNumber, int pageSize) {
        return getJdbcTemplate().queryForList("CALL GetInventoryProducts(?, ?, ?)", searchKey, pageNumber, pageSize);
    }
    @Override
    public List<Map<String, Object>> getInventoryValuation(String searchKey, int pageNumber, int pageSize) {
        return getJdbcTemplate().queryForList("CALL GetInventoryValuation(?, ?, ?)", searchKey, pageNumber, pageSize);
    }

    @Override
    public List<Map<String, Object>> getSalesReport(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetSalesReport(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getPurchaseReport(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetPurchaseReport(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getStockAddReport(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetStockAddReport(?, ?)", startDate, endDate);
    }
    @Override
    public List<Map<String, Object>> getStockSubtractReport(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetStockSubtractReport(?, ?)", startDate, endDate);
    }


    @Override
    public List<Map<String, Object>> getExpiryAnalysis(Integer warnDays) {
        return getJdbcTemplate().queryForList("CALL GetExpiryAnalysis(?)", warnDays);
    }

    @Override
    public List<Map<String, Object>> getSalesTransactionsReport(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetSalesTransactionsReport(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getPaymentModeAnalysisByUser(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL PaymentModeAnalysisByUser(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getSalesProfitAnalysis(String startDate, String endDate, String groupByOption) {
        return getJdbcTemplate().queryForList("CALL GetSalesProfitAnalysis(?, ?, ?)", startDate, endDate, groupByOption);
    }

    @Override
    public List<Map<String, Object>> getMonthlySalesBreakdown(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetMonthlySalesBreakdown(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getTopSellingProducts(String startDate, String endDate, Integer limitCount) {
        return getJdbcTemplate().queryForList("CALL GetTopSellingProducts(?, ?, ?)", startDate, endDate, limitCount);
    }


    @Override
    public List<Map<String, Object>> getDayOfWeekSalesAnalysis(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetDayOfWeekSalesAnalysis(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getSalesAnalyticsReport(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetSalesAnalyticsReport(?, ?)", startDate, endDate);
    }

    @Override
    public int updateCategory(Integer id, String categoryName, boolean active, String modifiedBy) {
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
                    .withProcedureName("UpdateCategory");

            Map<String, Object> inParams = Map.of(
                    "p_id", id,
                    "p_category_name", categoryName,
                    "p_active", active ? 1 : 0,
                    "p_modified_by", modifiedBy
            );

            Map<String, Object> out = simpleJdbcCall.execute(inParams);
            return ((Number) out.get("p_updated_rows")).intValue();
        } catch (Exception e) {
            throw e; // Let higher layers handle exceptions
        }
    }

    @Override
    public int updateBrand(Long id, String brandName, boolean active, String modifiedBy) {
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
                    .withProcedureName("UpdateBrand");

            Map<String, Object> inParams = Map.of(
                    "p_id", id,
                    "p_brand_name", brandName,
                    "p_active", active ? 1 : 0,
                    "p_modified_by", modifiedBy
            );

            Map<String, Object> out = simpleJdbcCall.execute(inParams);
            return ((Number) out.get("p_updated_rows")).intValue();
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public int updateUom(Long id, String unitName, boolean active, String modifiedBy) {
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
                    .withProcedureName("UpdateUom");

            Map<String, Object> inParams = Map.of(
                    "p_id", id,
                    "p_unit_name", unitName,
                    "p_active", active ? 1 : 0,
                    "p_modified_by", modifiedBy
            );

            Map<String, Object> out = simpleJdbcCall.execute(inParams);
            return ((Number) out.get("p_updated_rows")).intValue();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int updateProduct(Long id, String name, String code, double price, double cost, int minimumStock,
                             Integer categoryId, Long brandId, Long uomId, Long productTypeId, boolean active, String modifiedBy) {
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
                    .withProcedureName("UpdateProduct");

            Map<String, Object> inParams = Map.ofEntries(
                    entry("p_id", id),
                    entry("p_name", name),
                    entry("p_code", code),
                    entry("p_price", price),
                    entry("p_cost", cost),
                    entry("p_minimum_stock", minimumStock),
                    entry("p_category_id", categoryId),
                    entry("p_brand_id", brandId),
                    entry("p_uom_id", uomId),
                    entry("p_product_type_id", productTypeId),
                    entry("p_active", active ? 1 : 0),
                    entry("p_modified_by", modifiedBy)
            );

            Map<String, Object> out = simpleJdbcCall.execute(inParams);
            return ((Number) out.get("p_updated_rows")).intValue();
        } catch (Exception e) {
            throw e;
        }
    }



    @Override
    public Optional<Integer> getProductStockByCode(String productCode) {
        try {
            // Call the stored procedure using CALL syntax
            String sql = "{CALL get_product_stock_by_code(?)}";

            List<Integer> results = getJdbcTemplate().query(
                    sql,
                    ps -> ps.setString(1, productCode), // set the IN parameter
                    (rs, rowNum) -> rs.getInt("current_stock") // map the result column
            );

            // If no results, return Optional.empty
            if (results.isEmpty()) {
                return Optional.empty();
            }

            // Return the first (and only) result
            return Optional.ofNullable(results.get(0));

        } catch (DataAccessException e) {
            // Log the error and return empty optional instead of throwing
            // You could also throw a custom exception here if preferred
            System.err.println("Error calling stored procedure: " + e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public List<Map<String, Object>> getSalesReceipt(String serialNumber) {
        return getJdbcTemplate().queryForList("CALL GetSalesReceipt(?)", serialNumber);
    }

    @Override
    public List<Map<String, Object>> getSalesTransactionSummary(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetSalesTransactionSummary(?, ?)", startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getDashboardAnalytics(int year, int month) {
        return getJdbcTemplate().queryForList("CALL GetDashboardAnalytics(?, ?)", year, month);
    }
    @Override
    public List<Map<String, Object>> searchPaginateSuppliers(String searchTerm, int pageNumber, int pageSize) {
        return getJdbcTemplate().queryForList("CALL SearchPaginateSuppliers(?, ?, ?)", searchTerm, pageNumber, pageSize);
    }

    @Override
    public Map<String, Object> getSupplierTransactions(Long supplierId, String startDate, String endDate) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(getJdbcTemplate())
                .withProcedureName("GetSupplierTransactions");

        Map<String, Object> inParams = Map.of(
                "p_supplierId", supplierId,
                "p_startDate", startDate,
                "p_endDate", endDate
        );
        return jdbcCall.execute(inParams);
    }

    @Override
    public List<Map<String, Object>> getSupplierDashboardAnalytics(int year, Integer month) {
        return getJdbcTemplate().queryForList("CALL GetSupplierDashboardAnalytics(?, ?)", year, month);
    }



    @Override
    public List<Map<String, Object>> getCustomerTransactions(Long customerId, int year, Integer month) {
        return getJdbcTemplate().queryForList("CALL GetCustomerTransactions(?, ?, ?)", customerId, year, month);
    }

    @Override
    public List<Map<String, Object>> getCustomerDashboardAnalytics(int year, Integer month) {
        return getJdbcTemplate().queryForList("CALL GetCustomerDashboardAnalytics(?, ?)", year, month);
    }
    @Override
    public List<Map<String, Object>> getStockReturnReport(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetStockReturnReport(?, ?)", startDate, endDate);
    }


    @Override
    public List<Map<String, Object>> getMonthlySalesAnalysis(int reportYear) {
        return getJdbcTemplate().queryForList("CALL GetMonthlySalesAnalysis(?)", reportYear);
    }

    @Override
    public List<Map<String, Object>> getMonthlyProfitAnalysis(int reportYear) {
        return getJdbcTemplate().queryForList("CALL GetMonthlyProfitAnalysis(?)", reportYear);
    }

    @Override
    public List<Map<String, Object>> getWeeklySalesAnalysis(String weekStartDate) {
        return getJdbcTemplate().queryForList("CALL GetWeeklySalesAnalysis(?)", weekStartDate);
    }


    @Override
    public List<Map<String, Object>> getStockReport(String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetStockReport(?, ?)", startDate, endDate);
    }


    @Override
    public List<Map<String, Object>> getStockValuationReportMonthlyData(int year, int month, Integer storeId) {
        return getJdbcTemplate().queryForList("CALL GetStockValuationReportMonthlyData(?, ?, ?)", year, month, storeId);
    }

    @Override
    public List<Map<String, Object>> getCounterStockSheet(String startDate, String endDate, Integer storeId) {
        return getJdbcTemplate().queryForList("CALL GetCounterStockSheet(?, ?, ?)", startDate, endDate, storeId);
    }

    @Override
    public List<Map<String, Object>> getCategoryProfitability(Integer storeId, String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetCategoryProfitability(?, ?, ?)", storeId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getStockVelocityAnalysis(Integer storeId, String startDate, String endDate) {
        return getJdbcTemplate().queryForList("CALL GetStockVelocityAnalysis(?, ?, ?)", storeId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getDemandForecast(Integer productId, Integer storeId, int forecastMonths) {
        return getJdbcTemplate().queryForList("CALL GetDemandForecast(?, ?, ?)", productId, storeId, forecastMonths);
    }

    @Override
    public List<Map<String, Object>> getInventoryABCAnalysis(Integer storeId, String analysisDate) {
        return getJdbcTemplate().queryForList("CALL GetInventoryABCAnalysis(?, ?)", storeId, analysisDate);
    }

    @Override
    public List<Map<String, Object>> getStockTrendAnalysis(Integer productId, Integer storeId, int analysisPeriodMonths) {
        return getJdbcTemplate().queryForList("CALL GetStockTrendAnalysis(?, ?, ?)", productId, storeId, analysisPeriodMonths);
    }

    @Override
    public List<Map<String, Object>> getLowStockProducts() {
        return getJdbcTemplate().queryForList("CALL GetLowStockProducts()");
    }
}