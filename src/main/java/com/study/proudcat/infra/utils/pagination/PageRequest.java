package com.study.proudcat.infra.utils.pagination;

import org.springframework.data.domain.Pageable;

public abstract class PageRequest {

    private int page;
    private int size;

    public PageRequest() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(){
        return org.springframework.data.domain.PageRequest.of(page -1, size);
    }
}
