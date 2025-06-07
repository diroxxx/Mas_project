package com.mainproject.masproject.dtos;

import com.mainproject.masproject.models.Assignment;
import com.mainproject.masproject.models.LessonType;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@ToString
public class GroupLessonDto {
    private final Long subjectId;
    private final String subjectName;

    private final Long teacherId;
    private final String teacherName;

    private final LessonType lessonType;

    private final int groupCapacity;

    private final String dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    private final Long classId;
    private final String roomNumber;
    private final int roomCapacity;
    private final Long lessonId;


    public GroupLessonDto(Long subjectId, String subjectName,
                          Long teacherId, String teacherName,
                          LessonType lessonType, int groupCapacity,
                          String dayOfWeek, LocalTime startTime,
                          String roomNumber,Long classId, int roomCapacity,
                          Long lessonId) {
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
        this.roomCapacity = roomCapacity;
        this.lessonId = lessonId;
        this.endTime = startTime.plusMinutes((long) Assignment.getDurationInMinutes());
    }
}
