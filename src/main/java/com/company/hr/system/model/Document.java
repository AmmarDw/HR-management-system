package com.company.hr.system.model;

import jakarta.persistence.*;
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull
    @Column(nullable = false, length = 50)
    private String type;  // e.g., "National ID", "Contract", "Certification"

    // file related fields
    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 50)
    private String mimeType;  // e.g., "application/pdf", "image/jpeg"

    @Lob
    @Column(nullable = false)
    private byte[] fileContent;

    @Column(nullable = false)
    private Long fileSize;  // in bytes


    private LocalDate issueDate;
    private LocalDate expiryDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime uploadDate;

//  Issuing Authority if doc type is National ID
//  Contract Version if doc type is Contract
//  Certification Name if doc type is Certification
    @Size(max = 100)
    @Column(length = 100)
    private String info;
}
