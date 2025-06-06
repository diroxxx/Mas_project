package com.mainproject.masproject.services;

import com.mainproject.masproject.models.Classroom;
import com.mainproject.masproject.repositories.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;


    boolean isClassroomAvailable( Long classId, String day, LocalTime startTime) {
        Optional<Classroom> conflictingClassroom = classroomRepository.findConflictingClassroom(classId, day, startTime);
        return conflictingClassroom.isPresent();
    }
}
