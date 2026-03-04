package com.personalproject.task_service.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_dependencies")
public class TaskDependency extends BaseEntity {
    @EmbeddedId
    private TaskDependencyId id;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private ProjectTask task;

    @ManyToOne
    @MapsId("dependsTaskId")
    @JoinColumn(name = "depends_task_id")
    private ProjectTask dependsTask;

    private String description;

    public TaskDependency(LocalDateTime createdAt, LocalDateTime updatedAt, Boolean deleted, TaskDependencyId id,
                          ProjectTask task, ProjectTask dependsTask, String description) {
        super(createdAt, updatedAt, deleted);
        this.id = id;
        this.task = task;
        this.dependsTask = dependsTask;
        this.description = description;
    }
}
