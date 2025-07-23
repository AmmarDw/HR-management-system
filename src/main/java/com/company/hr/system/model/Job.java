package com.company.hr.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties({"parentOrganization", "manager", "description", "jobs", "type"})
    private Organization organization;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties({"jobs", "documents", "middleName", "birthDate", "email",
            "mobilePhoneNumber", "homePhoneNumber", "streetAddress",
            "city", "state", "zipCode", "country"})
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties({"jobs", "documents", "middleName", "birthDate", "email",
            "mobilePhoneNumber", "homePhoneNumber", "streetAddress",
            "city", "state", "zipCode", "country"})
    private Employee manager;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String title;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;
}