package com.mainproject.masproject.repositories;

import com.mainproject.masproject.dtos.GroupLessonDto;
import com.mainproject.masproject.models.Assignment;
import com.mainproject.masproject.models.LessonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {


    @Query("""
       SELECT DISTINCT ts
       FROM Assignment a
       JOIN a.availableTimeSlots ts
       """)
    List<LocalTime> findAllTimeSlots();

    @Query("""
       select a from Assignment a
       join a.scheduledBy l
       where l.id = :lessonId and a.startTime =:startTime and lower(a.dayOfWeek) = lower(:dayOfWeek)
""")
    public Optional<Assignment> findByLessonId(@Param("lessonId") Long lessonId, @Param("startTime") LocalTime startTime, @Param("dayOfWeek") String dayOfWeek);


    @Query("""
       select a from Assignment a
       join a.scheduledBy l
       where  a.startTime =:startTime and lower(a.dayOfWeek) = lower(:dayOfWeek) and a.id =:assignmentId 
""")
    public Optional<Assignment> findByLessonToDeleteId( @Param("startTime") LocalTime startTime, @Param("dayOfWeek") String dayOfWeek, @Param("assignmentId") Long assignmentId);


    @Query("""
    SELECT a
    FROM  Assignment a
    ORDER BY
      CASE  upper(a.dayOfWeek)
        WHEN 'MONDAY'    THEN 1
        WHEN 'TUESDAY'   THEN 2
        WHEN 'WEDNESDAY' THEN 3
        WHEN 'THURSDAY'  THEN 4
        WHEN 'FRIDAY'    THEN 5
        ELSE 7
      END,
      a.startTime
    """)
    List<Assignment> findAllOrdered();

}
