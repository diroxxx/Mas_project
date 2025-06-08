package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.Lesson;
import com.mainproject.masproject.models.LessonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {


    @Query("""
    select  distinct l.type from Lesson l
""")
    public List<LessonType> getAllTypes();


    public Optional<LessonType> findTypeById(Long id);

}
