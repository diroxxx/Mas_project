package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
