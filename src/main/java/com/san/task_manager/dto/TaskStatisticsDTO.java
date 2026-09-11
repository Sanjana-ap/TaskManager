package com.san.task_manager.dto;

import java.util.Map;

public class TaskStatisticsDTO {

    private Map<String, Long> countsByStatus;
    private Map<String, Long> countsByPriority;
    private Map<String, Long> countsByCategory;
    private long overdueCount;
    private long completedThisWeek;
    private long completedThisMonth;

    public TaskStatisticsDTO(Map<String, Long> countsByStatus,
                              Map<String, Long> countsByPriority,
                              Map<String, Long> countsByCategory,
                              long overdueCount,
                              long completedThisWeek,
                              long completedThisMonth) {
        this.countsByStatus = countsByStatus;
        this.countsByPriority = countsByPriority;
        this.countsByCategory = countsByCategory;
        this.overdueCount = overdueCount;
        this.completedThisWeek = completedThisWeek;
        this.completedThisMonth = completedThisMonth;
    }

    public Map<String, Long> getCountsByStatus() {
        return countsByStatus;
    }

    public Map<String, Long> getCountsByPriority() {
        return countsByPriority;
    }

    public Map<String, Long> getCountsByCategory() {
        return countsByCategory;
    }

    public long getOverdueCount() {
        return overdueCount;
    }

    public long getCompletedThisWeek() {
        return completedThisWeek;
    }

    public long getCompletedThisMonth() {
        return completedThisMonth;
    }

}