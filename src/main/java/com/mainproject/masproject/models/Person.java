package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
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

    @OneToOne(mappedBy = "personStudent", cascade = CascadeType.REMOVE)
    private Student student;

    @OneToOne(mappedBy = "personEmployee" , cascade = CascadeType.REMOVE)
    private Employee personEmployee;


}
