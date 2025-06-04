package com.mainproject.masproject.controllers;

import com.mainproject.masproject.dtos.GroupLessonDto;
import com.mainproject.masproject.dtos.GroupUniDto;
import com.mainproject.masproject.repositories.AssignmentRepository;
import com.mainproject.masproject.repositories.GroupUniRepository;
import com.mainproject.masproject.services.GroupUniService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final GroupUniService groupUniService;
    private final AssignmentRepository assignmentRepository;
    private GroupUniRepository groupUniRepository;

    @GetMapping()
    public String schedule(@RequestParam(required = false) Long groupId, Model model) {
        List<GroupUniDto> allGroups = groupUniService.getAllGroups();
        model.addAttribute("groups", allGroups);
        model.addAttribute("selectedGroupId", groupId);

        if (groupId != null) {
            List<GroupLessonDto> lessons = groupUniRepository.getFullSchedule(groupId);
            Map<String, List<GroupLessonDto>> lessonsByDay = lessons.stream()
                    .collect(Collectors.groupingBy(GroupLessonDto::getDayOfWeek));
            model.addAttribute("lessonsByDay", lessonsByDay);
        }

        return "editSchedule";
    }


    @PostMapping("/getScheduleForGroup")
    public String submitSchedule(@RequestParam Long groupId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("groupId", groupId); // doda parametr GET do redirecta
        return "redirect:/schedule";
    }


//    @PostMapping("/deleteLesson")
//    public String deleteLesson(@RequestParam Long lessonId, @RequestParam Long groupId, RedirectAttributes redirectAttributes) {
//        lessonService.deleteLesson(lessonId);
//        redirectAttributes.addAttribute("groupId", groupId);
//        return "redirect:/schedule";
//    }



}
