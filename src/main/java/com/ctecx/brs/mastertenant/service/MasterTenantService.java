package com.ctecx.brs.mastertenant.service;


import com.ctecx.brs.mastertenant.entity.MasterTenant;

import java.util.Optional;

/**
 * @author Md. Amran Hossain
 */
public interface MasterTenantService {

    Optional<MasterTenant> findByClientId(Integer clientId);
    Optional<MasterTenant> findByDomainUrl(String domainUrl);
}
