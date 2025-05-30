package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private LessonType type;

    @OneToMany(mappedBy = "scheduledBy", cascade = CascadeType.REMOVE)
    @NotNull
    private Set<Assignment> scheduledAs = new HashSet<>();

    @ManyToOne
    @NotNull
    @JoinColumn(name = "subject_realization_id", nullable = false)
    private SubjectRealization basedOn;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher taughtBy;
}
