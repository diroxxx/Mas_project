package com.mainproject.masproject.dtos;

import com.mainproject.masproject.models.Assignment;
import com.mainproject.masproject.models.LessonType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@ToString
public class GroupLessonDto {
    private Long subjectId;
    private String subjectName;

    private Long teacherId;
    private String teacherName;

    private LessonType lessonType;

    private int groupCapacity;

    private String dayOfWeek;
    private LocalTime startTime;
    private final LocalTime endTime;

    private Long classId;
    private String roomNumber;


    public GroupLessonDto(Long subjectId, String subjectName, Long teacherId, String teacherName,
                          LessonType lessonType, int groupCapacity, String dayOfWeek, LocalTime startTime, String roomNumber,Long classId) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.lessonType = lessonType;
        this.groupCapacity = groupCapacity;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.roomNumber = roomNumber;
        this.classId = classId;
        this.endTime = startTime.plusMinutes((long) Assignment.getDurationInMinutes());

//        this.duration= Assignment.getDurationInMinutes();
    }
}
