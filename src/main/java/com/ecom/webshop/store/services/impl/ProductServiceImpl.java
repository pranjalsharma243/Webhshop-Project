package com.ecom.webshop.store.services.impl;

import com.ecom.webshop.store.dtos.PageableResponse;
import com.ecom.webshop.store.dtos.ProductDto;
import com.ecom.webshop.store.entities.Category;
import com.ecom.webshop.store.entities.Product;
import com.ecom.webshop.store.exceptions.ResourceNotFoundException;
import com.ecom.webshop.store.helper.Helper;
import com.ecom.webshop.store.repositories.CategoryRepository;
import com.ecom.webshop.store.repositories.ProductRepository;
import com.ecom.webshop.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("images/products/")
    private String imagePath;
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        //productId
       String productId= UUID.randomUUID().toString();
       product.setProductId(productId);
       //added date
        product.setAddedDate(new Date());
        Product savedProduct=productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {

//fetch product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id!!"));
        product.setTitle(product.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());
        //save the entity
        Product updatedProduct=productRepository.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        //get the product first

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id!!"));
        //delete image
        String fullPath=imagePath+product.getProductImageName();
        System.out.println(fullPath);
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }
        catch(NoSuchFileException ex){
            ex.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        productRepository.delete(product);


    }

    @Override
    public ProductDto get(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id!!"));

        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getALl(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findAll(pageable);
        return Helper.getPageableReponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByLiveTrue(pageable);
        return Helper.getPageableReponse(page,ProductDto.class);

    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByTitleContaining(subTitle,pageable);
        return Helper.getPageableReponse(page,ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        //fetch the category
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with the give id not present!!"));
        Product product = mapper.map(productDto, Product.class);
        //productId
        String productId= UUID.randomUUID().toString();
        product.setProductId(productId);
        //added date
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product savedProduct=productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);

    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        //product fetch
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given id not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given id not found"));
        product.setCategory(category);
        Product savedProduct=productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProdOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given id not found"));
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByCategory(category,pageable);
        return Helper.getPageableReponse(page,ProductDto.class);
    }

    //update category of product

}
