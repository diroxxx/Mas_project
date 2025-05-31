package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.SubjectRealization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubjectRealizationRepository extends JpaRepository<SubjectRealization, Long> {

}

