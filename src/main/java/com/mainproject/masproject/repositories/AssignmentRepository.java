package com.mainproject.masproject.repositories;

import com.mainproject.masproject.dtos.GroupLessonDto;
import com.mainproject.masproject.models.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {



}
