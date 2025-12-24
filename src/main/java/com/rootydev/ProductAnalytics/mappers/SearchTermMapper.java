package com.rootydev.ProductAnalytics.mappers;

import com.rootydev.ProductAnalytics.dtos.SearchTermDto;
import com.rootydev.ProductAnalytics.dtos.SearchTermRequest;
import com.rootydev.ProductAnalytics.models.SearchTerm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SearchTermMapper {
    SearchTermDto toDto(SearchTerm searchTerm);
    SearchTerm toEntity(SearchTermRequest searchTermDto);
    void updateEntityFromDto(SearchTermRequest request, @MappingTarget SearchTerm searchTerm);
}
