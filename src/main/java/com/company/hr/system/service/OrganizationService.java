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
                .type(organization.getType() ? "Department" : "Team")
                .managerName(organization.getManager() != null ?
                        organization.getManager().getFirstName() + " " + organization.getManager().getLastName() :
                        null)
                .childOrganizations(organization.getChildOrganizations().stream()
                        .map(this::convertToChartDto)
                        .toList())
                .build();
    }

    @Transactional
    public Organization updateOrganization(Long organizationId, Organization updates, Long newParentOrganizationId) {
        Organization existing = organizationRepository.findById(Math.toIntExact(organizationId))
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        // Protect immutable fields
        updates.setId(existing.getId());
        updates.setChildOrganizations(existing.getChildOrganizations());
        updates.setJobs(existing.getJobs());
        updates.setManager(existing.getManager()); // Manager has separate endpoint

        // Validate and update name
        if (updates.getName() != null && !updates.getName().isBlank()) {
            if (!existing.getName().equals(updates.getName()) &&
                    organizationRepository.existsByName(updates.getName())) {
                throw new IllegalArgumentException("Organization name already exists");
            }
            existing.setName(updates.getName());
        }

        // Validate and update parent organization if provided
        if (newParentOrganizationId != null) {
            Organization newParentOrganization = organizationRepository.findById(Math.toIntExact(newParentOrganizationId))
                    .orElseThrow(() -> new IllegalArgumentException("New parent organization not found"));
            existing.setParentOrganization(newParentOrganization);
        }

        // Update description if provided
        if (updates.getDescription() != null) {
            existing.setDescription(updates.getDescription());
        }

        // Update type if provided
        if (updates.getType() != null) {
            existing.setType(updates.getType());
        }

        return organizationRepository.save(existing);
    }

    @Transactional
    public Organization setOrganizationManager(Long organizationId, Long employeeId) {
        Organization organization = organizationRepository.findById(Math.toIntExact(organizationId))
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        if (employeeId == null) {
            organization.setManager(null);
        } else {
            Employee manager = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
            organization.setManager(manager);
        }

        return organizationRepository.save(organization);
    }
}
