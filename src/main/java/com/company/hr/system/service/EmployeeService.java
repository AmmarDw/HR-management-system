package com.company.hr.system.service;

import com.company.hr.system.dto.OrganizationEmployeeDto;
import com.company.hr.system.model.Employee;
import com.company.hr.system.model.Job;
import com.company.hr.system.model.Organization;
import com.company.hr.system.repository.EmployeeRepository;
import com.company.hr.system.repository.JobRepository;
import com.company.hr.system.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public Employee createEmployee(Employee employee) {
        // Check if email already exists
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Clear ID to ensure we're creating new employee
        employee.setId(null);

        // Clear relationships to avoid accidental persistence
        employee.setJobs(null);
        employee.setDocuments(null);

        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<OrganizationEmployeeDto> getEmployeesByOrganization(Long organizationId) {
        Organization organization = organizationRepository.findById(Math.toIntExact(organizationId))
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        return jobRepository.findByOrganization(organization).stream()
                .filter(job -> job.getEndDate() == null) // Only current employees
                .map(this::convertToOrganizationEmployeeDto)
                .toList();
    }

    private OrganizationEmployeeDto convertToOrganizationEmployeeDto(Job job) {
        Employee employee = job.getEmployee();
        return OrganizationEmployeeDto.builder()
                .employeeId(employee.getId())
                .fullName(employee.getFirstName() + " " + employee.getLastName())
                .jobTitle(job.getTitle())
                .email(employee.getEmail())
                .phoneNumber(employee.getMobilePhoneNumber() != null ?
                        employee.getMobilePhoneNumber() : employee.getHomePhoneNumber())
                .build();
    }
}