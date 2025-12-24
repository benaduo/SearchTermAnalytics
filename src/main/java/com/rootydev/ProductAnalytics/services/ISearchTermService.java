package com.rootydev.ProductAnalytics.services;

import com.rootydev.ProductAnalytics.dtos.ApiResponse;
import com.rootydev.ProductAnalytics.dtos.PagedResult;
import com.rootydev.ProductAnalytics.dtos.SearchTermDto;
import com.rootydev.ProductAnalytics.dtos.SearchTermRequest;

public interface ISearchTermService {

    ApiResponse<PagedResult<SearchTermDto>> getAllSearchTerms(int pageIndex, int pageSize);

    ApiResponse<SearchTermDto> getSearchTermById(Long searchTermId);

    ApiResponse<Void> deleteSearchTermById(Long searchTermId);

    ApiResponse<SearchTermDto> createSearchTerm(SearchTermRequest request);

    ApiResponse<SearchTermDto> updateSearchTerm(Long searchTermId, SearchTermRequest request);
}
