package org.example.commit1.repository;

import org.example.commit1.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {


    boolean existsByMatchIdAndUserEmailAndDate(int matchId, String email, LocalDate date);


    boolean existsByUserEmailAndDate(String email, LocalDate date);


    List<CheckIn> findByMatchIdOrderByDateDesc(int matchId);


    boolean existsByMatchIdAndDate(int matchId, LocalDate date);
}