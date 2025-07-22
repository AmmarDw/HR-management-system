package com.company.hr.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
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

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String firstName;

    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    @Column(length = 50)
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotNull(message = "Birth date is required")
    @Column(nullable = false)
    private LocalDate birthDate;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Size(max = 20, message = "Mobile phone cannot exceed 20 characters")
    @Column(length = 20)
    private String mobilePhoneNumber;

    @Size(max = 20, message = "Home phone cannot exceed 20 characters")
    @Column(length = 20)
    private String homePhoneNumber;

    @Size(max = 50, message = "Street address cannot exceed 50 characters")
    @Column(length = 50)
    private String streetAddress;

    @Size(max = 20, message = "City cannot exceed 20 characters")
    @Column(length = 20)
    private String city;

    @Size(max = 20, message = "State cannot exceed 20 characters")
    @Column(length = 20)
    private String state;

    @Size(max = 20, message = "Zip code cannot exceed 20 characters")
    @Column(length = 20)
    private String zipCode;

    @Size(max = 20, message = "Country cannot exceed 20 characters")
    @Column(length = 20)
    private String country;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();

    // no need for jobs list for manager relation
}
