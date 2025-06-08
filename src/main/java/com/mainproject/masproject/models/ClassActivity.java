package com.mainproject.masproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private LocalDate createdAt;
    @NotBlank
    private String note;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment accessedBy;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom heldIn;
}
