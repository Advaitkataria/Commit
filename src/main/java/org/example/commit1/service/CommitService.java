package org.example.commit1.service;

import org.example.commit1.model.Commit;
import org.example.commit1.model.User;
import org.example.commit1.repository.CommitRepository;
import org.example.commit1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommitService {

    private final CommitRepository commitRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommitService(CommitRepository commitRepository, UserRepository userRepository){
        this.commitRepository=commitRepository;
        this.userRepository=userRepository;
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
    }

    public List<Commit> getAllCommit(LocalDate date){
        String email = getCurrentUser().getEmail();;

        if(getCurrentUser().getRole().equals("ROLE_ADMIN")){
            return commitRepository.findAll();
        }
        if(date == null){
            date = LocalDate.now();
        }
        return commitRepository.findByUserEmailAndDate(email,date);
    }

    public Commit addCommit(Commit commit){
        commit.setUser(getCurrentUser());
        commitRepository.save(commit);
        commit.setDate(LocalDate.now());
        return commit;
    }

    public void deleteCommit(int id){
        Commit commit = commitRepository.findById(id).orElseThrow(()->new RuntimeException("Commit not found"));
        commitRepository.deleteById(id);
    }

    public Commit updateCommit(int id, Commit updatedCommit){
        Commit commit = commitRepository.findById(id).orElseThrow(()->new RuntimeException("Commit not found"));
        if(!commit.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new RuntimeException("You can update only your commit");
        }
        commit.setDescription(updatedCommit.getDescription());
        commit.setCategory(updatedCommit.getCategory());
        commit.setTitle(updatedCommit.getTitle());

        return commitRepository.save(commit);
    }
}
