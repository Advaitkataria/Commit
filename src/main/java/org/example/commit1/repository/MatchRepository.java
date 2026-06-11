package org.example.commit1.repository;

import org.example.commit1.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match,Integer> {
    Optional<Match> findByUser1EmailAndDate(String email, LocalDate date);
    Optional<Match> findByUser2EmailAndDate(String email, LocalDate date);
}
