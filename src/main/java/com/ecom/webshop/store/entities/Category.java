package com.ecom.webshop.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="categories")
public class Category {
    @Id
    @Column(name="id")
    private String categoryId;
    @Column(name="category_title",length = 80)
    private String title;
    @Column(name="category_desc",length = 1000)
    private String description;
    private String CoverImage;
    @OneToMany(mappedBy ="category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products=new ArrayList<>();







}
