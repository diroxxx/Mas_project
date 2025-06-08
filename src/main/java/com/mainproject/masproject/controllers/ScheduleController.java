package com.mainproject.masproject.controllers;

import com.mainproject.masproject.dtos.*;
import com.mainproject.masproject.models.Assignment;
import com.mainproject.masproject.models.Lesson;
import com.mainproject.masproject.models.Teacher;
import com.mainproject.masproject.repositories.*;
import com.mainproject.masproject.services.AssignmentService;
import com.mainproject.masproject.services.GroupUniService;
import com.mainproject.masproject.services.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.*;
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
    private final AssignmentRepository assignmentRepository;

   private static final List<LocalTime> times = new ArrayList<>(List.of(LocalTime.of(8, 30),
            LocalTime.of(10, 15),
            LocalTime.of(12, 15),
            LocalTime.of(14, 0),
            LocalTime.of(15, 45),
            LocalTime.of(17, 30),
            LocalTime.of(19, 0)
    ));
   private static final List<String> daysOfWeek = new ArrayList<>(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
    private final LessonService lessonService;


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

        EditLesson editLesson = new EditLesson();
        CreateLessonDto createLessonDto = new CreateLessonDto();
        DeleteLessonDto deleteLessonDto = new DeleteLessonDto();
        if (groupId != null) {

            List<GroupLessonDto> lessons = groupUniRepository.getFullSchedule(groupId);
            Map<String, List<GroupLessonDto>> lessonsByDay = lessons.stream()
                    .collect(Collectors.groupingBy(GroupLessonDto::getDayOfWeek));

            model.addAttribute("lessonsByDay", lessonsByDay);
            editLesson.setGroupUniId(groupId);
            createLessonDto.setGroupUniId(groupId);
        }

        model.addAttribute("lessonForm", editLesson);
        model.addAttribute("createLesson", createLessonDto);
        model.addAttribute("deleteLesson", deleteLessonDto);
        return "editSchedule";
    }


    @PostMapping("/getScheduleForGroup")
    public String submitSchedule(@RequestParam Long groupId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("groupId", groupId);
        return "redirect:/schedule";
    }

    @PostMapping("/editLesson")
    public String editLesson(
            @Valid @ModelAttribute("lessonForm") EditLesson lessonForm,
            BindingResult bindingResult,
            Model model
    ) {

        System.out.println(lessonForm);
        if (!teacherRepository.isTeacherAvailable(
                lessonForm.getTeacherId(),
                lessonForm.getDayOfWeek(),
                lessonForm.getStartTime(),
                lessonForm.getAssignmentId())) {

            bindingResult.rejectValue(
                    "teacherId",
                    "teacher.unavailable",
                    "Nauczyciel nie jest dostępny w tym terminie"
            );
        }

        if (!classroomRepository.isClassroomAvailable(
                lessonForm.getClassroomId(),
                lessonForm.getDayOfWeek(),
                lessonForm.getStartTime(),
                lessonForm.getAssignmentId())) {

            bindingResult.rejectValue(
                    "classroomId",
                    "classroom.occupied",
                    "Sala jest już zajęta"
            );
        }

        if (
                lessonForm.getStartTime().equals(lessonForm.getOldStartTime()) &&
                        lessonForm.getDayOfWeek().equals(lessonForm.getOldDayOfWeek()) &&
                        lessonForm.getTeacherId().equals(lessonForm.getOldTeacherId()) &&
                        lessonForm.getClassroomId().equals(lessonForm.getOldClassroomId()) &&
                        lessonForm.getSubjectId().equals(lessonForm.getOldSubjectId()) &&
                        lessonForm.getTypeOfLecture().equals(lessonForm.getOldTypeOfLecture())
        ) {
            bindingResult.reject("noChanges", "Nie dokonano żadnych zmian w lekcji.");
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("showForm", true);


            model.addAttribute("teachers", teacherRepository.findAllTeachers());
            model.addAttribute("subjects", subjectRepository.findAll());
            model.addAttribute("timeSlots", times);
            model.addAttribute("classrooms", classroomRepository.getAllClassrooms());
            model.addAttribute("daysOfWeek", daysOfWeek);
            model.addAttribute("typesOfLecture", lessonRepository.getAllTypes());

            List<GroupLessonDto> lessons = groupUniRepository.getFullSchedule(lessonForm.getGroupUniId());
            model.addAttribute("lessonsByDay", lessons.stream()
                    .collect(Collectors.groupingBy(GroupLessonDto::getDayOfWeek)));

            return "editSchedule";
        }

        lessonService.editLesson(lessonForm);
        return "updatedLesson-page";
    }


    @PostMapping("/createLesson")
    public String createLesson(
            @Valid @ModelAttribute("createLesson") CreateLessonDto createLessonDto,
            BindingResult bindingResult,
            Model model
    ) {
        lessonService.validateCommonLessonData(
                createLessonDto.getTeacherId(),
                createLessonDto.getClassroomId(),
                createLessonDto.getDayOfWeek(),
                createLessonDto.getStartTime(),
                createLessonDto.getAssignmentId(),
                bindingResult);



        if (bindingResult.hasErrors()) {
            model.addAttribute("showForm", true);


            model.addAttribute("teachers", teacherRepository.findAllTeachers());
            model.addAttribute("subjects", subjectRepository.findAll());
            model.addAttribute("timeSlots", times);
            model.addAttribute("classrooms", classroomRepository.getAllClassrooms());
            model.addAttribute("daysOfWeek", daysOfWeek);
            model.addAttribute("typesOfLecture", lessonRepository.getAllTypes());

            List<GroupLessonDto> lessons = groupUniRepository.getFullSchedule(createLessonDto.getGroupUniId());
            model.addAttribute("lessonsByDay", lessons.stream()
                    .collect(Collectors.groupingBy(GroupLessonDto::getDayOfWeek)));

            return "editSchedule";
        }
        lessonService.createLesson(createLessonDto);
        return "createdLesson-page";
    }

    @GetMapping("/editedSuccesful")
    public String editSuccesfull() {
        return "updatedLesson-page";
    }


    @PostMapping("/deleteLesson")
    public String deleteLesson(@Valid @ModelAttribute("deleteLesson") DeleteLessonDto deleteLessonDto, RedirectAttributes redirectAttributes) {
        try {
            lessonService.deleteLesson(deleteLessonDto);
            redirectAttributes.addFlashAttribute("successMessage", "Usunięto lekcję.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd przy usuwaniu: " + e.getMessage());
        }
        return "deletedLesson-page";
    }





}
