package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    private static int maxDailyHours = 12;

    @OneToOne(optional = false)
    @JoinColumn(name = "person_id", updatable = false, nullable = false)
    private Person personEmployee;


    public Employee(LocalDate hireDate,
                     Teacher teacher,
                     DeanOfficeEmployee deanOffice) {
        this.hireDate   = hireDate;
        if (teacher == null && deanOffice == null) {
            throw new NullPointerException("Teacher and DeanOffice cannot be null at the same time");
        }

        if (teacher != null) {
        this.teacher= requireNonNull(teacher);
        teacher.setEmployeeTeacher(this);
        }
        if (deanOffice != null) {
        deanOffice.setEmployeeDean(this);
        this.deanOfficeEmployee = requireNonNull(deanOffice);
        }
    }

    @OneToOne(mappedBy = "employeeDean", cascade = CascadeType.ALL)
    private DeanOfficeEmployee deanOfficeEmployee;

    @OneToOne(mappedBy = "employeeTeacher", cascade = CascadeType.ALL)
    private Teacher teacher;




    public List<String> getResponsibilities () {
        if (deanOfficeEmployee == null) {
            throw new NullPointerException("DeanOfficeEmployee is null");
        }
        return deanOfficeEmployee.getResponsibilities();

    }

    public List<String> getAcademicTitles() {
        if(teacher == null) {
            throw new NullPointerException("Teacher is null");
        }
        return teacher.getAcademicTitles();
    }

}
