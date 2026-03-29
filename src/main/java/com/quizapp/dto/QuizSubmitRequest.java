package com.quizapp.dto;

import java.util.Map;

public class QuizSubmitRequest {
    private Long categoryId;
    private Map<Long, String> answers;
    private String level;

    public QuizSubmitRequest() {}

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> answers) { this.answers = answers; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
