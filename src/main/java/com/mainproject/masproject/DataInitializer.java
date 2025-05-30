package com.mainproject.masproject;

import com.mainproject.masproject.models.*;
import com.mainproject.masproject.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

                student.getSubmitTo().add(groupUni2); // Tylko to wystarczy!
                studentRepository.save(student);

            }catch (Exception e){
                e.printStackTrace();
            }


        }



    }



}
