package com.mainproject.masproject.services;

import com.mainproject.masproject.dtos.GroupUniDto;
import com.mainproject.masproject.repositories.GroupUniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
