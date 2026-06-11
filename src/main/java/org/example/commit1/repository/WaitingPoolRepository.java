package org.example.commit1.repository;

import org.example.commit1.model.WaitingPool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WaitingPoolRepository extends JpaRepository<WaitingPool,Integer> {

    Optional<WaitingPool> findFirstByDateAndUserEmailNot(LocalDate date, String email);
    Optional<WaitingPool> findByUserEmailAndDate(String email,LocalDate date);
}
