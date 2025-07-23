package com.company.hr.system.controller;

import com.company.hr.system.model.Employee;
import com.company.hr.system.model.Job;
import com.company.hr.system.model.Organization;
import com.company.hr.system.repository.EmployeeRepository;
import com.company.hr.system.repository.JobRepository;
import com.company.hr.system.repository.OrganizationRepository;
import com.company.hr.system.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final JobRepository jobRepository;

    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(
            @Valid @RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PostMapping("/{employeeId}/add-job")
    public ResponseEntity<Job> addJobToEmployee(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Boolean isNew,
            @RequestParam Long organizationId,
            @RequestParam(required = false) Long managerId,
            @RequestBody @Valid Job jobRequest) {

        // Fetch required entities
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        Organization organization = organizationRepository.findById(Math.toIntExact(organizationId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));

        Employee manager = null;
        if (managerId != null) {
            manager = employeeRepository.findById(managerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));
        }

        // Check if this is an initial job assignment
        boolean isInitialAssignment = isNew != null && isNew;
        boolean existingJob = jobRepository.existsByEmployee(employee);

        if (isInitialAssignment) {
            // Reject if employee already has job records
            if (existingJob) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Employee job details have already been initiated");
            }

            // Reject if end date is provided for initial job
            if (jobRequest.getEndDate() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cannot set end date for initial job assignment");
            }
        } else {
            // For non-initial jobs, check if employee has any existing jobs
            if (!existingJob && jobRequest.getEndDate() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cannot set end date for initial job details");
            }
        }

        // Create and save the job record
        Job job = Job.builder()
                .employee(employee)
                .organization(organization)
                .manager(manager)
                .title(jobRequest.getTitle())
                .startDate(jobRequest.getStartDate())
                .endDate(jobRequest.getEndDate())
                .build();

        Job savedJob = jobRepository.save(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
    }
}