package com.ecom.webshop.store.controllers;

import com.ecom.webshop.store.dtos.*;
import com.ecom.webshop.store.services.FileService;
import com.ecom.webshop.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${product.image.path}")
    private String imagePath;

    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid  @RequestBody ProductDto productDto){
        ProductDto createdProduct = productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);

    }


    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,@PathVariable String productId){
        ProductDto updatedProduct = productService.update(productDto,productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }



    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId){

        productService.delete(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Product is deleted successfully!!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //get Single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId){
        ProductDto productDto = productService.get(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);

    }



    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                       @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
                                                                       @RequestParam(value="sortBy",defaultValue = "title",required = false)  String sortBy,
                                                                       @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
        PageableResponse pageableResponse = productService.getALl(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);

    }


    //get all live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                       @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
                                                                       @RequestParam(value="sortBy",defaultValue = "title",required = false)  String sortBy,
                                                                       @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
        PageableResponse pageableResponse = productService.getAllLive(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);

    }


    //search all
    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(@PathVariable String query,@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                           @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
                                                                           @RequestParam(value="sortBy",defaultValue = "title",required = false)  String sortBy,
                                                                           @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
        PageableResponse pageableResponse = productService.searchByTitle(query,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);

    }
    //upload image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,@RequestParam("productImage") MultipartFile image
    ) throws IOException {
        String fileName = fileService.uploadImage(image, imagePath);
        ProductDto productDto = productService.get(productId);
        productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName()).message("Product image is successfully uploaded!!").status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);

    }


    //serve image
    @GetMapping("/image/{productId}")
    public void serverProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        //get product
        ProductDto productDto=productService.get(productId);

        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }








}
