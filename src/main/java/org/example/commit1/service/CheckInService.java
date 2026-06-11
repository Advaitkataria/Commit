package org.example.commit1.service;

import jakarta.transaction.Transactional;
import org.example.commit1.exception.UnauthorizedAccessException;
import org.example.commit1.model.CheckIn;
import org.example.commit1.model.Commit;
import org.example.commit1.model.Match;
import org.example.commit1.model.User;
import org.example.commit1.repository.CheckInRepository;
import org.example.commit1.repository.CommitRepository;
import org.example.commit1.repository.MatchRepository;
import org.example.commit1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final CommitRepository commitRepository;

    @Autowired
    public CheckInService(CheckInRepository checkInRepository,
                          MatchRepository matchRepository,
                          UserRepository userRepository,
                          CommitRepository commitRepository) {
        this.checkInRepository = checkInRepository;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.commitRepository=commitRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public CheckIn checkIn(int matchId, String note) {
        User currentUser = getCurrentUser();


        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));


        boolean belongsToMatch = match.getUser1().getEmail().equals(currentUser.getEmail())
                || match.getUser2().getEmail().equals(currentUser.getEmail());
        if (!belongsToMatch) {
            throw new UnauthorizedAccessException("You do not belong to this match");
        }


        boolean alreadyCheckedIn = checkInRepository
                .existsByMatchIdAndUserEmailAndDate(matchId, currentUser.getEmail(), LocalDate.now());
        if (alreadyCheckedIn) {
            throw new RuntimeException("You have already checked in today");
        }


        CheckIn checkIn = new CheckIn();
        checkIn.setMatch(match);
        checkIn.setUser(currentUser);
        checkIn.setNote(note);
        checkIn.setDate(LocalDate.now());
        checkIn.setCreatedAt(LocalDateTime.now());
        checkInRepository.save(checkIn);


        long checkInsToday = checkInRepository
                .findByMatchIdOrderByDateDesc(matchId)
                .stream()
                .filter(c -> c.getDate().equals(LocalDate.now()))
                .count();

        if (checkInsToday >= 2) {

            match.setStatus("COMPLETED");
            matchRepository.save(match);
            Commit commit1 = match.getCommit1();
            Commit commit2 = match.getCommit2();
            commit1.setStatus("COMPLETED");
            commit2.setStatus("COMPLETED");
            commitRepository.save(commit1);
            commitRepository.save(commit2);
        }

        return checkIn;
    }



    public Map<String, Boolean> checkedInToday() {
        String email = getCurrentUser().getEmail();
        boolean checkedIn = checkInRepository
                .existsByUserEmailAndDate(email, LocalDate.now());
        return Map.of("checkedIn", checkedIn);
    }



    public List<CheckIn> getByMatch(int matchId) {
        User currentUser = getCurrentUser();

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));


        boolean belongsToMatch = match.getUser1().getEmail().equals(currentUser.getEmail())
                || match.getUser2().getEmail().equals(currentUser.getEmail());
        if (!belongsToMatch) {
            throw new UnauthorizedAccessException("You do not belong to this match");
        }

        return checkInRepository.findByMatchIdOrderByDateDesc(matchId);
    }
}