package com.quizapp.controller;

import com.quizapp.dto.QuestionDto;
import com.quizapp.dto.QuizAttemptDto;
import com.quizapp.dto.QuizResultResponse;
import com.quizapp.dto.QuizSubmitRequest;
import com.quizapp.service.QuizService;
import com.quizapp.repository.CategoryRepository;
import com.quizapp.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class QuizController {

    @Autowired
    private QuizService quizService;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/quiz/{categoryId}/{level}")
    public ResponseEntity<List<QuestionDto>> getQuizQuestions(@PathVariable Long categoryId, @PathVariable String level) {
        return ResponseEntity.ok(quizService.getQuizQuestions(categoryId, level));
    }

    @PostMapping("/quiz/submit")
    public ResponseEntity<QuizResultResponse> submitQuiz(@RequestBody QuizSubmitRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return ResponseEntity.ok(quizService.submitQuiz(username, request));
    }

    @GetMapping("/dashboard/attempts")
    public ResponseEntity<List<QuizAttemptDto>> getUserAttempts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return ResponseEntity.ok(quizService.getUserAttempts(username));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<QuizAttemptDto>> getLeaderboard() {
        return ResponseEntity.ok(quizService.getLeaderboard());
    }
}
