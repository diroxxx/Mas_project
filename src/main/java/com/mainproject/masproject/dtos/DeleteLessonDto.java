package com.mainproject.masproject.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class DeleteLessonDto {
    @NotNull
    Long lessonIdToDelete;
    @NotNull
    LocalTime startTimeToDelete;
    @NotNull
    String dayOfWeekToDelete;
    @NotNull
    Long classroomIdToDelete;
    @NotNull
    Long assignmentToDelete;
    @NotNull
    Long groupId;
}
