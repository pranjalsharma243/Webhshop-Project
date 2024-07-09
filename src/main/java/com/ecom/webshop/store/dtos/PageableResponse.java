package com.ecom.webshop.store.dtos;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;

    private long totalElements;
    private long totalPages;
    private boolean islastPage;

}
