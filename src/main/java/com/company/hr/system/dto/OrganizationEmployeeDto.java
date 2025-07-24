package com.company.hr.system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationEmployeeDto {
    private Long employeeId;
    private String fullName;
    private String jobTitle;
    private String email;
    private String phoneNumber;
}