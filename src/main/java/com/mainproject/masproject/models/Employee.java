package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    private static int maxDailyHours = 12;

//    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "person_id", updatable = false, nullable = false)
    private Person personEmployee;


    @Builder
    private Employee(LocalDate hireDate,
                     Teacher teacher,
                     DeanOfficeEmployee deanOffice) {

        this.hireDate   = hireDate;

        this.teacher    = requireNonNull(teacher);
        this.deanOfficeEmployee = requireNonNull(deanOffice);

        teacher.setEmployeeTeacher(this);
        deanOffice.setEmployeeDean(this);
    }


    @OneToOne(mappedBy = "employeeDean", cascade = CascadeType.ALL)
    private DeanOfficeEmployee deanOfficeEmployee;

    @OneToOne(mappedBy = "employeeTeacher", cascade = CascadeType.ALL)
    private Teacher teacher;


//    void setPerson(Person person) { this.personEmployee = person; }


}
