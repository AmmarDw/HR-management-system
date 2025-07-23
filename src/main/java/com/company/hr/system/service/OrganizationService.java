package com.company.hr.system.service;

import com.company.hr.system.model.Employee;
import com.company.hr.system.model.Organization;
import com.company.hr.system.repository.EmployeeRepository;
import com.company.hr.system.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Organization createOrganization(Organization organization,
                                           Long parentOrganizationId,
                                           Long managerId) {
        // Check for unique organization name
        if (organizationRepository.existsByName(organization.getName())) {
            throw new IllegalArgumentException(
                    "Organization with name '" + organization.getName() + "' already exists");
        }

        // Set parent organization if provided
        if (parentOrganizationId != null) {
            Organization parent = organizationRepository.findById(Math.toIntExact(parentOrganizationId))
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Parent organization not found"));
            organization.setParentOrganization(parent);
        }

        // Set manager if provided
        if (managerId != null) {
            Employee manager = employeeRepository.findById(managerId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Manager employee not found"));
            organization.setManager(manager);
        }

        return organizationRepository.save(organization);
    }
}
