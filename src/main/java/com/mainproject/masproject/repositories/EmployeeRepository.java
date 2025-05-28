package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
