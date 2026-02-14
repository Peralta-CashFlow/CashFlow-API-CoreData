package com.cashflow.coredata.service.category;

import com.cashflow.auth.core.utils.AuthUtils;
import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.commons.core.dto.request.PageRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
        Long userId = AuthUtils.getUserIdFromSecurityContext();

        CategoryValidator.validateCategoryCreation(
                categoryExistsByName(request.name(), userId),
                messageSource,
                baseRequest.getLanguage()
        );

        Category category = categoryRepository.save(CategoryMapper.mapToEntity(
                request,
                userId
        ));

        log.info("Category created successfully!");

        return CategoryMapper.mapToResponse(category);

    }

    private boolean categoryExistsByName(String name, Long userId) {
        return categoryRepository.existsByNameIgnoreCase(name, userId) == 1;
    }

    @Override
    public Page<CategoryResponse> listCategories(PageRequest<Void> request) {

        Long userId = AuthUtils.getUserIdFromSecurityContext();
        String search = request.getSearch();

        log.info("Searching user: {} categories with search: {}", userId, search);

        Page<CategoryResponse> response = categoryRepository.findByNameLikeIgnoreCase(search, userId, request.getPageable());

        log.info("Found {} categories!", response.getTotalElements());

        return response;
    }

}
