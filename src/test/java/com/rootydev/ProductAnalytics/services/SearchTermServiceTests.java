package com.rootydev.ProductAnalytics.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.rootydev.ProductAnalytics.dtos.ApiResponse;
import com.rootydev.ProductAnalytics.dtos.PagedResult;
import com.rootydev.ProductAnalytics.dtos.SearchTermDto;
import com.rootydev.ProductAnalytics.dtos.SearchTermRequest;
import com.rootydev.ProductAnalytics.mappers.SearchTermMapper;
import com.rootydev.ProductAnalytics.models.SearchTerm;
import com.rootydev.ProductAnalytics.repositories.SearchTermRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchTermService Unit Tests")
public class SearchTermServiceTests {

    @Mock
    private SearchTermRepository searchTermRepository;

    @Mock
    private SearchTermMapper searchTermMapper;

    @Mock
    private ActorSystem actorSystem;

    @Mock
    private ActorRef auditLogActor;

    @Mock
    private ObjectMapper objectMapper;


    private SearchTermService searchTermService;

    private SearchTerm testSearchTerm;
    private SearchTermDto testSearchTermDto;
    private SearchTermRequest testSearchTermRequest;

    @BeforeEach
    public void setUp() {
        testSearchTerm = new SearchTerm();
        testSearchTerm.setId(1L);
        testSearchTerm.setSearchQuery("wireless headphones");
        testSearchTerm.setCreatedAt(Instant.now().toString());

        testSearchTermDto = new SearchTermDto();
        testSearchTermDto.setId(1L);
        testSearchTermDto.setSearchQuery("wireless headphones");
        testSearchTermDto.setCreatedAt(testSearchTerm.getCreatedAt());

        testSearchTermRequest = new SearchTermRequest();
        testSearchTermRequest.setSearchQuery("wireless headphones");

//        when(actorSystem.actorOf(any(), anyString())).thenReturn(auditLogActor);

        searchTermService = new SearchTermService(
                searchTermRepository,
                searchTermMapper,
                actorSystem,
                objectMapper,
                auditLogActor);

    }

    @Test
    @DisplayName("Should get all search terms with pagination")
    void getSearchTerms_ShouldReturnPagedResult() {
        // Arrange
        List<SearchTerm> searchTerms = Collections.singletonList(testSearchTerm);
        Page<SearchTerm> page = new PageImpl<>(searchTerms, PageRequest.of(0, 10), 1);

        when(searchTermRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(searchTermMapper.toDto(any(SearchTerm.class))).thenReturn(testSearchTermDto);

        // Act
        ApiResponse<PagedResult<SearchTermDto>> result = searchTermService.getAllSearchTerms(0, 10);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getResults()).hasSize(1);
        assertThat(result.getData().getPageIndex()).isZero();
        assertThat(result.getData().getPageSize()).isEqualTo(10);

        verify(searchTermRepository).findAll(any(Pageable.class));
        verify(searchTermMapper).toDto(testSearchTerm);

    }

    @Test
    @DisplayName("Should get search term by ID successfully")
    void getSearchTermById_ShouldReturnSearchTerm() {

        // Arrange
        when(searchTermRepository.findById(1L)).thenReturn(Optional.of(testSearchTerm));
        when(searchTermMapper.toDto(testSearchTerm)).thenReturn(testSearchTermDto);

        // Act
        ApiResponse<SearchTermDto> result = searchTermService.getSearchTermById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getId()).isEqualTo(1L);
        assertThat(result.getData().getSearchQuery()).isEqualTo("wireless headphones");

