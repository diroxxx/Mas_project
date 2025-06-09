package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.aspectj.apache.bcel.classfile.ClassFormatException;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;

    @Setter
    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;

    @Setter
    @NotNull
    @Size(min = 11, max = 11)
    private String pesel;

    @Setter
    @NotNull
    @Email
    private String email;

    @Setter
    @Size(min = 9, max = 9)
    private int phoneNumber;

    @OneToOne(mappedBy = "personStudent", cascade = CascadeType.ALL)
    private Student student;

    @OneToOne(mappedBy = "personEmployee" , cascade = CascadeType.ALL)
    private Employee personEmployee;


   public Person(String firstName,
                   String lastName,
                   String email,
                   String pesel,
                   int phoneNumber,
                   Student student,
                   Employee employee) {

        this.firstName   = firstName;
        this.lastName    = lastName;
        this.email       = email;
        this.pesel       = pesel;
        this.phoneNumber = phoneNumber;

        if (student == null && employee == null) {
            throw new NullPointerException("Null student or employee can not be null at the same time");
        }
        if (student != null) {
            this.student = student;
            student.setPersonStudent(this);
        }
        if (employee != null) {
            this.personEmployee = employee;
            employee.setPersonEmployee(this);
        }
    }

    public String getIndexNumber() {
        if (student != null) {
            return student.getIndex();
        }
        throw new ClassFormatException("student is null, so you can't get index number");
    }
    public LocalDate getHireDate() {    {
        if (personEmployee != null) {
            return personEmployee.getHireDate();
        }
        throw new ClassCastException("personEmployee is null , so you can't get hire date");  }
        }

}
