package com.mainproject.masproject.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class DeleteLessonDto {
    Long lessonIdToDelete;
    LocalTime startTimeToDelete;
    String dayOfWeekToDelete;
    Long classroomIdToDelete;
}
