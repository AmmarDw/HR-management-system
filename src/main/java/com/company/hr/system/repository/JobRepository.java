package com.company.hr.system.repository;

import com.company.hr.system.model.Employee;
import com.company.hr.system.model.Job;
import com.company.hr.system.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByEmployeeOrderByStartDateAsc(Employee employee);
    List<Job> findByOrganization(Organization organization);
}
