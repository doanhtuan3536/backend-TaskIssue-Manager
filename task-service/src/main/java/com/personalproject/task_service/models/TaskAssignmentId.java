package com.personalproject.task_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class TaskAssignmentId implements Serializable {
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "user_id")
    private Long userId;
}
