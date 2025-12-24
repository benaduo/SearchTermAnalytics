package com.rootydev.ProductAnalytics.dtos;

import lombok.Data;

@Data
public class SearchTermRequest {
    private String searchQuery;
    private String city;
    private String station;
    private String zone;
    private Boolean isUnifiedSearch;
    private String itemType;
    private String correctWord;
    private Boolean isManaged;
}
