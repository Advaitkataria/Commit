package org.example.commit1.service;

import org.example.commit1.model.Commit;
import org.example.commit1.model.User;
import org.example.commit1.repository.CommitRepository;
import org.example.commit1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final CommitRepository commitRepository;

    @Autowired
    public ProfileService(UserRepository userRepository,
                          CommitRepository commitRepository) {
        this.userRepository = userRepository;
        this.commitRepository = commitRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Map<String, Object> getProfile() {
        User currentUser = getCurrentUser();

        List<Commit> history = commitRepository
                .findByUserEmail(currentUser.getEmail());

        return Map.of(
                "name", currentUser.getName(),
                "email", currentUser.getEmail(),
                "history", history
        );
    }
}