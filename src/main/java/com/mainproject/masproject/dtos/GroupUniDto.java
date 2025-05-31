package com.mainproject.masproject.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupUniDto {
    private Long id;
    private String name;
    public GroupUniDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
