package com.rootydev.ProductAnalytics.controllers;

import com.rootydev.ProductAnalytics.dtos.ApiResponse;
import com.rootydev.ProductAnalytics.dtos.PagedResult;
import com.rootydev.ProductAnalytics.dtos.SearchTermDto;
import com.rootydev.ProductAnalytics.dtos.SearchTermRequest;
import com.rootydev.ProductAnalytics.services.ISearchTermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/search-terms")
@Tag(name = "Search Terms", description = "APIs for managing product search terms")
public class SearchTermController {
    private final ISearchTermService searchTermService;

    @GetMapping
    @Operation(
            summary = "Get all search terms",
            description = "Retrieve a paginated list of all search terms"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved search terms",
                    content = @Content(schema = @Schema(implementation = PagedResult.class))
            )
    })
    public ApiResponse<PagedResult<SearchTermDto>> getSearchTerms(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int pageIndex,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return searchTermService.getAllSearchTerms(pageIndex, pageSize);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get search term by ID",
            description = "Retrieve a specific search term by its ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Search term found",
                    content = @Content(schema = @Schema(implementation = SearchTermDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Search term not found"
            )
    })
    public ApiResponse<SearchTermDto> getSearchTerm(
            @Parameter(description = "ID of the search term", example = "1", required = true)
            @PathVariable Long id
    ) {
        return searchTermService.getSearchTermById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new search term",
            description = "Add a new search term to the database"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Search term created successfully",
                    content = @Content(schema = @Schema(implementation = SearchTermDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or duplicate search term"
            )
    })
    public ApiResponse<SearchTermDto> createSearchTerm(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Search term to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SearchTermRequest.class))
            )
            @RequestBody SearchTermRequest request
    ) {
        return searchTermService.createSearchTerm(request);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a search term",
            description = "Update an existing search term by ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Search term updated successfully",
                    content = @Content(schema = @Schema(implementation = SearchTermDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Search term not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input"
            )
    })
    public ApiResponse<SearchTermDto> updateSearchTerm(
            @Parameter(description = "ID of the search term to update", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated search term data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SearchTermRequest.class))
            )
            @RequestBody SearchTermRequest request
    ) {
        return searchTermService.updateSearchTerm(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a search term",
            description = "Remove a search term from the database by ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Search term deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Search term not found"
            )
    })
    public ApiResponse<Void> deleteSearchTerm(
            @Parameter(description = "ID of the search term to delete", example = "1", required = true)
            @PathVariable Long id
    ) {
        return searchTermService.deleteSearchTermById(id);
    }
}
