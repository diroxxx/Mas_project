package com.mainproject.masproject.services;

import com.mainproject.masproject.dtos.CreateLessonDto;
import com.mainproject.masproject.dtos.DeleteLessonDto;
import com.mainproject.masproject.dtos.EditLesson;
import com.mainproject.masproject.models.*;
import com.mainproject.masproject.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRealizationRepository realizationRepository;
    private final GroupUniRepository groupUniRepository;
    private final AssignmentRepository assignmentRepository;
    private final ClassroomRepository classroomRepository;
    private final ClassActivityRepository classActivityRepository;


    List<LessonType> getAllTypes() {
        List<LessonType> list = new ArrayList<>();
        list.addAll(List.of(LessonType.values()));
        return list;
    }
    public void validateCommonLessonData(
            Long teacherId,
            Long classroomId,
            String dayOfWeek,
            LocalTime startTime,
            Long assignmentId,
            BindingResult bindingResult
    ) {
        if (!teacherRepository.isTeacherAvailable(teacherId, dayOfWeek, startTime, assignmentId)) {
            bindingResult.rejectValue("teacherId", "teacher.unavailable", "Nauczyciel nie jest dostępny w tym terminie");
        }

        if (!classroomRepository.isClassroomAvailable(classroomId, dayOfWeek, startTime, assignmentId)) {
            bindingResult.rejectValue("classroomId", "classroom.occupied", "Sala jest już zajęta");
        }
    }


    @Transactional
    public void editLesson(EditLesson editLesson) {

        System.out.println(editLesson);

        Lesson lesson = lessonRepository.findById(editLesson.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Teacher teacher = teacherRepository.findById(editLesson.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        SubjectRealization realization = realizationRepository.findBySubjectId(editLesson.getSubjectId())
                .orElseThrow(() -> new RuntimeException("SubjectRealization not found"));

        Assignment assignment = assignmentRepository.findByLessonId(
                        editLesson.getLessonId(),
                        editLesson.getOldStartTime(),
                        editLesson.getOldDayOfWeek())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Classroom classroom = classroomRepository.findById(editLesson.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        GroupUni groupUni = groupUniRepository.findById(editLesson.getGroupUniId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        ClassActivity activity = classActivityRepository.findClassActivity(editLesson.getClassroomId(), assignment.getId())
                        .orElseThrow(() -> new RuntimeException("ca not found"));


        lesson.setTaughtBy(teacher);
        lesson.setType(editLesson.getTypeOfLecture());

        assignment.setStartTime(editLesson.getStartTime());
        assignment.setDayOfWeek(editLesson.getDayOfWeek());
        assignment.setAttendedBy(groupUni);


        activity.setHeldIn(classroom);

        classActivityRepository.save(activity);
        assignmentRepository.save(assignment);
        lessonRepository.save(lesson);

    }


    @Transactional
    public void createLesson(CreateLessonDto createLessonDto) {

        GroupUni group = groupUniRepository.findById(createLessonDto.getGroupUniId())
                .orElseThrow(() -> new RuntimeException("Grupa nie istnieje"));

        Teacher teacher = teacherRepository.findById(createLessonDto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Nauczyciel nie istnieje"));

        SubjectRealization realization = realizationRepository.findBySubjectId(createLessonDto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Realizacja przedmiotu nie istnieje"));

        Classroom classroom = classroomRepository.findById(createLessonDto.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Sala nie istnieje"));

        Lesson lesson = new Lesson();
        lesson.setType(createLessonDto.getTypeOfLecture());
        lesson.setTaughtBy(teacher);
        lesson.setBasedOn(realization);

        lessonRepository.save(lesson);

        Assignment assignment = new Assignment();
        assignment.setDayOfWeek(createLessonDto.getDayOfWeek());
        assignment.setStartTime(createLessonDto.getStartTime());
        assignment.setScheduledBy(lesson);
        assignment.setAttendedBy(group);

        assignmentRepository.save(assignment);

        ClassActivity ca = new ClassActivity();
        ca.setCreatedAt(LocalDate.now());
        ca.setNote("Utworzono automatycznie");
        ca.setHeldIn(classroom);
        ca.setAccessedBy(assignment);

        classActivityRepository.save(ca);
    }

    @Transactional
  public void deleteLesson(DeleteLessonDto deleteLessonDto){
        Lesson lesson = lessonRepository.findById(deleteLessonDto.getLessonIdToDelete())
                .orElseThrow(() -> new RuntimeException("Lekcja nie znaleziona"));


        Assignment assignment = assignmentRepository.findByLessonId(deleteLessonDto.getClassroomIdToDelete(), deleteLessonDto.getStartTimeToDelete(), deleteLessonDto.getDayOfWeekToDelete()).orElseThrow();

            ClassActivity activities = classActivityRepository.findClassActivity(deleteLessonDto.getClassroomIdToDelete(), assignment.getId()).orElseThrow();
            classActivityRepository.delete(activities);


        assignmentRepository.delete(assignment);


        lessonRepository.delete(lesson);
   }

}
