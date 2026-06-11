package org.example.commit1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "waitingPool")
public class WaitingPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDate date;

    private LocalDateTime joinedAt;

    @Version
    private int version;

    @OneToOne
    @JoinColumn(name = "user",nullable = false)
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn(name = "commit",nullable = false)
    @JsonIgnore
    private Commit commit;


}
