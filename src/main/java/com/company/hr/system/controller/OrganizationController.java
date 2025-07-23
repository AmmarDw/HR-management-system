package com.company.hr.system.controller;

import com.company.hr.system.model.Organization;
import com.company.hr.system.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
