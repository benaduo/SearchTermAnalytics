package com.rootydev.ProductAnalytics.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchTermFilter {
    private String query;
    private String startDate;
    private String endDate;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";
}
