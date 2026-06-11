package org.example.commit1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Pattern(regexp = "WAITING|ACTIVE|COMPLETED|FAILED")
    private String status;

    private LocalDate date= LocalDate.now();

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1",nullable = false)
    @JsonIgnore
    private User user1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2",nullable = false)
    @JsonIgnore
    private User user2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commit1",nullable = false)
    @JsonIgnore
    private Commit commit1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commit2",nullable = false)
    @JsonIgnore
    private Commit commit2;

}
