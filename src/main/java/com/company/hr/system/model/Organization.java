package com.company.hr.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_organization_id")
    @JsonIgnoreProperties({"manager", "description", "jobs", "type", "childOrganizations"})
    private Organization parentOrganization;

    @OneToMany(mappedBy = "parentOrganization", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"parentOrganization", "description", "jobs", "type"})
    private List<Organization> childOrganizations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnoreProperties({"jobs", "documents", "middleName", "birthDate", "email",
            "mobilePhoneNumber", "homePhoneNumber", "streetAddress",
            "city", "state", "zipCode", "country"})
    private Employee manager;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private Boolean type; // department=true, team=false

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"organization", "employee", "manager", "startDate", "endDate"})
    private List<Job> jobs = new ArrayList<>();
}