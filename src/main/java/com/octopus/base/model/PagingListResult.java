package com.octopus.base.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class PagingListResult<T> {
    
    private List<T>    list = new ArrayList<>();
    private Pagination pagination;
    
    public PagingListResult(List<T> list, Pagination pagination) {
        this.list.addAll(list);
        this.pagination = pagination;
    }
    
}
