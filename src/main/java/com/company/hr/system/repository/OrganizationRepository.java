package com.company.hr.system.repository;

import com.company.hr.system.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    boolean existsByName(String name);
    List<Organization> findByParentOrganizationIsNull();
}
