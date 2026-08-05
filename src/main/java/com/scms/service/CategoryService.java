package com.scms.service;

import com.scms.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getActiveCategories();

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

}