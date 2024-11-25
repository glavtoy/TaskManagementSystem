package com.taskmanagementsystem.service;

import com.taskmanagementsystem.dto.TaskDto;
import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.User;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private UserService userService;

    public List<Task> getAllTasksFromCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);
        if (user != null) {
            Set<Task> allTasks = new HashSet<>();
            allTasks.addAll(taskRepository.findByAssignee(user));
            allTasks.addAll(taskRepository.findByAuthor(user));
            return new ArrayList<>(allTasks);
        }
        return Collections.emptyList();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task createOrUpdateTask(Task task, TaskDto taskDto, User user) {
        task.setAuthor(user);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setAssignee(userService.findByEmail(taskDto.getAssignee()).orElse(null));
        return task;
    }
}
