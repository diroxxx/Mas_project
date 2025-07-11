package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String dayOfWeek;

    @NotNull
    private LocalTime startTime;

    private static double durationInMinutes = 90;

    @ElementCollection
    @CollectionTable(name = "time_slot", joinColumns = @JoinColumn(name = "assignment_id"))
    private Set<LocalTime> availableTimeSlots = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "group_uni_id")
    private GroupUni attendedBy;

    @OneToMany(mappedBy = "accessedBy", cascade = CascadeType.ALL, orphanRemoval = true)
//    @NotNull
    private Set<ClassActivity> accessTo = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson scheduledBy;


    public static double getDurationInMinutes() {
        return durationInMinutes;
    }
}
