package com.rootydev.ProductAnalytics.services;

import com.rootydev.ProductAnalytics.dtos.ApiResponse;
import com.rootydev.ProductAnalytics.dtos.PagedResult;
import com.rootydev.ProductAnalytics.dtos.SearchTermDto;
import com.rootydev.ProductAnalytics.dtos.SearchTermRequest;
import com.rootydev.ProductAnalytics.models.SearchTerm;
import com.rootydev.ProductAnalytics.repositories.SearchTermRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SearchTermService Integration Tests")
class SearchTermServiceIntegrationTest {

    @Autowired
    private SearchTermService searchTermService;

    @Autowired
    private SearchTermRepository searchTermRepository;

    @AfterEach
    void cleanup() {
        searchTermRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create and retrieve search term")
    void createAndRetrieve_ShouldWorkEndToEnd() {
        // Arrange
        SearchTermRequest request = new SearchTermRequest();
        request.setSearchQuery("integration test query");

        // Act - Create
        ApiResponse<SearchTermDto> createResponse = searchTermService.createSearchTerm(request);

        // Assert - Create
        assertThat(createResponse.getData()).isNotNull();
        Long createdId = createResponse.getData().getId();

        // Act - Retrieve
        ApiResponse<SearchTermDto> getResponse = searchTermService.getSearchTermById(createdId);

        // Assert - Retrieve
        assertThat(getResponse.getData().getSearchQuery()).isEqualTo("integration test query");
    }

    @Test
    @DisplayName("Should create, update, and verify changes")
    void createUpdateAndVerify_ShouldPersistChanges() {
        // Arrange
        SearchTermRequest createRequest = new SearchTermRequest();
        createRequest.setSearchQuery("original query");

        // Act - Create
        ApiResponse<SearchTermDto> createResponse = searchTermService.createSearchTerm(createRequest);
        Long id = createResponse.getData().getId();

        // Arrange - Update
        SearchTermRequest updateRequest = new SearchTermRequest();
        updateRequest.setSearchQuery("updated query");

        // Act - Update
        ApiResponse<SearchTermDto> updateResponse = searchTermService.updateSearchTerm(id, updateRequest);

        // Assert - Update
        assertThat(updateResponse.getData().getSearchQuery()).isEqualTo("updated query");

        // Verify persistence
        SearchTerm updatedEntity = searchTermRepository.findById(id).orElseThrow();
        assertThat(updatedEntity.getSearchQuery()).isEqualTo("updated query");
    }

    @Test
    @DisplayName("Should handle pagination correctly with real data")
    void pagination_WithMultipleRecords_ShouldReturnCorrectPages() {
        // Arrange - Create multiple records
        for (int i = 1; i <= 15; i++) {
            SearchTermRequest request = new SearchTermRequest();
            request.setSearchQuery("query " + i);
            searchTermService.createSearchTerm(request);
        }

        // Act - Get first page
        ApiResponse<PagedResult<SearchTermDto>> page1 = searchTermService.getAllSearchTerms(0, 10);

        // Assert - First page
        assertThat(page1.getData().getResults()).hasSize(10);

        // Act - Get second page
        ApiResponse<PagedResult<SearchTermDto>> page2 = searchTermService.getAllSearchTerms(1, 10);

        // Assert - Second page
        assertThat(page2.getData().getResults()).hasSize(5);
    }
}
