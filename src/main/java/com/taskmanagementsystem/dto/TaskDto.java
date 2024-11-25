package com.taskmanagementsystem.dto;

import com.taskmanagementsystem.model.priority.Priority;
import com.taskmanagementsystem.model.status.Status;
import lombok.Data;

import java.util.List;

@Data
public class TaskDto {

    private String title;
    private String description;
    private Status status;
    private Priority priority;

    private String author;
    private String assignee;

    private List<CommentDto> comments;
}
