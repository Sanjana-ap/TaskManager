package com.san.task_manager.controller;

import com.san.task_manager.entity.Reminder;
import com.san.task_manager.service.ReminderService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public Reminder createReminder(@RequestParam Long taskId, @RequestParam String reminderTime) {
        return reminderService.createReminder(taskId, LocalDateTime.parse(reminderTime));
    }
    
    @GetMapping("/unacknowledged")
    public List<Reminder> getUnacknowledgedReminders() {
        return reminderService.getUnacknowledgedReminders();
    }

    @PatchMapping("/{id}/acknowledge")
    public void acknowledgeReminder(@PathVariable Long id) {
        reminderService.acknowledgeReminder(id);
    }

}