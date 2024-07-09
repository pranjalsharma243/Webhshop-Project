package com.ecom.webshop.store.services;

import com.ecom.webshop.store.dtos.CategoryDto;
import com.ecom.webshop.store.dtos.PageableResponse;

public interface CategoryService {


    //create
    CategoryDto create(CategoryDto categoryDto);


    //update
    CategoryDto update(CategoryDto categoryDto,String categoryId);

    //delete
    void delete(String categoryId);

    //get all categories
    PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    //get single category detail
    CategoryDto get(String categoryId);

    //search
}
