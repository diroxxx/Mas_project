package com.mainproject.masproject.repositories;

import com.mainproject.masproject.dtos.GroupLessonDto;
import com.mainproject.masproject.models.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("""
    SELECT new com.mainproject.masproject.dtos.GroupLessonDto(
        s.includedBy.id, 
        s.includedBy.code,
        t.id, 
        CONCAT(t.employeeTeacher.personEmployee.firstName, ' ', t.employeeTeacher.personEmployee.lastName),
        l.type,
        g.capacity,
        a.dayOfWeek, 
        a.startTime,
        c.roomNumber
    )
    FROM Assignment a
    JOIN a.scheduledBy l
    JOIN l.taughtBy t
    JOIN l.basedOn s
    JOIN a.attendedBy g
    JOIN ClassActivity ca ON ca.accessedBy = a
    JOIN ca.heldIn c
    where g.id = :groupId
""")
    List<GroupLessonDto> getFullSchedule(@Param("groupId") Long groupId);

}
