package com.company.hr.system.model;

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
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Organization organization;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Employee employee;

    @ManyToOne
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