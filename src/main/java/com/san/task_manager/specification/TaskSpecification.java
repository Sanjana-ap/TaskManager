package com.san.task_manager.specification;

import com.san.task_manager.entity.Priority;
import com.san.task_manager.entity.Status;
import com.san.task_manager.entity.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasStatus(Status status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(Priority priority) {
        return (root, query, criteriaBuilder) ->
                priority == null ? null : criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Task> titleContains(String keyword) {
        return (root, query, criteriaBuilder) ->
                (keyword == null || keyword.isBlank())
                        ? null
                        : criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                "%" + keyword.toLowerCase() + "%"
                          );
    }
}