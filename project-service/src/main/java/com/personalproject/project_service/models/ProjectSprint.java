package com.personalproject.project_service.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_sprints")
public class ProjectSprint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long sprintId;

    // Many sprints belong to one project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private SprintStatus status;

    private Integer points;

    @Column(name = "percentage_done")
    private Double percentageDone;

    @Column(name = "user_create_id")
    private Long createdByUser;

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SprintStatus getStatus() {
        return status;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Double getPercentageDone() {
        return percentageDone;
    }

    public void setPercentageDone(Double percentageDone) {
        this.percentageDone = percentageDone;
    }

    public Long getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(Long createdByUser) {
        this.createdByUser = createdByUser;
    }

    @Override
    public String toString() {
        return "ProjectSprint{" +
                "sprintId=" + sprintId +
                ", project=" + project +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", points=" + points +
                ", percentageDone=" + percentageDone +
                ", createdByUser=" + createdByUser +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
    }

    public ProjectSprint(LocalDateTime createdAt, LocalDateTime updatedAt, Boolean deleted, Long sprintId, Project project,
                         String name, String description, SprintStatus status, Integer points, Double percentageDone, Long createdByUser) {
        super(createdAt, updatedAt, deleted);
        this.sprintId = sprintId;
        this.project = project;
        this.name = name;
        this.description = description;
        this.status = status;
        this.points = points;
        this.percentageDone = percentageDone;
        this.createdByUser = createdByUser;
    }
}
