package com.mainproject.masproject.repositories;

import com.mainproject.masproject.models.GroupUni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface GroupUniRepository extends JpaRepository<GroupUni, Long> {
}
