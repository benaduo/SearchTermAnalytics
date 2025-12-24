package com.rootydev.ProductAnalytics.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResult<T> {
    private int pageIndex = 1;
    private int pageSize = 10;
    private int totalCount;
    private List<T> results;

    public static <T> PagedResult<T> of(int pageIndex, int pageSize, int totalCount, List<T> results) {
        return new PagedResult<>(pageIndex, pageSize, totalCount, results);
    }
}
