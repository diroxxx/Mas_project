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
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private LessonType type;
    @Column(name = "is_lead", nullable = false)
    private boolean lead;


    @OneToMany(mappedBy = "scheduledBy", cascade = CascadeType.ALL, orphanRemoval = true)
//    @NotNull
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Assignment> scheduledAs = new HashSet<>();

    @ManyToOne
//    @NotNull
    @JoinColumn(name = "subject_realization_id" , nullable = false)
    private SubjectRealization basedOn;

    @ManyToOne
//    @NotNull
    @JoinColumn(name = "teacher_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Teacher taughtBy;

}
