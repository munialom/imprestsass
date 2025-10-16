package com.ctecx.brs.tenant.imprestrepos;

import com.ctecx.brs.tenant.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {}