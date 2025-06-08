package com.mainproject.masproject.dtos;

import com.mainproject.masproject.models.LessonType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class CreateLessonDto {

    private Long groupUniId;
    private Long lessonId;
    private Long subjectId;
    private Long teacherId;
    private String dayOfWeek;
    private String oldDayOfWeek;
    private LocalTime startTime;
    private Long classroomId;
    private LessonType typeOfLecture;
    private Long assignmentId;
}
