package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.ClassActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassActivityRepository extends CrudRepository<ClassActivity, Long> {

    @Query("""
    Select ca from ClassActivity ca
    where ca.heldIn.id =:classId and ca.accessedBy.id =:assignmentId
""")
    public Optional<ClassActivity> findClassActivity(@Param("classId") Long classId, @Param("assignmentId") Long assignmentId);

}
