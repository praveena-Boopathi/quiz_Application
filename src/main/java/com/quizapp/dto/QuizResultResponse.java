package com.quizapp.dto;

import java.util.Map;

public class QuizResultResponse {
    private Integer score;
    private Integer totalQuestions;
    private Map<Long, String> correctAnswers;

    public QuizResultResponse() {}

    public QuizResultResponse(Integer score, Integer totalQuestions, Map<Long, String> correctAnswers) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
    }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Map<Long, String> getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(Map<Long, String> correctAnswers) { this.correctAnswers = correctAnswers; }
}
