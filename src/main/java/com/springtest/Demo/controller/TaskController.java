package com.springtest.Demo.controller;

import com.springtest.Demo.auth.dto.UserSessionDTO;
import com.springtest.Demo.repository.Task;
import com.springtest.Demo.repository.TaskStatus;
import com.springtest.Demo.repository.User;
import com.springtest.Demo.service.SessionService;
import com.springtest.Demo.service.TaskService;
import com.springtest.Demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, SessionService sessionService) {
        this.taskService = taskService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute("title", "Home Page");

        UserSessionDTO currentUser = sessionService.getUserFromSession(request);
        if (currentUser != null) {
            List<Task> tasks = taskService.findByUserId(currentUser.getId());
            model.addAttribute("tasks", tasks);
        } else {
            return "redirect:/auth/login";
        }
        
        return "home";
    }

    @GetMapping("/home/add")
    public String taskAdd(Model model) {
        model.addAttribute("title", "Add Task");
        return "task-add";
    }

    @GetMapping("/tasks/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        model.addAttribute("taskId", id);
        return "task-details";
    }

    @PostMapping("/home/add")
    public String addTask(@RequestParam String title,
                          @RequestParam String description,
                          @RequestParam("reminderTime")
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminderTime,
                          HttpServletRequest request) {

        UserSessionDTO currentUser = sessionService.getUserFromSession(request);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        User user = userService.findById(currentUser.getId());

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setUser(user);
        task.setReminderTime(reminderTime.toLocalDate().atStartOfDay());

        taskService.createTask(task);
        return "redirect:/home";
    }

    @GetMapping(path = "/home/delete/{id}")
    public String deleteTask(@PathVariable Long id, HttpServletRequest request) {
        UserSessionDTO currentUser = sessionService.getUserFromSession(request);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Task task = taskService.getTaskById(id);
        if (task.getUser().getId().equals(currentUser.getId())) {
            taskService.deleteTask(id);
        }
        
        return "redirect:/home";
    }

    @GetMapping(path = "/home/done/{id}")
    public String doneTask(@PathVariable Long id, HttpServletRequest request) {
        UserSessionDTO currentUser = sessionService.getUserFromSession(request);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Task task = taskService.getTaskById(id);
        if (task.getUser().getId().equals(currentUser.getId())) {
            taskService.doneTask(id);
        }
        
        return "redirect:/home";
    }

    @GetMapping("/home/task-details/{id}")
    public String taskDetails(@PathVariable Long id, Model model, HttpServletRequest request) {
        model.addAttribute("title", "Task Details");

        UserSessionDTO currentUser = sessionService.getUserFromSession(request);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Task task = taskService.getTaskById(id);

        if (task.getUser().getId().equals(currentUser.getId())) {
            model.addAttribute("task", task);
            return "task-details";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/home/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        // Получаем текущего пользователя из сессии
        UserSessionDTO currentUser = sessionService.getUserFromSession(request);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Task task = taskService.getTaskById(id);
        
        // Проверяем, принадлежит ли задача текущему пользователю
        if (task.getUser().getId().equals(currentUser.getId())) {
            model.addAttribute("task", task);
            return "task-edit";
        } else {
            // Если задача не принадлежит пользователю, перенаправляем на домашнюю страницу
            return "redirect:/home";
        }
    }

    @PostMapping("/home/edit/{id}")
    public String editTask(@PathVariable Long id,
                          @RequestParam String title,
                          @RequestParam String description,
                          @RequestParam("reminderTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminderTime,
                          @RequestParam TaskStatus status,
                          HttpServletRequest request) {

        // Получаем текущего пользователя из сессии
        UserSessionDTO currentUser = sessionService.getUserFromSession(request);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Task task = taskService.getTaskById(id);
        
        // Проверяем, принадлежит ли задача текущему пользователю
        if (task.getUser().getId().equals(currentUser.getId())) {
            task.setTitle(title);
            task.setDescription(description);
            task.setReminderTime(reminderTime);
            task.setStatus(status);
            
            taskService.updateTask(task);
            return "redirect:/home/task-details/" + id;
        } else {
            // Если задача не принадлежит пользователю, перенаправляем на домашнюю страницу
            return "redirect:/home";
        }
    }

}

