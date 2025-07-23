package com.company.hr.system.dto;

import com.company.hr.system.model.DocumentTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadRequest {
    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Document type is required")
    private DocumentTypeEnum type;

    @NotNull(message = "File is required")
    private MultipartFile file;

    private LocalDate issueDate;
    private LocalDate expiryDate;

    // For National ID
    @Size(max = 100, message = "Issuing authority cannot exceed 100 characters")
    private String issuingAuthority;

    // For Contract
    @Size(max = 20, message = "Contract version cannot exceed 20 characters")
    private String contractVersion;

    // For Certification
    @Size(max = 100, message = "Certification name cannot exceed 100 characters")
    private String certificationName;
    @Size(max = 100, message = "Issuing body cannot exceed 100 characters")
    private String issuingBody;
}