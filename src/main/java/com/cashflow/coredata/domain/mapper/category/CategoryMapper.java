package com.cashflow.coredata.domain.mapper.category;

import com.cashflow.auth.core.domain.authentication.CashFlowAuthentication;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.coredata.domain.entities.Category;

import java.util.Objects;

public class CategoryMapper {

    private CategoryMapper(){}

    public static Category mapToEntity(CategoryCreationRequest request, CashFlowAuthentication authentication) {
        return new Category(
                null,
                request.name(),
                request.color(),
                request.icon(),
                true,
                Objects.requireNonNull(authentication.getCredentials()).id(),
                request.type()
        );
    }

    public static CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getIcon(),
                category.getType().getDescription()
        );
    }
}
