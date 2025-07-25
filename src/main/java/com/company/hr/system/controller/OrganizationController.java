package com.company.hr.system.controller;

import com.company.hr.system.dto.OrganizationChartDto;
import com.company.hr.system.model.Organization;
import com.company.hr.system.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Organization> addOrganization(
            @Valid @RequestBody Organization organization,
            @RequestParam(required = false) Long parentOrganizationId,
            @RequestParam(required = false) Long managerId) {

        Organization createdOrg = organizationService.createOrganization(
                organization, parentOrganizationId, managerId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrg);
    }

    @GetMapping("/chart")
    public ResponseEntity<List<OrganizationChartDto>> getOrganizationChart() {
        List<Organization> topLevelOrgs = organizationService.findTopLevelOrganizations();
        List<OrganizationChartDto> chartDtos = topLevelOrgs.stream()
                .map(organizationService::convertToChartDto)
                .toList();
        return ResponseEntity.ok(chartDtos);
    }

    @PatchMapping("/{organizationId}/update")
    public ResponseEntity<Organization> updateOrganization(
            @PathVariable Long organizationId,
            @RequestParam(required = false) Long newParentOrganizationId,
            @RequestBody Organization updates) {

        Organization updatedOrg = organizationService.updateOrganization(organizationId, updates, newParentOrganizationId);
        return ResponseEntity.ok(updatedOrg);
    }

    @PutMapping("/{organizationId}/set-manager")
    public ResponseEntity<Organization> setOrganizationManager(
            @PathVariable Long organizationId,
            @RequestParam(required = false) Long employeeId) {

        Organization updatedOrg = organizationService.setOrganizationManager(organizationId, employeeId);
        return ResponseEntity.ok(updatedOrg);
    }
}
