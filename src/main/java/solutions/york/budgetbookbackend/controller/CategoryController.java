package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.york.budgetbookbackend.dto.category.CategoryRequest;
import solutions.york.budgetbookbackend.dto.category.CategoryResponse;
import solutions.york.budgetbookbackend.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestHeader("Authorization") String token, @RequestBody CategoryRequest request) {
        CategoryResponse categoryResponse= categoryService.createCategory(token, request);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCustomerCategories(@RequestHeader("Authorization") String token) {
        List<CategoryResponse> categoryResponses = categoryService.getCustomerCategories(token);
        return ResponseEntity.ok(categoryResponses);
    }
}
