package com.mainproject.masproject.controllers;

import com.mainproject.masproject.dtos.GroupLessonDto;
import com.mainproject.masproject.dtos.GroupUniDto;
import com.mainproject.masproject.repositories.*;
import com.mainproject.masproject.services.AssignmentService;
import com.mainproject.masproject.services.GroupUniService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final GroupUniService groupUniService;
    private final AssignmentService assignmentService;


    private final GroupUniRepository groupUniRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    List<LocalTime> times = new ArrayList<>(List.of(LocalTime.of(8, 30),
            LocalTime.of(10, 15),
            LocalTime.of(12, 15),
            LocalTime.of(14, 0),
            LocalTime.of(15, 45),
            LocalTime.of(17, 30),
            LocalTime.of(19, 0)
    ));

    @GetMapping()
    public String schedule(@RequestParam(required = false) Long groupId, Model model) {
        //do zmiany jesli dodam repozytoria
        model.addAttribute("subjects", subjectRepository.findAll());

        List<TeacherRepository.TeacherProjection> allTeachers = teacherRepository.findAllTeachers();
//        System.out.println(allTeachers.size());
        model.addAttribute("teachers", allTeachers);

        List<String> daysOfWeek = new ArrayList<>(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
        model.addAttribute("daysOfWeek", daysOfWeek);

        model.addAttribute("timeSlots", times);

        model.addAttribute("classrooms", classroomRepository.getAllClassrooms());

        model.addAttribute("typesOfLecture", assignmentService.getAllTimeSlots());


        List<GroupUniDto> allGroups = groupUniService.getAllGroups();
        model.addAttribute("groups", allGroups);
        model.addAttribute("selectedGroupId", groupId);

        if (groupId != null) {
            List<GroupLessonDto> lessons = groupUniRepository.getFullSchedule(groupId);
            Map<String, List<GroupLessonDto>> lessonsByDay = lessons.stream()
                    .collect(Collectors.groupingBy(GroupLessonDto::getDayOfWeek));

            System.out.println(lessonsByDay.size());
            model.addAttribute("lessonsByDay", lessonsByDay);
        }

        return "editSchedule";
    }

    @PostMapping("/getScheduleForGroup")
    public String submitSchedule(@RequestParam Long groupId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("groupId", groupId);
        return "redirect:/schedule";
    }


//    @PostMapping("/deleteLesson")
//    public String deleteLesson(@RequestParam Long lessonId, @RequestParam Long groupId, RedirectAttributes redirectAttributes) {
//        lessonService.deleteLesson(lessonId);
//        redirectAttributes.addAttribute("groupId", groupId);
//        return "redirect:/schedule";
//    }



}
