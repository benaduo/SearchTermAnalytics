package com.rootydev.ProductAnalytics.dtos;

import lombok.Data;

@Data
public class SearchTermDto {
    private Long id;
    private String searchQuery;
    private String city;
    private String station;
    private String zone;
    private Boolean isUnifiedSearch = false;
    private String itemType;
    private String correctWord;
    private Boolean isManaged;
    private String createdAt;
    private String updatedAt;
}
