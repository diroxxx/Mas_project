package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {


    @Query("""
    SELECT CASE WHEN COUNT(a) = 0 THEN true ELSE false END
    FROM Teacher t
    JOIN t.teaches l
    JOIN l.scheduledAs a
    WHERE t.id = :id
      AND a.dayOfWeek = :day
      AND a.startTime = :startTime
      AND (:assignmentId IS NULL OR a.id <> :assignmentId)
""")

    boolean isTeacherAvailable(
            @Param("id") Long teacherId,
            @Param("day") String day,
            @Param("startTime") LocalTime startTime,
            @Param("assignmentId") Long assignmentId
    );

    @Query("""
    SELECT t.id AS id, CONCAT(p.firstName, ' ', p.lastName) AS fullName
        from Teacher t
        join t.employeeTeacher e
        join e.personEmployee p
""")
    List<TeacherProjection> getAllTeachers();

    public interface TeacherProjection {
        Long getId();
        String getFullName();
    }
}

