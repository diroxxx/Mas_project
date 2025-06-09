package com.mainproject.masproject;

import com.mainproject.masproject.models.*;
import com.mainproject.masproject.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
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

                Person person5 = new Person(
                        "Julia",
                        "Wtopa",
                        "che-dom@wpl.pl",
                        "12312312312311",
                        123123123,
                        new Student("s2742223", 1234),
                        new Employee(
                                LocalDate.of(2025, 5, 12),
                                new Teacher(List.of("mgr")),
                                null
                                )
                        );

                Person person4 = new Person(
                        "Konrad",
                        "Wierzbicki",
                        "che-dom@wpl.pl",
                        "12312312312311",
                        123123123,
                        new Student("s2743", 1234),
                        new Employee(
                                LocalDate.of(2025, 5, 12),
                                new Teacher(List.of("mgr")),
                                new DeanOfficeEmployee(List.of("papers")
                                )
                        )
                );

                Person person = new Person(
                        "Tomek",
                        "Kowalski",
                        "che-dom2@wpl.pl",
                        "12312312311",
                        123123123,
                        new Student("s27354", 1234),
                        new Employee(
                                LocalDate.of(2025, 3, 12),
                                new Teacher(List.of("mgr")),
                                null
                        )
                );

                Person person2 = new Person(
                        "Dominika",
                        "Wcislo",
                        "che-dom2@wpl.pl",
                        "12312312311",
                        547342123,
                        null,
                        new Employee(
                                LocalDate.of(2024, 3, 12),
                                new Teacher(List.of("mgr", "inz")),
                                null
                        )
                );
                personRepository.saveAll(List.of(person, person2, person4, person5));

                GroupUni groupUni = new GroupUni();
                groupUni.setName("25c_s6");
                groupUni.setCapacity(21);
                groupUniRepository.save(groupUni);

                if (person.getStudent().getSubmitTo() == null) {
                    person.getStudent().setSubmitTo(new HashSet<>());
                }
                groupUni.getSubmitedBy().put(person.getStudent().getIndex(), person.getStudent());

                person.getStudent().getSubmitTo().add(groupUni);

                personRepository.save(person);


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
                groupUni2.addStudent(person.getStudent());
                groupUniRepository.save(groupUni2);


                //daodanie lekcji
                Lesson lesson1 = new Lesson();
                lesson1.setType(LessonType.EXERCISE);
                lesson1.setBasedOn(subjectRealization1);
                lesson1.setTaughtBy(person.getPersonEmployee().getTeacher());


                person.getPersonEmployee().getTeacher().getTeaches().add(lesson1);


                Lesson lesson2 = new Lesson();
                lesson2.setType(LessonType.LECTURE);
                lesson2.setBasedOn(subjectRealization2);
                lesson2.setTaughtBy(person2.getPersonEmployee().getTeacher());

                Lesson lesson3 = new Lesson();
                lesson2.setType(LessonType.LECTURE);
                lesson2.setBasedOn(subjectRealization2);
                lesson2.setTaughtBy(person.getPersonEmployee().getTeacher());


                subjectRealization1.getBasedFor().add(lesson1);
                subjectRealization2.getBasedFor().add(lesson2);

                person2.getPersonEmployee().getTeacher().getTeaches().add(lesson2);
                teacherRepository.saveAll(List.of(person.getPersonEmployee().getTeacher(), person2.getPersonEmployee().getTeacher()));
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

        //dodawanie subjects
            Subject mdd = new Subject();
            mdd.setCode("Mdd");
            mdd.setEcts(4);
            mdd.setName("3dModeling Graphic");

            SubjectRealization subjectRealizationMdd = new SubjectRealization();
            subjectRealizationMdd.setIncludedBy(mdd);
            subjectRealizationMdd.setOfferedBy(semester1);
            subjectRealizationMdd.setLanguage(LangType.ENGLISH);
            subjectRealizationMdd.setMode(ModeType.STATIONARY);
            semester1.getOffers().add(subjectRealizationMdd);
            mdd.getIncludedIn().add(subjectRealizationMdd);



                Subject pbio = new Subject();
                pbio.setCode("Pbio");
                pbio.setEcts(5);
                pbio.setName("bioinformatics");

                SubjectRealization subjectRealizationPbio = new SubjectRealization();
                subjectRealizationPbio.setIncludedBy(pbio);
                subjectRealizationPbio.setOfferedBy(semester1);
                subjectRealizationPbio.setLanguage(LangType.ENGLISH);
                subjectRealizationPbio.setMode(ModeType.STATIONARY);
                semester1.getOffers().add(subjectRealizationPbio);
                pbio.getIncludedIn().add(subjectRealizationPbio);



                Subject mas = new Subject();
                mas.setCode("Mas");
                mas.setEcts(5);
                mas.setName("Modeling and analysis of information systems");

                SubjectRealization subjectRealizationMas = new SubjectRealization();
                subjectRealizationMas.setIncludedBy(mas);
                subjectRealizationMas.setOfferedBy(semester1);
                subjectRealizationMas.setLanguage(LangType.ENGLISH);
                subjectRealizationMas.setMode(ModeType.STATIONARY);
                semester1.getOffers().add(subjectRealizationMas);
                mas.getIncludedIn().add(subjectRealizationMas);

                Subject miw = new Subject();
                miw.setCode("Miw");
                miw.setEcts(5);
                miw.setName("engineering knowledge methods");

                SubjectRealization subjectRealizationMiw = new SubjectRealization();
                subjectRealizationMiw.setIncludedBy(miw);
                subjectRealizationMiw.setOfferedBy(semester1);
                subjectRealizationMiw.setLanguage(LangType.ENGLISH);
                subjectRealizationMiw.setMode(ModeType.STATIONARY);
                semester1.getOffers().add(subjectRealizationMiw);
                miw.getIncludedIn().add(subjectRealizationMiw);


                subjectRepository.saveAll(List.of(pbio,mdd, mas,miw));



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
