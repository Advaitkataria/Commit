package org.example.commit1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "commit")
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Column()
    private String description;

    private LocalDate date;

    @Pattern(regexp = "FITNESS|STUDY|WORK|HABIT|PERSONAL|CUSTOM")
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    private User user;

    @Pattern(regexp = "ACTIVE|COMPLETED|FAILED")
    private String status; // "ACTIVE", "COMPLETED", "FAILED"
}
