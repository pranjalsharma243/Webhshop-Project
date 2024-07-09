package com.ecom.webshop.store.services.impl;

import com.ecom.webshop.store.dtos.CategoryDto;
import com.ecom.webshop.store.dtos.PageableResponse;
import com.ecom.webshop.store.entities.Category;
import com.ecom.webshop.store.exceptions.ResourceNotFoundException;
import com.ecom.webshop.store.helper.Helper;
import com.ecom.webshop.store.repositories.CategoryRepository;
import com.ecom.webshop.store.services.CategoryService;
import org.apache.coyote.http11.HeadersTooLargeException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        //create Cat id at random
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category= mapper.map(categoryDto, Category.class);
       Category savedCategory=categoryRepository.save(category);
       return mapper.map(savedCategory,CategoryDto.class);

    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        //get category by id
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category not found with this Id!!"));
        //update category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory=categoryRepository.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);



    }

    @Override
    public void delete(String categoryId) {
        //get by id
        //get category by id
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category not found with this Id!!"));
        categoryRepository.delete(category);

    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<Category> page=categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableReponse=Helper.getPageableReponse(page,CategoryDto.class);

        return pageableReponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category not found with this Id!!"));
        return mapper.map(category,CategoryDto.class);
    }
}
