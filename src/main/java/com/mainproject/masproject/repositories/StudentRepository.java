package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends CrudRepository<Student, Long> {

    @Query("""
    select case when count(s) = 0 then true else false end 
    from Student s 
    where s.index = :newIndex
""")
    boolean checkIndexUnique(@Param("newIndex")String newIndex);
}
