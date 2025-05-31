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
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 15)

    @Column(unique = true)
    private String index;

    @OneToOne(optional = false)
    @JoinColumn(name = "person_id", updatable = false, nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Person personStudent;


    @ManyToMany()
    @JoinTable(
            name = "student_group",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "group_uni_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<GroupUni> submitTo = new HashSet<>();


}
