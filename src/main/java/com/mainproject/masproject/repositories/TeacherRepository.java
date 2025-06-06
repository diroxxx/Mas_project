package com.mainproject.masproject.repositories;

import com.mainproject.masproject.dtos.TeacherDto;
import com.mainproject.masproject.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {







    @Query("""
       select t from Teacher t
       join t.teaches l
       join l.scheduledAs a
       where t.id =:id and a.dayOfWeek =:day and a.startTime=: startTime
""")
    Optional<Teacher> existTeacherInGivenTime(@Param("id") Long id,@Param("day") String day,@Param("startTime") LocalTime startTime);

    @Query("""
    SELECT t.id AS id, CONCAT(p.firstName, ' ', p.lastName) AS fullName
        from Teacher t
        join t.employeeTeacher e
        join e.personEmployee p
""")
    List<TeacherProjection> findAllTeachers();

    public interface TeacherProjection {
        Long getId();
        String getFullName();
    }


//    @Query("""
//        select new com.mainproject.masproject.dtos.TeacherDto(t.id, concat(p.firstName, ' ', p.lastName) ) from Teacher t
//        join t.employeeTeacher e
//        join e.personEmployee p
//
//""")
//    List<TeacherDto> findAllTeachers();
}

