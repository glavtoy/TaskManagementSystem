package com.taskmanagementsystem.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String text;
    private String email;
    private TaskDto task;
}
