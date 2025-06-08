package com.mainproject.masproject.services;

import com.mainproject.masproject.dtos.CreateLessonDto;
import com.mainproject.masproject.dtos.DeleteLessonDto;
import com.mainproject.masproject.dtos.EditLesson;
import com.mainproject.masproject.models.*;
import com.mainproject.masproject.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
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
    private final SubjectRepository subjectRepository;
    private final SubjectRealizationRepository subjectRealizationRepository;


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

        SubjectRealization subjectRealization = realizationRepository.findBySubjectId(editLesson.getSubjectId())
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

        ClassActivity activity = classActivityRepository.findClassActivity(editLesson.getOldClassroomId(), assignment.getId())
                        .orElseThrow(() -> new RuntimeException("ca not found"));

        Subject subject = subjectRepository.findById(editLesson.getSubjectId()).orElseThrow(() -> new RuntimeException("Subject not found"));
        System.out.println("new subject: " + subject.getId() );
        System.out.println("old: " + editLesson.getOldSubjectId());
        if (!editLesson.getOldSubjectId().equals(subject.getId())) {
            subjectRealization.setIncludedBy(subject);
            lesson.setBasedOn(subjectRealization);
        }
        if (!editLesson.getOldTeacherId().equals(editLesson.getTeacherId())) {
            lesson.setTaughtBy(teacher);

        }
        if (!editLesson.getOldClassroomId().equals(editLesson.getClassroomId())) {
            activity.setHeldIn(classroom);
        }
        if (!editLesson.getOldStartTime().equals(editLesson.getStartTime())) {
            assignment.setStartTime(editLesson.getStartTime());
        }
        if (!editLesson.getOldDayOfWeek().equals(editLesson.getDayOfWeek())) {
            assignment.setDayOfWeek(editLesson.getDayOfWeek());
        }
        if(!editLesson.getOldTypeOfLecture().equals(editLesson.getTypeOfLecture())) {
            lesson.setType(editLesson.getTypeOfLecture());
        }

        subjectRealizationRepository.save(subjectRealization);

//        classActivityRepository.save(activity);
//        assignmentRepository.save(assignment);
//        lessonRepository.save(lesson);

    }


    @Transactional
    public void createLesson(CreateLessonDto createLessonDto) {

        System.out.println(createLessonDto);
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
        System.out.println(deleteLessonDto);

        Lesson lesson = lessonRepository.findById(deleteLessonDto.getLessonIdToDelete())
                .orElseThrow(() -> new RuntimeException("Lekcja nie znaleziona"));


        Assignment assignment = assignmentRepository
                .findById(deleteLessonDto.getAssignmentToDelete())
                .orElseThrow(() -> new RuntimeException("Assignment nieznaleziona"));
        System.out.println(assignment.getId());   // powinno wypisać 2


        ClassActivity activity = classActivityRepository.findClassActivity(
                deleteLessonDto.getClassroomIdToDelete(),
                assignment.getId())
                .orElseThrow(() -> new RuntimeException("nie znaleziona a"));
        System.out.println(activity.getId());

        Classroom classroom = classroomRepository.
                findById(deleteLessonDto.getClassroomIdToDelete())
                .orElseThrow(() -> new RuntimeException("Sala nie znaleziona"));
        System.out.println(classroom.getId());

//        if (activity.getHeldIn() != null)
//            activity.getHeldIn().getHolds().remove(activity);
//        if (assignment.getAccessTo() != null)
//            assignment.getAccessTo().remove(activity);
//        if (lesson.getScheduledAs() != null)
//            lesson.getScheduledAs().remove(assignment);
//
//        activity.setHeldIn(null);
//        activity.setAccessedBy(null);
//
//        assignment.setScheduledBy(null);
//        assignment.setAttendedBy(null);
//
//        if (lesson.getTaughtBy() != null)
//            lesson.getTaughtBy().getTeaches().remove(lesson);
//        if (lesson.getBasedOn() != null)
//            lesson.getBasedOn().getBasedFor().remove(lesson);

        // Usuwanie
//        classActivityRepository.delete(activity);
//        assignmentRepository.delete(assignment);
        lessonRepository.delete(lesson);

//        lessonRepository.flush();

    }

}
