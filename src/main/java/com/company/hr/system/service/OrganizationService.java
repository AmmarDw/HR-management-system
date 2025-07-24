package com.company.hr.system.service;

import com.company.hr.system.dto.OrganizationChartDto;
import com.company.hr.system.model.Employee;
import com.company.hr.system.model.Organization;
import com.company.hr.system.repository.EmployeeRepository;
import com.company.hr.system.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<Organization> findTopLevelOrganizations() {
        return organizationRepository.findByParentOrganizationIsNull();
    }

    public OrganizationChartDto convertToChartDto(Organization organization) {
        return OrganizationChartDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .type(organization.isType() ? "Department" : "Team")
                .managerName(organization.getManager() != null ?
                        organization.getManager().getFirstName() + " " + organization.getManager().getLastName() :
                        null)
                .childOrganizations(organization.getChildOrganizations().stream()
                        .map(this::convertToChartDto)
                        .toList())
                .build();
    }
}
