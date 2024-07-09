package com.ecom.webshop.store.controllers;

import com.ecom.webshop.store.dtos.ApiResponseMessage;
import com.ecom.webshop.store.dtos.CategoryDto;
import com.ecom.webshop.store.dtos.PageableResponse;
import com.ecom.webshop.store.dtos.ProductDto;
import com.ecom.webshop.store.entities.Category;
import com.ecom.webshop.store.services.CategoryService;
import com.ecom.webshop.store.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        //call service to save obj
        CategoryDto categoryDto1=categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);

    }


    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable  String categoryId ,@Valid @RequestBody CategoryDto categoryDto){

        CategoryDto updatedCategory=categoryService.update(categoryDto,categoryId);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);

    }



    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted Successfully!!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);


    }



    //get all cat
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value="pageNumber",defaultValue  ="0",required = false ) int pageNumber,
            @RequestParam(value="pageSize",defaultValue  ="5",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue  ="title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue  ="asc",required = false) String sortDir
    ){
      PageableResponse<CategoryDto> pageableResponse=  categoryService.getAll(pageNumber,pageSize,sortBy,sortDir);
      return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }


    //get one category
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String categoryId){
        CategoryDto categoryDto=categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }

    //create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable("categoryId") String categoryId,@RequestBody ProductDto productDto){
        ProductDto productwithCategory = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productwithCategory,HttpStatus.CREATED);

    }
    //update category of product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(
            @PathVariable String categoryId,
            @PathVariable String productId
    ){
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    //get products of categories
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value="pageNumber",defaultValue  ="0",required = false ) int pageNumber,
            @RequestParam(value="pageSize",defaultValue  ="5",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue  ="title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue  ="asc",required = false) String sortDir){
        PageableResponse<ProductDto> response = productService.getAllProdOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }



}
