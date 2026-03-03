package com.cashflow.coredata.service.category;

import com.cashflow.cache.annotations.clear.CacheClear;
import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.commons.core.dto.request.PageRequest;
import com.cashflow.commons.core.dto.response.PageResponse;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.request.category.CategoryEditionRequest;
import com.cashflow.coredata.domain.dto.response.category.CategoryResponse;
import com.cashflow.coredata.domain.dto.response.category.CategorySummaryResponse;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.coredata.domain.mapper.category.CategoryMapper;
import com.cashflow.coredata.domain.validator.category.CategoryValidator;
import com.cashflow.coredata.repository.category.CategoryRepository;
import com.cashflow.coredata.service.tag.ITagService;
import com.cashflow.coredata.utils.constants.cache.CacheNames;
import com.cashflow.exception.core.CashFlowException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static com.cashflow.coredata.utils.constants.cache.CacheNames.APP_BASE_KEY;

@Service
public class CategoryService implements ICategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final MessageSource messageSource;

    private final ITagService tagService;

    public CategoryService(final CategoryRepository categoryRepository,
                           final MessageSource messageSource,
                           final ITagService tagService) {
        this.categoryRepository = categoryRepository;
        this.messageSource = messageSource;
        this.tagService = tagService;
    }

    @Override
    @Transactional
    @CacheClear(
            value = APP_BASE_KEY + CacheNames.CATEGORIES,
            patterns = { "#baseRequest.userId + '-*'" }
    )
    public CategorySummaryResponse registerCategory(BaseRequest<CategoryCreationRequest> baseRequest) throws CashFlowException {

        CategoryCreationRequest request = baseRequest.getRequest();
        long userId = baseRequest.getUserId();

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

        return CategoryMapper.mapToSummaryResponse(category);

    }

    private boolean categoryExistsByName(String name, long userId) {
        return categoryRepository.existsByNameIgnoreCase(name, userId) == 1;
    }

    @Override
    @Cacheable(
            value = CacheNames.CATEGORIES,
            key = "#request.userId + '-' + #request.search + '-' + #request.pageable.pageNumber + '-' + #request.pageable.pageSize"
    )
    public PageResponse<CategorySummaryResponse> listCategories(PageRequest<Void> request) {

        String search = request.getSearch();
        long userId = request.getUserId();

        log.info("Searching user: {} categories with search: {}", userId, search);

        Page<CategorySummaryResponse> response = categoryRepository.findByNameLikeIgnoreCase(search, userId, request.getPageable());

        log.info("Found {} categories!", response.getTotalElements());

        return new PageResponse<>(
                response.getContent(),
                response.getPageable().getPageNumber(),
                response.getPageable().getPageSize(),
                response.getTotalElements(),
                response.getTotalPages()
        );
    }

    @Override
    public CategoryResponse getCategoryById(BaseRequest<Long> baseRequest) throws CashFlowException {
        return CategoryMapper.mapToResponse(
                getCategoryByIdAndUserId(baseRequest.getRequest(), baseRequest.getUserId(), baseRequest.getLanguage())
        );
    }

    @Override
    @Transactional
    @CacheClear(
            value = APP_BASE_KEY + CacheNames.CATEGORIES,
            patterns = { "#baseRequest.userId + '-*'" }
    )
    public CategoryResponse editCategoryById(BaseRequest<CategoryEditionRequest> baseRequest) throws CashFlowException {

        log.info("Editing category...");

        CategoryEditionRequest categoryEditionRequest = baseRequest.getRequest();
        long userId = baseRequest.getUserId();
        Locale language = baseRequest.getLanguage();

        Category category = getCategoryByIdAndUserId(categoryEditionRequest.id(), userId, language);

        CategoryValidator.validateCategoryCreation(
                categoryExistsByNameAndDifferentId(categoryEditionRequest.name(), userId, categoryEditionRequest.id()),
                messageSource,
                baseRequest.getLanguage()
        );

        CategoryMapper.updateFromRequest(category, categoryEditionRequest, userId);

        tagService.editTagsFromRequest(category, new BaseRequest<>(language, categoryEditionRequest.tags(), userId));

        category = categoryRepository.save(category);

        log.info("Category updated successfully!");

        return CategoryMapper.mapToResponse(category);
    }

    private boolean categoryExistsByNameAndDifferentId(String name, long userId, long categoryId) {
        return categoryRepository.existsByNameIgnoreCaseDifferentCategoryId(name, userId, categoryId) == 1;
    }

    private Category getCategoryByIdAndUserId(Long categoryId, Long userId, Locale language) throws CashFlowException {
        log.info("Fetching category with id: {} from user: {}", categoryId, userId);

        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> {
                            log.error("Category not found.");
                            return new CashFlowException(
                                    HttpStatus.NOT_FOUND.value(),
                                    messageSource.getMessage("category.not.found.title", null, language),
                                    messageSource.getMessage("category.not.found.message", null, language),
                                    CategoryService.class.getName(),
                                    "getCategoryByIdAndUserId"
                            );
                        }
                );

        log.info("Category successfully retrieved.");

        return category;
    }


}
