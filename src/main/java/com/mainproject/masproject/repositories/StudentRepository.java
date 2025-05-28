package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
