package com.cashflow.coredata.domain.mapper.category;

import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.coredata.domain.entities.Category;

public class CategoryMapper {

    private CategoryMapper(){}

    public static Category mapToEntity(CategoryCreationRequest request, Long userId) {
        return new Category(
                null,
                request.name(),
                request.color(),
                request.icon(),
                true,
                userId
        );
    }

    public static CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getIcon()
        );
    }
}
