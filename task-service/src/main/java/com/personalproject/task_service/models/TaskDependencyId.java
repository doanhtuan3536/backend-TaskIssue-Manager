package com.personalproject.task_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class TaskDependencyId implements Serializable {
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "depends_task_id")
    private Long dependsTaskId;
}
