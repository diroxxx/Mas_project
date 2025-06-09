package com.mainproject.masproject.repositories;

import com.mainproject.masproject.dtos.GroupLessonDto;
import com.mainproject.masproject.models.GroupUni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface GroupUniRepository extends JpaRepository<GroupUni, Long> {

    @Query("""
    SELECT CASE WHEN COUNT(a) = 0 THEN true ELSE false END
    FROM GroupUni g
    JOIN g.attendsIn a
    JOIN a.accessTo ca
    JOIN ca.heldIn c
    WHERE g.id = :id
      AND lower(a.dayOfWeek) = lower(:day) 
      AND a.startTime = :startTime
""")
    boolean isGroupUniAvailable(
            @Param("id") Long id,
            @Param("day") String day,
            @Param("startTime") LocalTime startTime
    );




    @Query("""
SELECT COUNT(a) > 0
FROM GroupUni g
JOIN g.attendsIn a
WHERE g.id = :groupId
  AND lower(a.dayOfWeek) = lower(:day)
  AND a.startTime = :startTime
  AND a.id <> :currentAssignmentId
""")
    boolean isGroupUniAvailableWithAssignment(
            @Param("groupId") Long id,
            @Param("day") String day,
            @Param("startTime") LocalTime startTime,
            @Param("currentAssignmentId") Long currentAssignmentId
    );


    Optional<GroupUni> findById(Long id);

    @Query("""
SELECT new com.mainproject.masproject.dtos.GroupLessonDto(
    l.basedOn.includedBy.id,
    l.basedOn.includedBy.code,
    l.taughtBy.id,
    CONCAT(l.taughtBy.employeeTeacher.personEmployee.firstName, ' ',
           l.taughtBy.employeeTeacher.personEmployee.lastName),
    l.type,
    g.capacity,
    a.dayOfWeek,
    a.startTime,
    ca.heldIn.roomNumber,
    c.id,
    c.capacity,
    l.id,
    a.id
)
FROM Assignment a
JOIN a.scheduledBy l
JOIN a.attendedBy g
JOIN a.accessTo ca
join ca.heldIn c
WHERE g.id = :groupId
""")
    List<GroupLessonDto> getFullSchedule(@Param("groupId") Long groupId);

}
