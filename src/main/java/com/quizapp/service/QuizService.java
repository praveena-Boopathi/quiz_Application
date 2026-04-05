package com.quizapp.service;

import com.quizapp.dto.QuestionDto;
import com.quizapp.dto.QuizAttemptDto;
import com.quizapp.dto.QuizResultResponse;
import com.quizapp.dto.QuizSubmitRequest;
import com.quizapp.entity.Category;
import com.quizapp.entity.Question;
import com.quizapp.entity.QuizAttempt;
import com.quizapp.entity.Level;
import com.quizapp.entity.User;
import com.quizapp.repository.CategoryRepository;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.QuizAttemptRepository;
import com.quizapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserRepository userRepository;

    public List<QuestionDto> getQuizQuestions(Long categoryId, String levelStr) {
        Level level = Level.valueOf(levelStr.toUpperCase());
        List<Question> questions = questionRepository.findByCategoryIdAndLevel(categoryId, level);
        
        Collections.shuffle(questions);
        
        int limit = level == Level.BASICS ? 5 : (level == Level.MEDIUM ? 10 : 15);
        
        return questions.stream()
                .limit(limit)
                .map(q -> new QuestionDto(
                        q.getId(),
                        q.getText(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD(),
                        q.getCorrectOption()))
                .collect(Collectors.toList());
    }

    public QuizResultResponse submitQuiz(String username, QuizSubmitRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Level level = Level.valueOf(request.getLevel().toUpperCase());
        List<Question> allQuestions = questionRepository.findByCategoryIdAndLevel(category.getId(), level);
        int limit = level == Level.BASICS ? 5 : (level == Level.MEDIUM ? 10 : 15);
        
        int score = 0;
        Map<Long, String> correctAnswers = new HashMap<>();

        if (request.getAnswers() != null) {
            for (Question q : allQuestions) {
                if (request.getAnswers().containsKey(q.getId())) {
                    correctAnswers.put(q.getId(), q.getCorrectOption());
                    String submittedAnswer = request.getAnswers().get(q.getId());
                    if (submittedAnswer != null && submittedAnswer.equalsIgnoreCase(q.getCorrectOption())) {
                        score++;
                    }
                }
            }
        }
        
        score = Math.min(score, limit);

        // Save Attempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setCategory(category);
        attempt.setLevel(level);
        attempt.setScore(score);
        attempt.setTotalQuestions(limit);
        attempt.setStartTime(LocalDateTime.now()); // In a real app, startTime comes from a prior request
        attempt.setSubmitTime(LocalDateTime.now());
        attempt.setCompleted(true);
        quizAttemptRepository.save(attempt);

        return new QuizResultResponse(score, limit, correctAnswers);
    }

    public List<QuizAttemptDto> getUserAttempts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<QuizAttempt> attempts = quizAttemptRepository.findByUserIdOrderByStartTimeDesc(user.getId());
        return attempts.stream()
                .map(a -> new QuizAttemptDto(
                        a.getId(),
                        a.getCategory().getName(),
                        a.getScore(),
                        a.getTotalQuestions(),
                        a.getUser().getUsername(),
                        a.getSubmitTime()))
                .collect(Collectors.toList());
    }

    public List<QuizAttemptDto> getLeaderboard() {
        List<QuizAttempt> attempts = quizAttemptRepository.findAllByOrderByScoreDesc();
        return attempts.stream()
                .map(a -> new QuizAttemptDto(
                        a.getId(),
                        a.getCategory().getName(),
                        a.getScore(),
                        a.getTotalQuestions(),
                        a.getUser().getUsername(),
                        a.getSubmitTime()))
                .limit(50) // Top 50
                .collect(Collectors.toList());
    }
}
