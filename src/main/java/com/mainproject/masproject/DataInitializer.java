package com.mainproject.masproject;

import com.mainproject.masproject.models.*;
import com.mainproject.masproject.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final PersonRepository personRepository;
    private final EmployeeRepository employeeRepository;
    private final TeacherRepository teacherRepository;
    private final GroupUniRepository groupUniRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectRealizationRepository subjectRealizationRepository;
    private final SemesterRepository semesterRepository;
    private final LessonRepository lessonRepository;
    private final AssignmentRepository assignmentRepository;
    private final ClassroomRepository classroomRepository;
    private final ClassActivityRepository classActivityRepository;
    @EventListener
    public void atStart(ContextRefreshedEvent event) {

        if (personRepository.count() == 0 ) {


            try {


                Person person = Person.builder()
                        .pesel("12312312311")
                        .firstName("asd")
                        .lastName("asdasd")
                        .email("che-dom@wp.pl")
                        .phoneNumber(123123123)
                        .build();
                personRepository.save(person);

                Student student = new Student();
                student.setIndex("s27354");
                student.setPersonStudent(person);


                Employee employee = Employee.builder()
                        .hireDate(LocalDate.of(2025, 3, 12))
                        .personEmployee(person)
                        .build();
                employeeRepository.save(employee);
                Teacher teacher = Teacher.builder()
                        .academicTitles(List.of("prof", "mgr"))
                        .employeeTeacher(employee)
                        .build();
                teacherRepository.save(teacher);

                GroupUni groupUni = new GroupUni();
                groupUni.setName("25c_s6");
                groupUni.setCapacity(20);
                groupUniRepository.save(groupUni);

                student.getSubmitTo().add(groupUni);
                groupUni.getSubmitedBy().add(student);


                person.setPersonEmployee(employee);
                person.setStudent(student);
                employee.setTeacher(teacher);

                studentRepository.save(student);
                employeeRepository.save(employee);
                personRepository.save(person);

                Subject math = new Subject();
                math.setName("math");
                math.setCode("mt");
                math.setEcts(4);

                Syllabus mathSyllabus = new Syllabus();
                mathSyllabus.setDescription("description");
                mathSyllabus.setRequirements("requirements");

                math.setDefines(mathSyllabus);
                mathSyllabus.setDefinedBy(math);

                subjectRepository.save(math);

                Semester semester1 = new Semester();
                semester1.setCode("2025/2026");
                semester1.setNumber(1);
                semester1.setStartDate(LocalDate.of(2025, 3, 12));
                semester1.setEndDate(LocalDate.of(2025, 9, 12));

                Subject english = new Subject();
                math.setName("english");
                math.setCode("eng");
                math.setEcts(2);
//
                SubjectRealization subjectRealization1 = new SubjectRealization();
                subjectRealization1.setMode(ModeType.STATIONARY);
                subjectRealization1.setLanguage(LangType.ENGLISH);

                subjectRepository.save(english);
                semesterRepository.save(semester1);


                subjectRealization1.setOfferedBy(semester1);
                subjectRealization1.setIncludedBy(english);
                english.setIncludedIn(Set.of(subjectRealization1));
                semester1.setOffers(Set.of(subjectRealization1));
                subjectRealizationRepository.save(subjectRealization1);

//                //zapisanie grupy do studenta
                GroupUni groupUni2 = new GroupUni();
                groupUni2.setName("25c_s7");
                groupUni2.setCapacity(20);
                groupUniRepository.save(groupUni2);
                groupUni2.setSubmitedBy(Set.of(student));

                student.getSubmitTo().add(groupUni2);
                studentRepository.save(student);

                //daodanie lekcji
                Lesson lesson1 = new Lesson();
                lesson1.setName("lesson1");
                lesson1.setType(LessonType.LECTURE);
                lesson1.setBasedOn(subjectRealization1);
                lesson1.setTaughtBy(teacher);


                Lesson lesson2 = new Lesson();
                lesson2.setName("lesson2");
                lesson2.setType(LessonType.EXERCISE);
                lesson2.setBasedOn(subjectRealization1);
                lesson2.setTaughtBy(teacher);


                lessonRepository.saveAll(Set.of(lesson1, lesson2));
//
                subjectRealization1.setBasedFor(Set.of(lesson1, lesson2));
                subjectRealizationRepository.save(subjectRealization1);
                teacher.setTeaches(Set.of(lesson1, lesson2));
                teacherRepository.save(teacher);


                Assignment assignment = new Assignment();
                assignment.setDayOfWeek("FRIDAY");
                assignment.setStartTime(LocalTime.of(10, 0));
                assignment.setScheduledBy(lesson1);
                assignment.setAttendedBy(groupUni2);


                Assignment assignment2 = new Assignment();
                assignment2.setDayOfWeek("FRIDAY");
                assignment2.setStartTime(LocalTime.of(12, 0));
                assignment2.setScheduledBy(lesson2);
                assignment2.setAttendedBy(groupUni2);

                assignmentRepository.saveAll(Set.of(assignment, assignment2));

                Classroom classroom = new Classroom();
                classroom.setCapacity(20);
                classroom.setRoomNumber("123_c");
                classroomRepository.save(classroom);


                ClassActivity classActivity = new ClassActivity();
                classActivity.setOccupiedDate(LocalDate.of(2025, 3, 12));
                classActivity.setStatus(ActivityStatus.SCHEDULED);
                classActivity.setAccessedBy(assignment);
                classActivity.setHeldIn(classroom);


                ClassActivity classActivity2 = new ClassActivity();
                classActivity2.setOccupiedDate(LocalDate.of(2025, 3, 12));
                classActivity2.setStatus(ActivityStatus.SCHEDULED);
                classActivity2.setAccessedBy(assignment2);
                classActivity2.setHeldIn(classroom);


                classActivityRepository.saveAll(Set.of(classActivity, classActivity2));




            }catch (Exception e){
                e.printStackTrace();
            }

        }



    }



}
