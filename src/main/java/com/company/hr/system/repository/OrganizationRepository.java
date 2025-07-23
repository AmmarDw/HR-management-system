package com.company.hr.system.repository;

import com.company.hr.system.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    boolean existsByName(String name);
}
