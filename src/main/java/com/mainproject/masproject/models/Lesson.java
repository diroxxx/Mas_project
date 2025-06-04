package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @JoinColumn(name = "subject_realization_id" , nullable = false)
    private SubjectRealization basedOn;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "teacher_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Teacher taughtBy;

    @ManyToOne()
    @JoinColumn(name = "teacher_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Teacher leadBy;
}
