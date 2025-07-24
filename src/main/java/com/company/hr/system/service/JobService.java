package com.company.hr.system.service;

import com.company.hr.system.model.*;
import com.company.hr.system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public Job assignJobToEmployee(Long employeeId, Long organizationId, Long managerId, Job jobRequest, boolean isInitialAssignment) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        Organization organization = organizationRepository.findById(Math.toIntExact(organizationId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));

        Employee manager = managerId != null ?
                employeeRepository.findById(managerId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found")) :
                null;

        // Validate job start date is not in the future
        if (jobRequest.getStartDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Job cannot start in the future. Start date must be today or earlier");
        }

        validateJobAssignment(employee, jobRequest, isInitialAssignment);

        Job job = Job.builder()
                .employee(employee)
                .organization(organization)
                .manager(manager)
                .title(jobRequest.getTitle())
                .startDate(jobRequest.getStartDate())
                .endDate(jobRequest.getEndDate())
                .build();

        return jobRepository.save(job);
    }

    private void validateJobAssignment(Employee employee, Job jobRequest, boolean isInitialAssignment) {
        List<Job> existingJobs = jobRepository.findByEmployeeOrderByStartDateAsc(employee);

        if (isInitialAssignment) {
            if (!existingJobs.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Employee job details have already been initiated");
            }

            if (jobRequest.getEndDate() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cannot set end date for initial job assignment");
            }
        }

        if (jobRequest.getEndDate() != null &&
                jobRequest.getEndDate().isBefore(jobRequest.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "End date must be after start date");
        }

        if (!existingJobs.isEmpty()) {
            validateNoOverlappingPeriods(jobRequest, existingJobs);
        }
    }

    private void validateNoOverlappingPeriods(Job newJob, List<Job> existingJobs) {
        LocalDate newJobEndDate, existingJobEndDate;

        if (newJob.getEndDate() == null) {
            newJobEndDate = LocalDate.now();
        } else {
            newJobEndDate = newJob.getEndDate();
        }

        for (Job existingJob : existingJobs) {
            if (existingJob.getEndDate() == null) {
                existingJobEndDate = LocalDate.now();
            } else {
                existingJobEndDate = existingJob.getEndDate();
            }

            // [newJob.getStartDate(), newJobEndDate] -> [A, B]
            // [existingJob.getStartDate(), existingJobEndDate] -> [C, D]
            if (    !newJob.getStartDate().isAfter(existingJobEndDate) && // A <= D
                    !existingJob.getStartDate().isAfter(newJobEndDate)) {   // C <= B)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("New job overlaps with existing job period: %s to %s",
                                existingJob.getStartDate(),
                                existingJob.getEndDate() == null ? "present" : existingJob.getEndDate()));
            }
        }
    }
}