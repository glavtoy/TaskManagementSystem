package com.taskmanagementsystem.model;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    private String text;

    @ManyToOne
    private Task task;

    @ManyToOne
    private User author;
}
