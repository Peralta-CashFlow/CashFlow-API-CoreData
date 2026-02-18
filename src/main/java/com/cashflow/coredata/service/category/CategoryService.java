package com.cashflow.coredata.service.category;

import com.cashflow.cache.service.CacheService;
import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.commons.core.dto.request.PageRequest;
import com.cashflow.commons.core.dto.response.PageResponse;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.coredata.domain.mapper.category.CategoryMapper;
import com.cashflow.coredata.domain.validator.category.CategoryValidator;
import com.cashflow.coredata.repository.category.CategoryRepository;
import com.cashflow.coredata.utils.constants.cache.CacheNames;
import com.cashflow.exception.core.CashFlowException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {

    @Value("${cache.key-prefix}")
    private String cacheKeyPrefix;

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final MessageSource messageSource;

    private final CacheService cacheService;

    public CategoryService(final CategoryRepository categoryRepository,
                           final MessageSource messageSource,
                           final CacheService cacheService) {
        this.categoryRepository = categoryRepository;
        this.messageSource = messageSource;
        this.cacheService = cacheService;
    }

    @Override
    @Transactional
    public CategoryResponse registerCategory(BaseRequest<CategoryCreationRequest> baseRequest, Long userId) throws CashFlowException {

        CategoryCreationRequest request = baseRequest.getRequest();

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

        cacheService.invalidateCacheByPattern(
                cacheKeyPrefix + CacheNames.CATEGORIES + CacheNames.SEPARATOR + userId + "-*"
        );

        return CategoryMapper.mapToResponse(category);

    }

    private boolean categoryExistsByName(String name, Long userId) {
        return categoryRepository.existsByNameIgnoreCase(name, userId) == 1;
    }

    @Override
    @Cacheable(
            value = CacheNames.CATEGORIES,
            key = "#userId + '-' + #request.search + '-' + #request.pageable.pageNumber + '-' + #request.pageable.pageSize"
    )
    public PageResponse<CategoryResponse> listCategories(PageRequest<Void> request, Long userId) {

        String search = request.getSearch();

        log.info("Searching user: {} categories with search: {}", userId, search);

        Page<CategoryResponse> response = categoryRepository.findByNameLikeIgnoreCase(search, userId, request.getPageable());

        log.info("Found {} categories!", response.getTotalElements());

        return new PageResponse<>(
                response.getContent(),
                response.getPageable().getPageNumber(),
                response.getPageable().getPageSize(),
                response.getTotalElements(),
                response.getTotalPages()
        );
    }

}
