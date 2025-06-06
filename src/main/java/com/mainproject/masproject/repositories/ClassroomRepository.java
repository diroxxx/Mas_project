package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.Classroom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends CrudRepository<Classroom, Long> {


    @Query("""
    Select c from Classroom c
""")
    List<Classroom> getAllClassrooms();

    @Query("""
    SELECT c FROM Classroom c
    JOIN c.holds ca
    JOIN ca.accessedBy a
    WHERE c.id = :classId AND a.startTime = :startTime AND a.dayOfWeek = :day
""")
    Optional<Classroom> findConflictingClassroom(@Param("classId") Long classId, @Param("day") String day, @Param("startTime") LocalTime startTime);

}
