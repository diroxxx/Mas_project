package com.mainproject.masproject.dtos;

import com.mainproject.masproject.models.LessonType;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EditLesson {
    private Long groupUniId;
    private Long lessonId;
    private Long subjectId;
    private Long teacherId;
    private String dayOfWeek;
    private LocalTime startTime;
    private Long classroomId;
    private LessonType typeOfLecture;




}
