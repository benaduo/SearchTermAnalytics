package com.rootydev.ProductAnalytics.repositories;

import com.rootydev.ProductAnalytics.models.SearchTerm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchTermRepository extends JpaRepository<SearchTerm, Long> {
}
