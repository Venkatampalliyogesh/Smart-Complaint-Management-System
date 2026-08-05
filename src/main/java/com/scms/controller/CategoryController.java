package com.scms.controller;

import com.scms.dto.CategoryDTO;
import com.scms.service.CategoryService;
import com.scms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get Active Categories")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getActiveCategories() {

        List<CategoryDTO> categories = categoryService.getActiveCategories();

        return ResponseEntity.ok(
                ApiResponse.<List<CategoryDTO>>builder()
                        .success(true)
                        .message("Categories Retrieved Successfully")
                        .data(categories)
                        .build());
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Categories")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {

        List<CategoryDTO> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(
                ApiResponse.<List<CategoryDTO>>builder()
                        .success(true)
                        .message("All Categories Retrieved Successfully")
                        .data(categories)
                        .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Category By Id")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(
            @PathVariable Long id) {

        CategoryDTO category = categoryService.getCategoryById(id);

        return ResponseEntity.ok(
                ApiResponse.<CategoryDTO>builder()
                        .success(true)
                        .message("Category Retrieved Successfully")
                        .data(category)
                        .build());
    }

    @PostMapping
    @Operation(summary = "Create Category")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(
            @RequestBody CategoryDTO categoryDTO) {

        CategoryDTO category = categoryService.createCategory(categoryDTO);

        return ResponseEntity.ok(
                ApiResponse.<CategoryDTO>builder()
                        .success(true)
                        .message("Category Created Successfully")
                        .data(category)
                        .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Category")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO) {

        CategoryDTO category = categoryService.updateCategory(id, categoryDTO);

        return ResponseEntity.ok(
                ApiResponse.<CategoryDTO>builder()
                        .success(true)
                        .message("Category Updated Successfully")
                        .data(category)
                        .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Category")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Category Deleted Successfully")
                        .build());
    }

}