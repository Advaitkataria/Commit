package org.example.commit1.repository;

import org.example.commit1.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommitRepository extends JpaRepository<Commit,Integer> {

    List<Commit> findByUserEmail(String email);
    public List<Commit> findByUserEmailAndDate(String email, LocalDate date);

    @Query("SELECT c FROM Commit c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Commit> findByIdWithUser(@Param("id") int id);
}
