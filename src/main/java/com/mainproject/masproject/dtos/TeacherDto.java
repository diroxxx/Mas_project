package com.mainproject.masproject.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherDto {
    private int teacherId;
    private String teacherName;

    public TeacherDto(int teacherId, String teacherName) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }
}
