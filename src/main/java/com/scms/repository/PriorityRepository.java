package com.scms.repository;

import com.scms.entity.Priority;
import com.scms.enums.PriorityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    Optional<Priority> findByName(PriorityType name);

    Optional<Priority> findByLevel(Integer level);

    List<Priority> findAllByOrderByLevelAsc();

    boolean existsByName(PriorityType name);

}