package com.company.hr.system.service;

import com.company.hr.system.model.Employee;
import com.company.hr.system.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

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
}