package com.quizapp.service;

import com.quizapp.dto.CategoryRequest;
import com.quizapp.dto.QuestionRequest;
import com.quizapp.entity.Category;
import com.quizapp.entity.Level;
import com.quizapp.entity.Question;
import com.quizapp.repository.CategoryRepository;
import com.quizapp.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Category Methods
    public Category addCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // Question Methods
    public Question addQuestion(QuestionRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (request.getOptionA() == null || request.getOptionA().trim().isEmpty() ||
            request.getOptionB() == null || request.getOptionB().trim().isEmpty() ||
            request.getOptionC() == null || request.getOptionC().trim().isEmpty() ||
            request.getOptionD() == null || request.getOptionD().trim().isEmpty()) {
            throw new RuntimeException("All options (A, B, C, D) must be provided and cannot be empty.");
        }

        Question question = new Question();
        question.setCategory(category);
        
        if (request.getLevel() != null && !request.getLevel().trim().isEmpty()) {
            question.setLevel(Level.valueOf(request.getLevel().toUpperCase()));
        } else {
            question.setLevel(Level.BASICS); // Default fallback
        }
        
        question.setText(request.getText());
        question.setOptionA(request.getOptionA().trim());
        question.setOptionB(request.getOptionB().trim());
        question.setOptionC(request.getOptionC().trim());
        question.setOptionD(request.getOptionD().trim());
        question.setCorrectOption(request.getCorrectOption());

        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
    
    public List<Question> getQuestionsByCategory(Long categoryId) {
        return questionRepository.findByCategoryId(categoryId);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
