package com.company.hr.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(length = 50)
    private String middleName;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    private LocalDate birthDate;

    @NotBlank
    @Email
    @Size(max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Size(max = 20)
    @Column(length = 20)
    private String mobilePhoneNumber;

    @Size(max = 20)
    @Column(length = 20)
    private String homePhoneNumber;

    @Size(max = 50)
    @Column(length = 50)
    private String streetAddress;

    @Size(max = 20)
    @Column(length = 20)
    private String city;

    @Size(max = 20)
    @Column(length = 20)
    private String state;

    @Size(max = 20)
    @Column(length = 20)
    private String zipCode;

    @Size(max = 20)
    @Column(length = 20)
    private String country;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();

    // no need for jobs list for manager relation
}