package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;

    @NotNull
    @Size(min = 11, max = 11)
    private String pesel;

    @NotNull
    @Email
    private String email;

    @Size(min = 9, max = 9)
    private int phoneNumber;

    @OneToOne(mappedBy = "personStudent", cascade = CascadeType.ALL)
    private Student student;

    @OneToOne(mappedBy = "personEmployee" , cascade = CascadeType.ALL)
    private Employee personEmployee;


    @Builder
    private Person(String firstName,
                   String lastName,
                   String email,
                   String pesel,
                   Integer phoneNumber,
                   Student student,
                   Employee employee) {

        this.firstName   = firstName;
        this.lastName    = lastName;
        this.email       = email;
        this.pesel       = pesel;
        this.phoneNumber = phoneNumber;

        this.student  = Objects.requireNonNull(student);
        this.personEmployee = Objects.requireNonNull(employee);

        student.setPersonStudent(this);
        employee.setPersonEmployee(this);
    }

    public String    getIndexNumber()   { return student.getIndex(); }
    public LocalDate getHireDate()      { return personEmployee.getHireDate();  }



}
