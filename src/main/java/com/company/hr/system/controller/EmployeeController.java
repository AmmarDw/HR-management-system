package com.company.hr.system.controller;

import com.company.hr.system.dto.OrganizationEmployeeDto;
import com.company.hr.system.model.Employee;
import com.company.hr.system.model.Job;
import com.company.hr.system.service.EmployeeService;
import com.company.hr.system.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JobService jobService;

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

        Job job = jobService.assignJobToEmployee(
                employeeId,
                organizationId,
                managerId,
                jobRequest,
                isNew != null && isNew
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @GetMapping("/view")
    public ResponseEntity<List<OrganizationEmployeeDto>> getOrganizationEmployees(
            @RequestParam Long organizationId) {
        List<OrganizationEmployeeDto> employees = employeeService.getEmployeesByOrganization(organizationId);
        return ResponseEntity.ok(employees);
    }
}