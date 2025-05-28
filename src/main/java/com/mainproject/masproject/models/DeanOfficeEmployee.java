package com.mainproject.masproject.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DeanOfficeEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;




    @ElementCollection
    @CollectionTable(name = "responsibilities", joinColumns = @JoinColumn(name = "dean_office_employee_id"))
    private List<String> responsibilities = new ArrayList<>();

    @OneToOne(optional = false)
    @JoinColumn(name = "employee_id", updatable = false, nullable = false)
    private Employee employeeDean;

}
