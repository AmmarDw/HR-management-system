package com.company.hr.system.repository;

import com.company.hr.system.model.Employee;
import com.company.hr.system.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {
    boolean existsByEmployee(Employee employee);
}
