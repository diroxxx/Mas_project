package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Syllabus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 1000)
    private String requirements;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @OneToOne
    @NotNull
    @JoinColumn(name = "subject_id", nullable = false, updatable = false)
    private Subject definedBy;



}
