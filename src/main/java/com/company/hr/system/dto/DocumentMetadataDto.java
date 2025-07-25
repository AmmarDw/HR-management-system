package com.company.hr.system.dto;

import com.company.hr.system.model.DocumentTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentMetadataDto {
    private Long id;
    private DocumentTypeEnum type;
    private String fileName;
    private LocalDateTime uploadDate;

    // For National ID
    private String issuingAuthority;

    // For Contract
    private String contractVersion;

    // For Certification
    private String certificationName;
    private String issuingBody;
}