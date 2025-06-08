package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 15)

    @Column(unique = true)
    private String index;

    @Enumerated(EnumType.ORDINAL)
    private StudentStatus status;

    private double tuitionBalance;


    public Student(String index,  double tuitionBalance) {
        this.index = index;
        this.tuitionBalance = tuitionBalance;
    }
    void setPerson(Person person) { this.personStudent = person; }



    public void graduate() {
        this.status = StudentStatus.GRADUATED;
    }
    public void withdrawn() {
        this.status = StudentStatus.WITHDRAWN;
    }
    public void sendRequest() {
        this.status = StudentStatus.STUDY_BREAK;
    }
    public void resumeStudies() {
        this.status = StudentStatus.ACTIVE;
    }
    public void checkBalance() {
        if (this.tuitionBalance < 0) {
            this.status = StudentStatus.SUSPENDED;

        } else {
            this.status = StudentStatus.ACTIVE;
        }
    }
    public void archiveStudent() {
        this.status = StudentStatus.GRADUATED;
    }
    public void finalizeGraduation() {
        this.status = StudentStatus.WITHDRAWN;
    }

//    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "person_id", updatable = false, nullable = false)
    private Person personStudent;


    @ManyToMany()
    @JoinTable(
            name = "student_group",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "group_uni_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<GroupUni> submitTo = new HashSet<>();


}
