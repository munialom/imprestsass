package com.ctecx.brs.mastertenant.entity;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

;

@Component
public class MasterTenantMapper {

    public Optional<MasterTenant> mapToMasterTenant(Map<String, Object> row) {
        return Optional.ofNullable(row)
                .map(this::createMasterTenant);
    }

    private MasterTenant createMasterTenant(Map<String, Object> row) {
        var masterTenant = new MasterTenant();

        // Basic fields
        Optional.ofNullable((Integer) row.get("tenant_client_id"))
                .ifPresent(masterTenant::setTenantClientId);

        Optional.ofNullable((String) row.get("db_name"))
                .ifPresent(masterTenant::setDbName);

        Optional.ofNullable((String) row.get("url"))
                .ifPresent(masterTenant::setUrl);

        Optional.ofNullable((String) row.get("user_name"))
                .ifPresent(masterTenant::setUserName);

        Optional.ofNullable((String) row.get("password"))
                .ifPresent(masterTenant::setPassword);

        Optional.ofNullable((String) row.get("driver_class"))
                .ifPresent(masterTenant::setDriverClass);

        Optional.ofNullable((String) row.get("status"))
                .ifPresent(masterTenant::setStatus);

        Optional.ofNullable((String) row.get("domain_url"))
                .ifPresent(masterTenant::setDomainUrl);

        return masterTenant;
    }
}