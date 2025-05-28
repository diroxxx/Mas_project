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


    @EventListener
    public void atStart(ContextRefreshedEvent event) {

        if (personRepository.count() == 0 ) {

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
                    .hireDate(LocalDate.of(2025,3,12))
                    .personEmployee(person)
                    .build();
            employeeRepository.save(employee);
            Teacher teacher = Teacher.builder()
                            .academicTitles(List.of("prof","mgr"))
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

        }
    }



}
