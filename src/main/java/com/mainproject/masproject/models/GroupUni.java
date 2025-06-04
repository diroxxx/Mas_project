package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GroupUni {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String name;
    @Size(min = 1, max = 30)
    private int capacity;


//    public void addStudent()


    @ManyToMany(mappedBy = "submitTo")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @MapKey(name = "index")
    private Map<String,Student> submitedBy = new HashMap<>();

    @OneToMany(mappedBy = "attendedBy", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Assignment> attendsIn = new HashSet<>();





}
