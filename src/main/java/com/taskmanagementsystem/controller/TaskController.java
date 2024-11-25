package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.dto.TaskDto;
import com.taskmanagementsystem.exception.ApplicationError;
import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.User;
import com.taskmanagementsystem.service.TaskService;
import com.taskmanagementsystem.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public List<Task> getUserTasks() {
        return taskService.getAllTasksFromCurrentUser();
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addTask(@RequestBody TaskDto taskDto) {
        String username = userService.getCurrentUsername();
        User user = userService.findByEmail(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Пользователь не найден"));
        }
        Task task = taskService.createOrUpdateTask(new Task(), taskDto, user);
        Task savedTask = taskService.saveTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> editTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Task task = taskService.getTaskById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApplicationError(HttpStatus.NOT_FOUND.value(), "Задача не найдена"));
        }

        String currentUsername = userService.getCurrentUsername();

        User assignee = task.getAssignee();
        User author = task.getAuthor();

        if ((assignee != null && !assignee.getUsername().equalsIgnoreCase(currentUsername)) &&
                !author.getUsername().equalsIgnoreCase(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApplicationError(HttpStatus.FORBIDDEN.value(), "Нет доступа"));
        }

        task = taskService.createOrUpdateTask(task, taskDto, task.getAuthor());
        Task savedTask = taskService.saveTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        if (task == null) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.NOT_FOUND.value(), "Не найдено"), HttpStatus.NOT_FOUND);
        }
        if (!task.getAuthor().getUsername().equalsIgnoreCase(userService.getCurrentUsername()) && !userService.isAdmin()) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), "Нет доступа"), HttpStatus.BAD_REQUEST);
        }
        taskService.deleteTask(id);
        return new ResponseEntity<>(new ApplicationError(HttpStatus.NO_CONTENT.value(), "Удалено"), HttpStatus.NO_CONTENT);
    }
}
