package org.example.commit1.repository;

import org.example.commit1.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CommitRepository extends JpaRepository<Commit,Integer> {

    List<Commit> findByUserEmail(String email);
    public List<Commit> findByUserEmailAndDate(String email, LocalDate date);
}
