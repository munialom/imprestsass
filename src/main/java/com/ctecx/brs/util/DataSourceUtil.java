
package com.ctecx.brs.util;


import com.ctecx.brs.mastertenant.entity.MasterTenant;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * @author Md. Amran Hossain
 * Optimized version to reduce database server CPU usage
 */
public final class DataSourceUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceUtil.class);

    public static DataSource createAndConfigureDataSource(MasterTenant masterTenant) {
        HikariConfig config = new HikariConfig();

        // Basic connection settings
        config.setUsername(masterTenant.getUserName());
        config.setPassword(masterTenant.getPassword());
        config.setJdbcUrl(masterTenant.getUrl());
        config.setDriverClassName(masterTenant.getDriverClass());

        // Optimized connection pool sizing
        config.setMinimumIdle(2); // Reduced to minimize idle connections
        config.setMaximumPoolSize(8); // Slightly reduced to lower DB load

        // Timeout settings
        config.setConnectionTimeout(20000); // Reduced to 20s to fail fast
        config.setValidationTimeout(3000); // Reduced to 3s for quicker validation
        config.setInitializationFailTimeout(0); // Fail immediately if connection fails

        // Connection lifetime management
        config.setIdleTimeout(TimeUnit.MINUTES.toMillis(3)); // Reduced to 3m to recycle idle connections
        config.setMaxLifetime(TimeUnit.MINUTES.toMillis(15)); // Reduced to 15m to refresh connections
        config.setKeepaliveTime(TimeUnit.MINUTES.toMillis(1)); // Keepalive to maintain connection health

        // Leak detection to prevent resource exhaustion
        config.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(60)); // Log if connection is held > 60s



        // Pool naming
        String tenantConnectionPoolName = masterTenant.getDbName() + "-connection-pool";
        config.setPoolName(tenantConnectionPoolName);

        // Initialize DataSource
        HikariDataSource ds = new HikariDataSource(config);

        LOG.info("Configured datasource: {}. Connection pool name: {}",
                masterTenant.getDbName(), tenantConnectionPoolName);
        return ds;
    }


}