package com.cashflow.coredata.service.category;

import com.cashflow.auth.core.domain.authentication.CashFlowAuthentication;
import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.coredata.domain.mapper.category.CategoryMapper;
import com.cashflow.coredata.domain.validator.category.CategoryValidator;
import com.cashflow.coredata.repository.category.CategoryRepository;
import com.cashflow.exception.core.CashFlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CategoryService implements ICategoryService {

    Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final MessageSource messageSource;

    public CategoryService(final CategoryRepository categoryRepository,
                           final MessageSource messageSource) {
        this.categoryRepository = categoryRepository;
        this.messageSource = messageSource;
    }

    @Override
    public CategoryResponse registerCategory(BaseRequest<CategoryCreationRequest> baseRequest) throws CashFlowException {

        CategoryCreationRequest request = baseRequest.getRequest();

        CategoryValidator.validateCategoryCreation(
                categoryExistsByName(request.name()),
                messageSource,
                baseRequest.getLanguage()
        );

        Category category = categoryRepository.save(CategoryMapper.mapToEntity(
                request,
                (CashFlowAuthentication) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
        ));

        log.info("Category created successfully!");

        return CategoryMapper.mapToResponse(category);

    }

    private boolean categoryExistsByName(String name) {
        return categoryRepository.existsByNameIgnoreCase(name) == 1;
    }

}
