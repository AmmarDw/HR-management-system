package com.company.hr.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NotNull(message = "Employee is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"documents"})
    private Employee employee;

    @NotNull(message = "Document type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DocumentTypeEnum type;  // e.g., "National ID", "Contract", "Certification"

    // file related fields
    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name cannot exceed 255 characters")
    @Column(nullable = false, length = 255)
    private String fileName;

    @NotBlank(message = "MIME type is required")
    @Size(max = 50, message = "MIME type cannot exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String mimeType;  // e.g., "application/pdf", "image/jpeg"

    @NotNull(message = "File content is required")
    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] fileContent;

    @NotNull(message = "File size is required")
    @Column(nullable = false)
    private Long fileSize;  // in bytes


    private LocalDate issueDate; // used for Contract Start Date
    private LocalDate expiryDate; // used for Contract End Date

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime uploadDate;

//  Issuing Authority if doc type is National ID
//  Contract Version if doc type is Contract
//  Certification Name if doc type is Certification & Not Blank
    @Size(max = 255)
    @Column(length = 255)
    private String info;
}
