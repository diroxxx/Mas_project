package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    @NotNull
    private static int maxDailyHours = 12;

    @OneToOne(optional = false)
    @JoinColumn(name = "person_id", updatable = false, nullable = false)
    private Person personEmployee;


    @OneToOne(mappedBy = "employeeDean", cascade = CascadeType.REMOVE)
    private DeanOfficeEmployee deanOfficeEmployee;

    @OneToOne(mappedBy = "employeeTeacher", cascade = CascadeType.REMOVE)
    private Teacher teacher;
}
