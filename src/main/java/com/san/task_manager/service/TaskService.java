package com.san.task_manager.service;

import com.san.task_manager.dto.TaskStatisticsDTO;
import com.san.task_manager.entity.Category;
import com.san.task_manager.entity.Priority;
import com.san.task_manager.entity.Status;
import com.san.task_manager.entity.Task;
import com.san.task_manager.exception.CategoryNotFoundException;
import com.san.task_manager.exception.TaskNotFoundException;
import com.san.task_manager.repository.CategoryRepository;
import com.san.task_manager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import com.san.task_manager.specification.TaskSpecification;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    public Task createTask(Task task) {
        Category category = resolveCategory(task.getCategory().getId());
        task.setCategory(category);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existing = getTaskById(id);

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setDueDate(updatedTask.getDueDate());
        existing.setDueTime(updatedTask.getDueTime());
        existing.setPriority(updatedTask.getPriority());
        existing.setStatus(updatedTask.getStatus());
        
        if (updatedTask.getStatus() == Status.COMPLETED) {
            if (existing.getCompletedAt() == null) {
                existing.setCompletedAt(LocalDateTime.now());
            }
        } else {
            existing.setCompletedAt(null);
        }

        Category category = resolveCategory(updatedTask.getCategory().getId());
        existing.setCategory(category);

        return taskRepository.save(existing);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task completeTask(Long id) {

        Task task = getTaskById(id);

        if (task.getStatus() != Status.COMPLETED) {
            task.setStatus(Status.COMPLETED);
            task.setCompletedAt(LocalDateTime.now());
        }

        return taskRepository.save(task);
    }

    private Category resolveCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
    }
    
    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority);
    }

    public List<Task> getTasksByCategory(Long categoryId) {
        return taskRepository.findByCategoryId(categoryId);
    }
    
    public List<Task> searchTasks(Status status, Priority priority, Long categoryId, String keyword) {
        Specification<Task> spec = Specification
                .where(TaskSpecification.hasStatus(status))
                .and(TaskSpecification.hasPriority(priority))
                .and(TaskSpecification.hasCategoryId(categoryId))
                .and(TaskSpecification.titleContains(keyword));

        return taskRepository.findAll(spec);
    }
    
    public TaskStatisticsDTO getStatistics() {
        List<Task> allTasks = taskRepository.findAll();

        Map<String, Long> countsByStatus = allTasks.stream()
                .collect(Collectors.groupingBy(t -> t.getStatus().name(), Collectors.counting()));

        Map<String, Long> countsByPriority = allTasks.stream()
                .collect(Collectors.groupingBy(t -> t.getPriority().name(), Collectors.counting()));

        Map<String, Long> countsByCategory = allTasks.stream()
                .collect(Collectors.groupingBy(t -> t.getCategory().getName(), Collectors.counting()));

        LocalDate today = LocalDate.now();
        long overdueCount = allTasks.stream()
                .filter(t -> t.getStatus() != Status.COMPLETED && t.getDueDate().isBefore(today))
                .count();

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        long completedThisWeek = allTasks.stream()
                .filter(t -> t.getCompletedAt() != null
                        && !t.getCompletedAt().toLocalDate().isBefore(startOfWeek))
                .count();

        LocalDate startOfMonth = today.withDayOfMonth(1);
        long completedThisMonth = allTasks.stream()
                .filter(t -> t.getCompletedAt() != null
                        && !t.getCompletedAt().toLocalDate().isBefore(startOfMonth))
                .count();

        return new TaskStatisticsDTO(countsByStatus, countsByPriority, countsByCategory,
                overdueCount, completedThisWeek, completedThisMonth);
    }

}