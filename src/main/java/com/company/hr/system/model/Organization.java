package com.company.hr.system.model;

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
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_organization_id")
    private Organization parentOrganization;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    private List<Job> jobs = new ArrayList<>();
}