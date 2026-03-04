package com.personalproject.task_service.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_tasks")
public class ProjectTask extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    // Just store ID
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "sprint_id")
    private Long sprintId;

    @Column(name = "user_create_id")
    private Long userCreateId;

    private String name;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    private LocalDateTime deadline;

    public ProjectTask(LocalDateTime createdAt, LocalDateTime updatedAt, Boolean deleted, Long taskId,
                       Long projectId, Long sprintId, Long userCreateId, String name,
                       TaskStatus status, TaskPriority priority, TaskType type, LocalDateTime startDate, LocalDateTime deadline) {
        super(createdAt, updatedAt, deleted);
        this.taskId = taskId;
        this.projectId = projectId;
        this.sprintId = sprintId;
        this.userCreateId = userCreateId;
        this.name = name;
        this.status = status;
        this.priority = priority;
        this.type = type;
        this.startDate = startDate;
        this.deadline = deadline;
    }
}
