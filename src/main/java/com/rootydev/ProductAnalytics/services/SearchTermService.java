package com.rootydev.ProductAnalytics.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.rootydev.ProductAnalytics.actors.AuditLogActor;
import com.rootydev.ProductAnalytics.config.SpringExtension;
import com.rootydev.ProductAnalytics.dtos.*;
import com.rootydev.ProductAnalytics.mappers.SearchTermMapper;
import com.rootydev.ProductAnalytics.models.SearchTerm;
import com.rootydev.ProductAnalytics.repositories.SearchTermRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Slf4j
@Service
public class SearchTermService implements ISearchTermService {
    private static final Logger logger = LoggerFactory.getLogger(SearchTermService.class);
    private final SearchTermRepository searchTermRepository;
    private final SearchTermMapper searchTermMapper;
    private final ActorRef auditLogActor;
    private final ObjectMapper objectMapper;
    private final ActorSystem actorSystem;

    @Autowired
    public SearchTermService(SearchTermRepository searchTermRepository, SearchTermMapper searchTermMapper, ActorSystem actorSystem, ObjectMapper objectMapper) {
        this.searchTermRepository = searchTermRepository;
        this.searchTermMapper = searchTermMapper;
        this.auditLogActor = actorSystem.actorOf(
                SpringExtension.SPRING_EXTENSION_PROVIDER.get(actorSystem).props(AuditLogActor.class),
                "AuditLogActor"
        );
        this.objectMapper = objectMapper;
        this.actorSystem = actorSystem;
    }

    // Test constructor - package-private, used only in tests
    SearchTermService(SearchTermRepository searchTermRepository,
                      SearchTermMapper searchTermMapper,
                      ActorSystem actorSystem,
                      ObjectMapper objectMapper,
                      ActorRef auditLogActor) {
        this.searchTermRepository = searchTermRepository;
        this.searchTermMapper = searchTermMapper;
        this.actorSystem = actorSystem;
        this.objectMapper = objectMapper;
        this.auditLogActor = auditLogActor;
    }

    @Transactional(readOnly = true)
    public ApiResponse<PagedResult<SearchTermDto>> getAllSearchTerms(int pageIndex, int pageSize) {
        log.debug("Fetching search terms - page: {}, size: {}", pageIndex, pageSize);

        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SearchTerm> page = searchTermRepository.findAll(pageable);

        PagedResult<SearchTermDto> result = new PagedResult<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getContent().stream().map(searchTermMapper::toDto).toList()
        );

        log.info("Retrieved {} search terms", result.getTotalCount());
        return ApiResponse.success(result);
    }
/*
    @Transactional(readOnly = true)
    public ApiResponse<PagedResult<SearchTermDto>> searchWithFilters(SearchTermFilter filter, int pageIndex, int pageSize) {
        log.debug("Searching with filters: {}", filter);

        // Build sort
        Sort sort = Sort.by(
                "ASC".equalsIgnoreCase(filter.getSortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC,
                filter.getSortBy()
        );

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Specification<SearchTerm> spec = SearchTermSpecification.withFilter(filter);

        Page<SearchTerm> page = searchTermRepository.findAll(spec, pageable);

        PagedResult<SearchTermDto> result = new PagedResult<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getContent().stream().map(searchTermMapper::toDto).toList()
        );

        log.info("Found {} search terms matching filters", result.getTotalCount());
        return ApiResponse.success(result);
    }
*/
/*
    public ApiResponse<PagedResult<SearchTermDto>> getAllSearchTerms(int page, int size) {
        try {
            log.debug("Fetching search terms - page: {}, size: {}", page, size);

            List<SearchTermDto> searchTerms = searchTermRepository.findAll()
                    .stream()
                    .map(searchTermMapper::toDto)
                    .toList();
            logger.info("Found {} search terms from DB", searchTerms.size());
            PagedResult<SearchTermDto> pagedResult = new PagedResult<>(page, size, searchTerms.size(), searchTerms);
            return ApiResponse.success(pagedResult);
        } catch (Exception e) {
            logger.error("Error fetching search terms: {}", e.getMessage());
            return ApiResponse.error("Internal Server Error", 500, e.getMessage());
        }

    }
*/
    public ApiResponse<SearchTermDto> getSearchTermById(Long searchTermId) {
        try {
            logger.debug("Getting search term with id {}", searchTermId);
//            SearchTerm searchTerm = searchTermRepository.findById(searchTermId).orElseThrow(()-> new ResourceNotFoundException("SearchTerm", "id", searchTermId));
            SearchTerm searchTerm = searchTermRepository.findById(searchTermId).orElse(null);
            if (searchTerm == null) {
                return ApiResponse.error("Search term not found", 404, "NOT_FOUND", null);
            }
            logger.info("Found search term with id {}", searchTermId);
            return ApiResponse.success(searchTermMapper.toDto(searchTerm));
        } catch (Exception e) {
            logger.error("Error fetching search term: {}", e.getMessage());
            return ApiResponse.error("Internal Server Error", 500, e.getMessage());
        }
    }

    public ApiResponse<Void> deleteSearchTermById(Long searchTermId) {
        try {
            logger.debug("Deleting search term with id {}", searchTermId);
            SearchTerm searchTerm = searchTermRepository.findById(searchTermId).orElse(null);
            if (searchTerm == null) {
                return ApiResponse.error("Search term not found", 404, "NOT_FOUND", null);
            }
            searchTermRepository.deleteById(searchTermId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            logger.error("Error deleting search term: {}", e.getMessage());
            return ApiResponse.error("Internal Server Error", 500, e.getMessage());
        }
    }

    public ApiResponse<SearchTermDto> createSearchTerm(SearchTermRequest request) {


        try {
            logger.debug("Creating search term with request {}", request);

            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(request);

            auditLogActor.tell("Processing search term: " + prettyJson, ActorRef.noSender());

            SearchTerm searchTerm = searchTermMapper.toEntity(request);
            searchTerm.setCreatedAt(Instant.now().toString());
            searchTermRepository.save(searchTerm);
            return ApiResponse.success(searchTermMapper.toDto(searchTerm));
        } catch (JacksonException e) {
            logger.error("Error creating search term: {}", e.getMessage());
            return ApiResponse.error("Internal Server Error", 500, e.getMessage());
        }
    }

    public ApiResponse<SearchTermDto> updateSearchTerm(Long searchTermId, SearchTermRequest request) {
        try {
            logger.debug("Updating search term with id {} and \n request {}", searchTermId, request);
            SearchTerm searchTerm = searchTermRepository.findById(searchTermId).orElse(null);
            if (searchTerm == null) {
                return ApiResponse.error("Search term not found", 404, "NOT_FOUND", null);
            }
            searchTermMapper.updateEntityFromDto(request, searchTerm);
            searchTerm.setUpdatedAt(Instant.now().toString());
            searchTermRepository.save(searchTerm);
            return ApiResponse.success(searchTermMapper.toDto(searchTerm));
        } catch (Exception e) {
            logger.error("Error updating search term: {}", e.getMessage());
            return ApiResponse.error("Internal Server Error", 500, e.getMessage());
        }
    }


}
