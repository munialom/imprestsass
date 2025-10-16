package com.ctecx.brs.tenant.util;

import org.springframework.data.jpa.repository.JpaRepository;



public interface RegisteredComputerRepository extends JpaRepository<RegisteredComputer, Long> {
    boolean existsByComputerNameAndActive(String computerName, boolean active);
    boolean existsByIpAddressAndActive(String ipAddress, boolean active);
}