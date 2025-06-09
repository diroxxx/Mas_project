package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "academic_title", joinColumns = @JoinColumn(name = "teacher_id"))
    private List<String> academicTitles = new ArrayList<>();

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "employee_id", updatable = false, nullable = false)
    private Employee employeeTeacher;


    @OneToMany(mappedBy = "taughtBy", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Lesson> teaches = new HashSet<>();

    @OneToMany(mappedBy = "taughtBy")
    @SQLRestriction("is_lead = true")
    private Set<Lesson> leads = new HashSet<>();

    public Teacher(List<String> academicTitles) {
        this.academicTitles = academicTitles;
        this.teaches = new HashSet<>();
        this.leads = new HashSet<>();
    }

    public void addTaughtLesson(Lesson lesson) {
        Objects.requireNonNull(lesson);
        teaches.add(lesson);
        lesson.setTaughtBy(this);
        lesson.setLead(false);
    }

    public void promoteToLead(Lesson lesson) {
        verifyTeaches(lesson);
        lesson.setLead(true);
    }

    public void demoteFromLead(Lesson lesson) {
        verifyTeaches(lesson);
        lesson.setLead(false);
    }

    private void verifyTeaches(Lesson lesson) {
        if (!teaches.contains(lesson)) {
            throw new IllegalStateException(
                    "Teacher must teach the lesson before it can be modified as lead");
        }
    }
}
