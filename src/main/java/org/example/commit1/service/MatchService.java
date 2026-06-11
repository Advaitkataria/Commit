package org.example.commit1.service;

import jakarta.transaction.Transactional;
import org.example.commit1.exception.UnauthorizedAccessException;
import org.example.commit1.model.Commit;
import org.example.commit1.model.Match;
import org.example.commit1.model.User;
import org.example.commit1.model.WaitingPool;
import org.example.commit1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.example.commit1.dto.MatchResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final WaitingPoolRepository waitingPoolRepository;
    private final CommitRepository commitRepository;
    private final UserRepository userRepository;
    private final CheckInRepository checkInRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository,
                        WaitingPoolRepository waitingPoolRepository,
                        CommitRepository commitRepository,
                        UserRepository userRepository,
                        CheckInRepository checkInRepository) {
        this.matchRepository = matchRepository;
        this.waitingPoolRepository = waitingPoolRepository;
        this.commitRepository = commitRepository;
        this.userRepository = userRepository;
        this.checkInRepository = checkInRepository;
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
    }

    @Transactional
    public Match joinPool(int commitId){
        User currentUser = getCurrentUser();

//        Commit commit = commitRepository.findByIdWithUser(commitId)
//                .orElseThrow(() -> new RuntimeException("Commit not found"));

        Commit commit = commitRepository.findByIdWithUser(commitId)
                .orElseThrow(() -> new RuntimeException("Commit not found"));

        if(!commit.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new UnauthorizedAccessException("This commit does not belong to you");
        }

        boolean alreadyInPool = waitingPoolRepository
                .findByUserEmailAndDate(currentUser.getEmail(), LocalDate.now())
                .isPresent();
        if (alreadyInPool) {
            throw new RuntimeException("You are already waiting for a match today");
        }

        boolean alreadyMatched = matchRepository
                .findByUser1EmailAndDate(currentUser.getEmail(), LocalDate.now())
                .isPresent()
                ||
                matchRepository
                        .findByUser2EmailAndDate(currentUser.getEmail(), LocalDate.now())
                        .isPresent();
        if (alreadyMatched) {
            throw new RuntimeException("You already have a match today");
        }


        Optional<WaitingPool> waitingOptional = waitingPoolRepository
                .findFirstByDateAndUserEmailNot(LocalDate.now(), currentUser.getEmail());


        if (waitingOptional.isPresent()) {
            WaitingPool partner = waitingOptional.get();

            try {

                waitingPoolRepository.delete(partner);
                waitingPoolRepository.flush();


                Match match = new Match();
                match.setUser1(partner.getUser());
                match.setUser2(currentUser);
                match.setCommit1(partner.getCommit());
                match.setCommit2(commit);
                match.setStatus("ACTIVE");
                match.setDate(LocalDate.now());
                match.setCreatedAt(LocalDateTime.now());

                return matchRepository.save(match);

            } catch (OptimisticLockingFailureException e) {
                addToPool(currentUser, commit);
                return null;
            }
        }

        addToPool(currentUser, commit);
        return null;
    }

    private void addToPool(User user, Commit commit) {
        WaitingPool entry = new WaitingPool();
        entry.setUser(user);
        entry.setCommit(commit);
        entry.setDate(LocalDate.now());
        entry.setJoinedAt(LocalDateTime.now());
        waitingPoolRepository.save(entry);
    }

public MatchResponseDTO getCurrentMatch() {
    User currentUser = getCurrentUser();
    String email = currentUser.getEmail();
    LocalDate today = LocalDate.now();


    Optional<WaitingPool> inPool = waitingPoolRepository
            .findByUserEmailAndDate(email, today);

    if (inPool.isPresent()) {
        WaitingPool poolEntry = inPool.get();
        Commit myCommit = poolEntry.getCommit();

        return MatchResponseDTO.builder()
                .matchId(-1)
                .status("WAITING")
                .waitingForPartner(true)
                .commitment(MatchResponseDTO.CommitInfo.builder()
                        .id(myCommit.getId())
                        .title(myCommit.getTitle())
                        .description(myCommit.getDescription())
                        .category(myCommit.getCategory())
                        .build())
                .buddy(null)
                .buddyCheckedInToday(false)
                .myCheckins(new boolean[7])
                .buddyCheckins(new boolean[7])
                .build();
    }


    Optional<Match> matchOpt = matchRepository
            .findByUser1EmailAndDate(email, today);
    if (matchOpt.isEmpty()) {
        matchOpt = matchRepository.findByUser2EmailAndDate(email, today);
    }


    if (matchOpt.isEmpty()) {
        return null;
    }

    Match match = matchOpt.get();

    boolean iAmUser1 = match.getUser1().getEmail().equals(email);

    User buddy        = iAmUser1 ? match.getUser2()   : match.getUser1();
    Commit myCommit   = iAmUser1 ? match.getCommit1() : match.getCommit2();


    boolean buddyCheckedInToday = checkInRepository
            .existsByMatchIdAndUserEmailAndDate(match.getId(), buddy.getEmail(), today);


    boolean[] myCheckins   = buildCheckinArray(email, match.getId());
    boolean[] buddyCheckins = buildCheckinArray(buddy.getEmail(), match.getId());

    return MatchResponseDTO.builder()
            .matchId(match.getId())
            .status(match.getStatus())
            .waitingForPartner(false)
            .commitment(MatchResponseDTO.CommitInfo.builder()
                    .id(myCommit.getId())
                    .title(myCommit.getTitle())
                    .description(myCommit.getDescription())
                    .category(myCommit.getCategory())
                    .build())
            .buddy(MatchResponseDTO.BuddyInfo.builder()
                    .id(buddy.getId())
                    .name(buddy.getName())
                    .streak(0)
                    .build())
            .buddyCheckedInToday(buddyCheckedInToday)
            .myCheckins(myCheckins)
            .buddyCheckins(buddyCheckins)
            .build();
}

private boolean[] buildCheckinArray(String email, int matchId) {
    boolean[] result = new boolean[7];
    LocalDate today = LocalDate.now();
    for (int i = 0; i < 7; i++) {
        LocalDate day = today.minusDays(6 - i);
        result[i] = checkInRepository
                .existsByMatchIdAndUserEmailAndDate(matchId, email, day);
    }
    return result;
}
}

