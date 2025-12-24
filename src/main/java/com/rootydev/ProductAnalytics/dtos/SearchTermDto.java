package com.rootydev.ProductAnalytics.dtos;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class SearchTermDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
