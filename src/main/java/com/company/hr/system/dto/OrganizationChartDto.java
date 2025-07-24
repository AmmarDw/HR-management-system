package com.company.hr.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationChartDto {
    private Long id;
    private String name;
    private String type;
    private String managerName;
    private List<OrganizationChartDto> childOrganizations;
}