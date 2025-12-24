package com.rootydev.ProductAnalytics.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "search_terms", indexes = {
@Index(name = "idx_search_query", columnList = "search_query"),
@Index(name = "idx_created_at", columnList = "created_at")
})
public class SearchTerm {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "search_query", nullable = false)
    private String searchQuery;
    @Column(name = "city")
    private String city;
    @Column(name = "station")
    private String station;
    @Column(name = "zone")
    private String zone;
    @Column(name = "is_unified_search")
    private Boolean isUnifiedSearch = false;
    @Column(name = "item_type")
    private String itemType;
    @Column(name = "correct_word")
    private String correctWord;
    @Column(name = "is_managed")
    private Boolean isManaged;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "updated_at")
    private String updatedAt;
}
