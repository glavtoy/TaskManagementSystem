package com.taskmanagementsystem.model;

import com.taskmanagementsystem.model.priority.Priority;
import com.taskmanagementsystem.model.status.Status;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    private User author;
    @ManyToOne
    private User assignee;

    @OneToMany
    private List<Comment> comments;
}