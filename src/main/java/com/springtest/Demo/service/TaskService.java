package com.springtest.Demo.service;

import com.springtest.Demo.repository.Task;
import com.springtest.Demo.repository.TaskRepository;
import com.springtest.Demo.repository.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача с ID " + id + " не найдена"));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public void doneTask(Long id) {
        Task task = getTaskById(id);
        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Transactional
    public void updateTask(Task task) {
        taskRepository.save(task);
    }
}
