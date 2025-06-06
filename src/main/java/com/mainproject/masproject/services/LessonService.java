package com.mainproject.masproject.services;

import com.mainproject.masproject.models.LessonType;
import com.mainproject.masproject.repositories.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;


    List<LessonType> getAllTypes() {
        List<LessonType> list = new ArrayList<>();
        list.addAll(List.of(LessonType.values()));
        return list;
    }

//   void deleteLesson( Long lessonId, DeleteLessonDto)

}
