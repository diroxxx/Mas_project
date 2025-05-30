package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private LocalDate occupiedDate;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private ActivityStatus status;


    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment accessedBy;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom heldIn;
}
