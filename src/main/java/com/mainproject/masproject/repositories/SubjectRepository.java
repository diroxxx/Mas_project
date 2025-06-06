package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {


    @Query("""
    Select count(s) > 0 from Subject s
    where s.code =: code
""")
    boolean checkCodeUnique(@Param("code") String code);

    @Query("""
    select s from Subject s 
""")
    List<Subject> getAllSubjects();

}


