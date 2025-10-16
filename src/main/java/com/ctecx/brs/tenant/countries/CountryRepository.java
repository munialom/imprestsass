package com.ctecx.brs.tenant.countries;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CountryRepository extends JpaRepository<Country, Long> {
    boolean existsByName(String name);

    boolean existsByAbbreviation(String abbreviation);

    @Query("SELECT c FROM Country c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Country> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT c FROM Country c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND c.status = true")
    Page<Country> findActiveByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
    @Query("SELECT COUNT(c) FROM Country c")
    long countAllCountries();

    @Query("SELECT COUNT(c) FROM Country c WHERE c.status = true")
    long countActiveCountries();

    @Query("SELECT COUNT(c) FROM Country c WHERE c.status = false")
    long countInactiveCountries();
}