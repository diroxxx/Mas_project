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
import java.util.Map;
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
                        .firstName("Tomek")
                        .lastName("Kowalksi")
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

                Person person2 = Person.builder()
                        .pesel("12312312311")
                        .firstName("Dominika")
                        .lastName("Nowak")
                        .email("che-dom@wp.pl")
                        .phoneNumber(123123123)
                        .build();

                Employee employee2 = Employee.builder()
                        .hireDate(LocalDate.of(2024, 12, 12))
                        .personEmployee(person2)
                        .build();

                Teacher teacher2 = Teacher.builder()
                        .academicTitles(List.of( "mgr"))
                        .employeeTeacher(employee2)
                        .build();
                person2.setPersonEmployee(employee2);
                employee2.setPersonEmployee(person2);
                employee2.setTeacher(teacher2);
                teacher2.setEmployeeTeacher(employee2);
                personRepository.save(person2);
//                employeeRepository.save(employee2);
//                teacherRepository.save(teacher2);



                GroupUni groupUni = new GroupUni();
                groupUni.setName("25c_s6");
                groupUni.setCapacity(21);
                groupUniRepository.save(groupUni);

                student.getSubmitTo().add(groupUni);
                groupUni.getSubmitedBy().put(student.getIndex(), student);


                person.setPersonEmployee(employee);
                person.setStudent(student);
                employee.setTeacher(teacher);

                studentRepository.save(student);
                employeeRepository.save(employee);
                personRepository.save(person);



//                subjectRepository.save(math);

                Semester semester1 = new Semester();
                semester1.setCode("2025/2026");
                semester1.setNumber(1);
                semester1.setStartDate(LocalDate.of(2025, 3, 12));
                semester1.setEndDate(LocalDate.of(2025, 9, 12));

                //prezdmioty math zapisa
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
                semesterRepository.save(semester1);
//
                SubjectRealization subjectRealization1 = new SubjectRealization();
                subjectRealization1.setMode(ModeType.STATIONARY);
                subjectRealization1.setLanguage(LangType.ENGLISH);

                math.getIncludedIn().add(subjectRealization1);
                subjectRealization1.setIncludedBy(math);
                semester1.getOffers().add(subjectRealization1);
                subjectRealization1.setOfferedBy(semester1);

                subjectRealizationRepository.save(subjectRealization1);

                //zap[is english semester 1
                Subject english = new Subject();
                english.setName("english");
                english.setCode("eng");
                english.setEcts(2);
                subjectRepository.save(english);
//
                SubjectRealization subjectRealization2 = new SubjectRealization();
                subjectRealization1.setMode(ModeType.STATIONARY);
                subjectRealization1.setLanguage(LangType.POLISH);

                english.getIncludedIn().add(subjectRealization2);
                subjectRealization2.setIncludedBy(english);
                subjectRealization2.setOfferedBy(semester1);
                semester1.getOffers().add(subjectRealization2);

                subjectRealizationRepository.save(subjectRealization2);


//                //zapisanie grupy do studenta
                GroupUni groupUni2 = new GroupUni();
                groupUni2.setName("25c_s7");
                groupUni2.setCapacity(20);
                groupUni2.addStudent(student);
                groupUniRepository.save(groupUni2);
//                groupUni2.setSubmitedBy(Map.of(student.getIndex(), student));


//                student.getSubmitTo().add(groupUni2);
//                studentRepository.save(student);

                //daodanie lekcji
                Lesson lesson1 = new Lesson();
                lesson1.setType(LessonType.EXERCISE);
                lesson1.setBasedOn(subjectRealization1);
                lesson1.setTaughtBy(teacher);


                Lesson lesson2 = new Lesson();
                lesson2.setType(LessonType.LECTURE);
                lesson2.setBasedOn(subjectRealization2);
                lesson2.setTaughtBy(teacher2);

                subjectRealization1.getBasedFor().add(lesson1);
                subjectRealization2.getBasedFor().add(lesson2);

                teacher.setTeaches(Set.of(lesson1));
                teacher2.setTeaches(Set.of(lesson2));
                teacherRepository.saveAll(List.of(teacher, teacher2));
                lessonRepository.saveAll(List.of(lesson1, lesson2));

                subjectRealizationRepository.saveAll(List.of(subjectRealization1, subjectRealization2));

                Assignment assignment = new Assignment();
                assignment.setDayOfWeek("Friday");
                assignment.setStartTime(LocalTime.of(10, 15));
                assignment.setScheduledBy(lesson1);
                assignment.setAttendedBy(groupUni);
                lesson1.getScheduledAs().add(assignment);

                Assignment assignment2 = new Assignment();
                assignment2.setDayOfWeek("Friday");
                assignment2.setStartTime(LocalTime.of(12, 15));
                assignment2.setScheduledBy(lesson2);
                assignment2.setAttendedBy(groupUni2);
                lesson2.getScheduledAs().add(assignment2);


                Assignment assignment3 = new Assignment();
                assignment3.setDayOfWeek("Monday");
                assignment3.setStartTime(LocalTime.of(14, 0));
                assignment3.setScheduledBy(lesson1);
                assignment3.setAttendedBy(groupUni2);
                lesson1.getScheduledAs().add(assignment3);

                groupUni.getAttendsIn().add(assignment);
                groupUni2.getAttendsIn().add(assignment2);
                groupUni2.getAttendsIn().add(assignment3);

                assignmentRepository.saveAll(Set.of(assignment, assignment2, assignment3));

                Classroom classroom = new Classroom();
                classroom.setCapacity(23);
                classroom.setRoomNumber("123A");

                Classroom classroom2 = new Classroom();
                classroom2.setCapacity(25);
                classroom2.setRoomNumber("111X");

                ClassActivity classActivity = new ClassActivity();
                classActivity.setCreatedAt(LocalDate.of(2025, 3, 12));
                classActivity.setCreatedAt(LocalDate.now());
                classActivity.setAccessedBy(assignment);
                classActivity.setHeldIn(classroom);
                classroom.getHolds().add(classActivity);


                ClassActivity classActivity2 = new ClassActivity();
                classActivity2.setCreatedAt(LocalDate.of(2025, 3, 12));
                classActivity2.setCreatedAt(LocalDate.now());
                classActivity2.setAccessedBy(assignment2);
                classActivity2.setHeldIn(classroom);
                classroom2.getHolds().add(classActivity2);


                ClassActivity classActivity3 = new ClassActivity();
                classActivity3.setCreatedAt(LocalDate.of(2025, 3, 12));
                classActivity3.setCreatedAt(LocalDate.now());
                classActivity3.setAccessedBy(assignment3);
                classActivity3.setHeldIn(classroom);
                classroom.getHolds().add(classActivity3);

                classroomRepository.saveAll(List.of(classroom, classroom2));

            }catch (Exception e){
                e.printStackTrace();
            } finally {
                assignmentRepository.findAll().forEach(a -> {
                    System.out.println("Assignment " + a.getId() + ": " + a.getDayOfWeek() + " " + a.getStartTime());
                });

            }

        }
    }
}
