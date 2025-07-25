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
    private final GroupUniRepository guideRepository;
    private final LessonService lessonService;

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


    @ModelAttribute("createLesson")
    public CreateLessonDto defaultCreateLesson() {
        return new CreateLessonDto();
    }

    @ModelAttribute("lessonForm")
    public EditLesson defaultLessonForm() {
        return new EditLesson();
    }

    @ModelAttribute("deleteLesson")
    public DeleteLessonDto defaultDeleteLesson() {
        return new DeleteLessonDto();
    }

    @GetMapping()
    public String schedule(@RequestParam(required = false) Long groupId, Model model) {

        model.addAttribute("timeSlots", times);
        model.addAttribute("subjects", subjectRepository.findAll());
        model.addAttribute("teachers", teacherRepository.getAllTeachers());
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

            ((CreateLessonDto) model.getAttribute("createLesson")).setGroupUniId(groupId);
            ((EditLesson)     model.getAttribute("lessonForm")).setGroupUniId(groupId);

        }

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
            Model model, RedirectAttributes redirectAttributes
    ) {

        lessonService.validateCommonLessonData(
                lessonForm.getTeacherId(),
                lessonForm.getClassroomId(),
                lessonForm.getDayOfWeek(),
                lessonForm.getStartTime(),
                lessonForm.getAssignmentId(),
                bindingResult);

//      if (!groupUniRepository.isGroupUniAvailableWithAssignment(
//                lessonForm.getGroupUniId(),
//                lessonForm.getDayOfWeek(),
//                lessonForm.getStartTime(),
//                lessonForm.getAssignmentId()
//                )){
//          bindingResult.reject("groupBusyEdit", "Group has different classes in this time");
//
//
//      }


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
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.lessonForm", bindingResult);
            redirectAttributes.addFlashAttribute("lessonForm", lessonForm);
            redirectAttributes.addAttribute("groupId", lessonForm.getGroupUniId());
            return "redirect:/schedule";
        }

        lessonService.editLesson(lessonForm);
        model.addAttribute("groupId", lessonForm.getGroupUniId());

        return "created-edited-Lesson-page";
    }


    @PostMapping("/createLesson")
    public String createLesson(
            @Valid @ModelAttribute("createLesson") CreateLessonDto createLessonDto,
            BindingResult bindingResult,
            Model model,RedirectAttributes redirectAttributes
    ) {


        if (!groupUniRepository.isGroupUniAvailable(
                createLessonDto.getGroupUniId(),
                createLessonDto.getDayOfWeek(),
                createLessonDto.getStartTime())){
            bindingResult.reject("GroupIsBusy", "The group has classes on this date");

        }
        lessonService.validateCommonLessonData(
                createLessonDto.getTeacherId(),
                createLessonDto.getClassroomId(),
                createLessonDto.getDayOfWeek(),
                createLessonDto.getStartTime(),
                createLessonDto.getAssignmentId(),
                bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createLesson", bindingResult);
            redirectAttributes.addFlashAttribute("createLesson", createLessonDto);
            redirectAttributes.addAttribute("groupId", createLessonDto.getGroupUniId());
            return "redirect:/schedule";
        }
        lessonService.createLesson(createLessonDto);
        model.addAttribute("groupId", createLessonDto.getGroupUniId());

        return "created-edited-Lesson-page";
    }

    @PostMapping("/deleteLesson")
    public String deleteLesson(@Valid @ModelAttribute("deleteLesson") DeleteLessonDto deleteLessonDto, Model model) {
        lessonService.deleteLesson(deleteLessonDto);
        model.addAttribute("groupId", deleteLessonDto.getGroupId());
        return "deletedLesson-page";
    }

}
