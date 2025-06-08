package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.SubjectRealization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubjectRealizationRepository extends JpaRepository<SubjectRealization, Long> {


    @Query("""
    select sr from SubjectRealization sr
    join sr.includedBy s
    where s.id = :classId
""")
    public Optional<SubjectRealization> findBySubjectId(@Param("classId") Long classId);
}

