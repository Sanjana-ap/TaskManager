package com.san.task_manager.service;

import com.san.task_manager.entity.Reminder;
import com.san.task_manager.entity.Task;
import com.san.task_manager.exception.TaskNotFoundException;
import com.san.task_manager.repository.ReminderRepository;
import com.san.task_manager.repository.TaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final TaskRepository taskRepository;

    public ReminderService(ReminderRepository reminderRepository, TaskRepository taskRepository) {
        this.reminderRepository = reminderRepository;
        this.taskRepository = taskRepository;
    }

    public Reminder createReminder(Long taskId, LocalDateTime reminderTime) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        Reminder reminder = new Reminder();
        reminder.setTask(task);
        reminder.setReminderTime(reminderTime);

        return reminderRepository.save(reminder);
    }
    
    public List<Reminder> getUnacknowledgedReminders() {
        return reminderRepository.findByTriggeredTrueAndAcknowledgedFalse();
    }

    public void acknowledgeReminder(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found with id: " + id));
        reminder.setAcknowledged(true);
        reminderRepository.save(reminder);
    }

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        List<Reminder> dueReminders =
                reminderRepository.findByTriggeredFalseAndReminderTimeLessThanEqual(LocalDateTime.now());

        for (Reminder reminder : dueReminders) {
            System.out.println("REMINDER: Task '" + reminder.getTask().getTitle() + "' is due soon!");
            reminder.setTriggered(true);
            reminderRepository.save(reminder);
        }
    }

}