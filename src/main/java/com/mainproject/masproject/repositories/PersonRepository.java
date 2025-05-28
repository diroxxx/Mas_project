package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
