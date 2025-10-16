package com.ctecx.brs.mastertenant.repository;






import com.ctecx.brs.mastertenant.entity.MasterTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Integer> {

    /*MasterTenant findByTenantClientId(Integer clientId);


    Optional<MasterTenant> findByDomainUrlAndStatus(String domainUrl, String status);*/

    @Query(value = "CALL sp_find_master_tenant_by_client_id(:clientId)", nativeQuery = true)
    Optional<MasterTenant> findByTenantClientId(@Param("clientId") Integer clientId);

    @Query(value = "CALL sp_find_master_tenant_by_domain_and_status(:domainUrl, :status)", nativeQuery = true)
    Optional<MasterTenant> findByDomainUrlAndStatus(@Param("domainUrl") String domainUrl, @Param("status") String status);


}
