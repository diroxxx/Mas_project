package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"subject_id", "semester_id"})
})
public class SubjectRealization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ModeType mode;
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private LangType language;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "subject_id" ,nullable = false)
    private Subject includedBy;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester offeredBy;

    @OneToMany(mappedBy = "basedOn", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Lesson> basedFor = new HashSet<>();




}
