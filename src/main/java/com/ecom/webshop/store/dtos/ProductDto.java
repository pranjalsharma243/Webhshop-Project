package com.ecom.webshop.store.dtos;

import com.ecom.webshop.store.entities.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

    private String productId;

    private String title;

    private String description;

    private double price;

    private int quantity;

    private Date addedDate;

    private boolean live;
    private boolean stock;

    private double discountedPrice;
    private String productImageName;

    private CategoryDto category;

}
