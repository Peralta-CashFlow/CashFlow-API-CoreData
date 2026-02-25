package com.cashflow.coredata.domain.mapper.category;

import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.category.CategoryResponse;
import com.cashflow.coredata.domain.dto.response.category.CategorySummaryResponse;
import com.cashflow.coredata.domain.dto.response.tag.TagResponse;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.coredata.domain.mapper.tag.TagMapper;

import java.util.Comparator;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category mapToEntity(CategoryCreationRequest request, Long userId) {
        return new Category(
                request.name(),
                request.color(),
                request.icon(),
                true,
                userId
        );
    }

    public static CategorySummaryResponse mapToSummaryResponse(Category category) {
        return new CategorySummaryResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getIcon()
        );
    }

    public static CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getIcon(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                TagMapper.toTagResponseList(category.getTags())
                        .stream()
                        .sorted(Comparator.comparing(TagResponse::name))
                        .toList()
        );
    }
}
