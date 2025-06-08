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
    private String oldDayOfWeek;
    private LocalTime startTime;
    private LocalTime oldStartTime;
    private Long classroomId;
    private LessonType typeOfLecture;
    private Long assignmentId;
//    private String code;

    private Long oldTeacherId;
    private Long oldClassroomId;
    private Long oldSubjectId;
    private LessonType oldTypeOfLecture;


}
