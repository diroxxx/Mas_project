package com.mainproject.masproject.controllers;

import com.mainproject.masproject.dtos.EditLesson;
import com.mainproject.masproject.dtos.GroupLessonDto;
import com.mainproject.masproject.dtos.GroupUniDto;
import com.mainproject.masproject.models.Teacher;
import com.mainproject.masproject.repositories.*;
import com.mainproject.masproject.services.AssignmentService;
import com.mainproject.masproject.services.GroupUniService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private final LessonRepository lessonRepository;

   private static final List<LocalTime> times = new ArrayList<>(List.of(LocalTime.of(8, 30),
            LocalTime.of(10, 15),
            LocalTime.of(12, 15),
            LocalTime.of(14, 0),
            LocalTime.of(15, 45),
            LocalTime.of(17, 30),
            LocalTime.of(19, 0)
    ));
   private static final List<String> daysOfWeek = new ArrayList<>(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));


    @GetMapping()
    public String schedule(@RequestParam(required = false) Long groupId, Model model) {

        model.addAttribute("timeSlots", times);
        //do zmiany jesli dodam repozytoria
        model.addAttribute("subjects", subjectRepository.findAll());
        List<TeacherRepository.TeacherProjection> allTeachers = teacherRepository.findAllTeachers();
        model.addAttribute("teachers", allTeachers);
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("classrooms", classroomRepository.getAllClassrooms());
        model.addAttribute("typesOfLecture", lessonRepository.getAllTypes());


        List<GroupUniDto> allGroups = groupUniService.getAllGroups();
        model.addAttribute("groups", allGroups);
        model.addAttribute("selectedGroupId", groupId);

        if (groupId != null) {
            List<GroupLessonDto> lessons = groupUniRepository.getFullSchedule(groupId);
            Map<String, List<GroupLessonDto>> lessonsByDay = lessons.stream()
                    .collect(Collectors.groupingBy(GroupLessonDto::getDayOfWeek));

            model.addAttribute("lessonsByDay", lessonsByDay);

            //przekazanie obiektu do form
            EditLesson editLesson = new EditLesson();
            editLesson.setGroupUniId(groupId);
            model.addAttribute("lessonForm", editLesson);
        }



        return "editSchedule";
    }


    @PostMapping("/getScheduleForGroup")
    public String submitSchedule(@RequestParam Long groupId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("groupId", groupId);
        return "redirect:/schedule";
    }


//    @PostMapping("/editLesson")
//    public String editLesson(@ModelAttribute("lessonForm") EditLesson lessonForm, RedirectAttributes redirectAttributes) {
//        System.out.println(lessonForm);
//        return "redirect:/schedule";
//    }


//    @PostMapping("/deleteLesson")
//    public String deleteLesson(@RequestParam Long lessonId, @RequestParam Long groupId, RedirectAttributes redirectAttributes) {
//        lessonService.deleteLesson(lessonId);
//        redirectAttributes.addAttribute("groupId", groupId);
//        return "redirect:/schedule";
//    }



}