        verify(searchTermRepository).findById(1L);
        verify(searchTermMapper).toDto(testSearchTerm);
    }

    @Test
    @DisplayName("Should throw exception when search term not found")
    void getSearchTermById_WithInvalidId_ShouldReturnError() {

        // Arrange
        when(searchTermRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ApiResponse<SearchTermDto> result = searchTermService.getSearchTermById(1L);

        // Assert

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(404);

        verify(searchTermRepository).findById(1L);
        verify(searchTermMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should create search term successfully")
    void createSearchTerm_WithValidRequest_ShouldReturnCreatedSearchTerm() {
        // Arrange
        ObjectWriter mockWriter = mock(ObjectWriter.class);

        when(searchTermMapper.toEntity(testSearchTermRequest)).thenReturn(testSearchTerm);
        when(searchTermRepository.save(any(SearchTerm.class))).thenReturn(testSearchTerm);
        when(searchTermMapper.toDto(testSearchTerm)).thenReturn(testSearchTermDto);
        when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(mockWriter);
        when(mockWriter.writeValueAsString(any())).thenReturn("{}");

        // Act
        ApiResponse<SearchTermDto> result = searchTermService.createSearchTerm(testSearchTermRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getSearchQuery()).isEqualTo("wireless headphones");

        verify(searchTermMapper).toEntity(testSearchTermRequest);
        verify(searchTermRepository).save(any(SearchTerm.class));
        verify(searchTermMapper).toDto(testSearchTerm);
        verify(objectMapper).writerWithDefaultPrettyPrinter();
        verify(mockWriter).writeValueAsString(any());
    }


    @Test
    @DisplayName("Should update search term successfully")
    void updateSearchTerm_WithValidData_ShouldReturnUpdatedSearchTerm() {
        // Arrange
        SearchTermRequest updateRequest = new SearchTermRequest();
        updateRequest.setSearchQuery("bluetooth headphones");

        when(searchTermRepository.findById(1L)).thenReturn(Optional.of(testSearchTerm));
        when(searchTermRepository.save(any(SearchTerm.class))).thenReturn(testSearchTerm);
        when(searchTermMapper.toDto(any(SearchTerm.class))).thenReturn(testSearchTermDto);
        doNothing().when(searchTermMapper).updateEntityFromDto(any(), any());

        // Act
        ApiResponse<SearchTermDto> result = searchTermService.updateSearchTerm(1L, updateRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getData()).isNotNull();

        verify(searchTermRepository).findById(1L);
        verify(searchTermMapper).updateEntityFromDto(updateRequest, testSearchTerm);
        verify(searchTermRepository).save(testSearchTerm);
    }

    @Test
    @DisplayName("Should return error when updating non-existent search term")
    void updateSearchTerm_WithInvalidId_ShouldReturnError() {
        // Arrange
        when(searchTermRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ApiResponse<SearchTermDto> result = searchTermService.updateSearchTerm(999L, testSearchTermRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(404);

        verify(searchTermRepository).findById(999L);
        verify(searchTermRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete search term successfully")
    void deleteSearchTermById_WithValidId_ShouldDeleteSuccessfully() {
        // Arrange
        when(searchTermRepository.findById(1L)).thenReturn(Optional.of(testSearchTerm));
        doNothing().when(searchTermRepository).deleteById(1L);

        // Act
        ApiResponse<Void> result = searchTermService.deleteSearchTermById(1L);

        // Assert
        assertThat(result).isNotNull();

        verify(searchTermRepository).findById(1L);
        verify(searchTermRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should return error when deleting non-existent search term")
    void deleteSearchTermById_WithInvalidId_ShouldReturnError() {
        // Arrange
        when(searchTermRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ApiResponse<Void> result = searchTermService.deleteSearchTermById(999L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(404);

        verify(searchTermRepository).findById(999L);
        verify(searchTermRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should handle pagination with empty results")
    void getAllSearchTerms_WithNoData_ShouldReturnEmptyPage() {
        // Arrange
        Page<SearchTerm> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(searchTermRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        ApiResponse<PagedResult<SearchTermDto>> result = searchTermService.getAllSearchTerms(0, 10);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getData().getResults()).isEmpty();
        assertThat(result.getData().getTotalCount()).isZero();

        verify(searchTermRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle large page numbers")
    void getAllSearchTerms_WithLargePageNumber_ShouldReturnEmptyPage() {
        // Arrange
        Page<SearchTerm> emptyPage = new PageImpl<>(List.of(), PageRequest.of(100, 10), 1);
        when(searchTermRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        ApiResponse<PagedResult<SearchTermDto>> result = searchTermService.getAllSearchTerms(100, 10);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getData().getResults()).isEmpty();

        verify(searchTermRepository).findAll(any(Pageable.class));
    }

}
