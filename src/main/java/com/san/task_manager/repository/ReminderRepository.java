package com.san.task_manager.repository;

import com.san.task_manager.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByTriggeredFalseAndReminderTimeLessThanEqual(LocalDateTime now);

    List<Reminder> findByTriggeredTrueAndAcknowledgedFalse();
    void deleteByTaskId(Long taskId);
}