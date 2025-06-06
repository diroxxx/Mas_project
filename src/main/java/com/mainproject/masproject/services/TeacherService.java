package com.mainproject.masproject.services;

import com.mainproject.masproject.dtos.TeacherDto;
import com.mainproject.masproject.models.Teacher;
import com.mainproject.masproject.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;

    boolean isTeacherAvailable(Long id, String day, LocalTime startTime) {
        if (id == null || startTime == null || day == null) {
            throw new NullPointerException();
        }
        Optional<Teacher> teacher = teacherRepository.existTeacherInGivenTime(id, day, startTime);
        if (teacher.isPresent()) {
            return true;
        }
        return false;
    }


//    public List<TeacherRepository.TeacherProjection> getAllTeachers()
//    {
//
//        teacherRepository.findAllTeachers();
//    }
}
