package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "academic_title", joinColumns = @JoinColumn(name = "teacher_id"))
    private List<String> academicTitles = new ArrayList<>();


    @OneToOne(optional = false)
    @JoinColumn(name = "employee_id", updatable = false, nullable = false)
    private Employee employeeTeacher;

    @OneToMany(mappedBy = "taughtBy", cascade = CascadeType.REMOVE)
    private Set<Lesson> teaches = new HashSet<>();

}
