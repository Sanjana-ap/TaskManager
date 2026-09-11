package com.san.task_manager.repository;

import com.san.task_manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import com.san.task_manager.entity.Priority;
import com.san.task_manager.entity.Status;
import com.san.task_manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task>{

    List<Task> findByStatus(Status status);

    List<Task> findByPriority(Priority priority);

    List<Task> findByCategoryId(Long categoryId);

}
