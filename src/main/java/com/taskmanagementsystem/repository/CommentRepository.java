package com.taskmanagementsystem.repository;

import com.taskmanagementsystem.model.Comment;
import com.taskmanagementsystem.model.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
}
