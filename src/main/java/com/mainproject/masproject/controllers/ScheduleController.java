package com.mainproject.masproject.controllers;

import com.mainproject.masproject.dtos.*;
import com.mainproject.masproject.repositories.*;
import com.mainproject.masproject.services.GroupUniService;
import com.mainproject.masproject.services.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final GroupUniService groupUniService;

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
    private static final Map<String, Integer> DAY_ORDER =
            IntStream.range(0, daysOfWeek.size())
                    .boxed()
                    .collect(Collectors.toMap(daysOfWeek::get, i -> i));

    private final LessonService lessonService;


    @GetMapping()
    public String schedule(@RequestParam(required = false) Long groupId, Model model) {

        model.addAttribute("timeSlots", times);
        model.addAttribute("subjects", subjectRepository.findAll());
        List<TeacherRepository.TeacherProjection> allTeachers = teacherRepository.getAllTeachers();
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

            Comparator<GroupLessonDto> LESSON_COMPARATOR =
                    Comparator.comparing(
                                    (GroupLessonDto l) -> DAY_ORDER.getOrDefault(l.getDayOfWeek(), Integer.MAX_VALUE) )
                            .thenComparing(GroupLessonDto::getStartTime);

            List<GroupLessonDto> sortedLessons = lessons.stream()
                    .sorted(LESSON_COMPARATOR)
                    .toList();

            Map<String, List<GroupLessonDto>> lessonsByDaySorted =
                    sortedLessons.stream()
                            .collect(Collectors.groupingBy(
                                    GroupLessonDto::getDayOfWeek,
                                    LinkedHashMap::new,
                                    Collectors.toList()));




            model.addAttribute("lessonsByDay", lessonsByDaySorted);
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

//        System.out.println(lessonForm);
//        if (!teacherRepository.isTeacherAvailable(
//                lessonForm.getTeacherId(),
//                lessonForm.getDayOfWeek(),
//                lessonForm.getStartTime(),
//                lessonForm.getAssignmentId())) {
//
//            bindingResult.rejectValue(
//                    "teacherId",
//                    "teacher.unavailable",
//                    "The teacher is not available on this date\n"
//            );
//        }
//
//        if (!classroomRepository.isClassroomAvailable(
//                lessonForm.getClassroomId(),
//                lessonForm.getDayOfWeek(),
//                lessonForm.getStartTime(),
//                lessonForm.getAssignmentId())) {
//
//            bindingResult.rejectValue(
//                    "classroomId",
//                    "classroom.occupied",
//                    "The classroom is already occupied"
//            );
//        }
        lessonService.validateCommonLessonData(
                lessonForm.getTeacherId(),
                lessonForm.getClassroomId(),
                lessonForm.getDayOfWeek(),
                lessonForm.getStartTime(),
                lessonForm.getAssignmentId(),
                bindingResult);

        if (
                lessonForm.getStartTime().equals(lessonForm.getOldStartTime()) &&
                        lessonForm.getDayOfWeek().equals(lessonForm.getOldDayOfWeek()) &&
                        lessonForm.getTeacherId().equals(lessonForm.getOldTeacherId()) &&
                        lessonForm.getClassroomId().equals(lessonForm.getOldClassroomId()) &&
                        lessonForm.getSubjectId().equals(lessonForm.getOldSubjectId()) &&
                        lessonForm.getTypeOfLecture().equals(lessonForm.getOldTypeOfLecture())
        ) {
            bindingResult.reject("noChanges", "No changes were made to the lesson");
        }


        if (bindingResult.hasErrors()) {
//            model.addAttribute("showForm", true);

            model.addAttribute("teachers", teacherRepository.getAllTeachers());
            model.addAttribute("subjects", subjectRepository.findAll());
            model.addAttribute("timeSlots", times);
            model.addAttribute("classrooms", classroomRepository.getAllClassrooms());
            model.addAttribute("daysOfWeek", daysOfWeek);
            model.addAttribute("typesOfLecture", lessonRepository.getAllTypes());

            List<GroupLessonDto> lessons = groupUniRepository.getFullSchedule(lessonForm.getGroupUniId());
            model.addAttribute("lessonsByDay", lessons.stream()
                    .collect(Collectors.groupingBy(GroupLessonDto::getDayOfWeek)));

            return "editSchedule";
//            return "redirect:/schedule";
        }

        lessonService.editLesson(lessonForm);
        return "created-edited-Lesson-page";
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

            model.addAttribute("teachers", teacherRepository.getAllTeachers());
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
        return "created-edited-Lesson-page";
    }

    @GetMapping("/editedSuccesful")
    public String editSuccesfull() {
        return "created-edited-Lesson-page";
    }


    @PostMapping("/deleteLesson")
    public String deleteLesson(@Valid @ModelAttribute("deleteLesson") DeleteLessonDto deleteLessonDto) {
        lessonService.deleteLesson(deleteLessonDto);
        return "deletedLesson-page";
    }


}
