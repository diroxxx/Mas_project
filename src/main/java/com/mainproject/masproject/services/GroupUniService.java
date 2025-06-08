package com.mainproject.masproject.services;

import com.mainproject.masproject.dtos.GroupLessonDto;
import com.mainproject.masproject.dtos.GroupUniDto;
import com.mainproject.masproject.models.GroupUni;
import com.mainproject.masproject.repositories.GroupUniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupUniService {

    private final GroupUniRepository groupUniRepository;

    public List<GroupUniDto> getAllGroups() {
        return groupUniRepository.findAll().stream()
                .map(group -> new GroupUniDto(group.getId(), group.getName()))
                .toList();
    }
}
