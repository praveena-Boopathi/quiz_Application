package com.quizapp.dto;

import java.time.LocalDateTime;

public class QuizAttemptDto {
    private Long id;
    private String categoryName;
    private Integer score;
    private Integer totalQuestions;
    private String username;
    private LocalDateTime submitTime;

    public QuizAttemptDto() {}

    public QuizAttemptDto(Long id, String categoryName, Integer score, Integer totalQuestions, String username, LocalDateTime submitTime) {
        this.id = id;
        this.categoryName = categoryName;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.username = username;
        this.submitTime = submitTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getSubmitTime() { return submitTime; }
    public void setSubmitTime(LocalDateTime submitTime) { this.submitTime = submitTime; }
}
