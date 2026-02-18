package com.cashflow.coredata.service.category;

import com.cashflow.auth.core.domain.authentication.CashFlowAuthentication;
import com.cashflow.cache.service.CacheService;
import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.commons.core.dto.request.PageRequest;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.coredata.domain.validator.category.CategoryValidator;
import com.cashflow.coredata.repository.category.CategoryRepository;
import com.cashflow.exception.core.CashFlowException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import templates.category.CategoryTemplates;
import templates.security.AuthenticationTemplates;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private CacheService cacheService;

    private Locale locale;

    private final CategoryCreationRequest request = CategoryTemplates.categoryCreationRequest();

    private final BaseRequest<CategoryCreationRequest> baseRequest = new BaseRequest<>(locale, request);

    private final CashFlowAuthentication authentication = AuthenticationTemplates.cashFlowAuthentication();

    private final CategoryResponse categoryResponse = CategoryTemplates.categoryResponse();

    @BeforeEach
    void setup() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void givenExistentCategory_whenRegisterCategory_thenThrowCashFlowException() {

        when(categoryRepository.existsByNameIgnoreCase(
                request.name(),
                Objects.requireNonNull(authentication.getCredentials()).id())
        ).thenReturn(1L);
        when(messageSource.getMessage("category.already.exists.title", null, locale)).thenReturn("Title");
        when(messageSource.getMessage("category.already.exists.message", null, locale)).thenReturn("Message");

        CashFlowException exception = assertThrows(CashFlowException.class, () -> categoryService.registerCategory(
                baseRequest, Objects.requireNonNull(authentication.getCredentials()).id())
        );

        assertAll(() -> {
            assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatusCode());
            assertEquals("Title", exception.getTitle());
            assertEquals("Message", exception.getMessage());
            assertEquals(CategoryValidator.class.getName(), exception.getClassName());
            assertEquals("validateCategoryCreation", exception.getMethodName());
        });
    }

    @Test
    @SneakyThrows
    void givenNewCategory_whenRegisterCategory_thenReturnCategoryResponse() {

        Category category = CategoryTemplates.category();

        when(categoryRepository.existsByNameIgnoreCase(
                request.name(),
                Objects.requireNonNull(authentication.getCredentials()).id())
        ).thenReturn(0L);
        when(categoryRepository.save(any())).thenReturn(category);

        CategoryResponse response = categoryService.registerCategory(
                baseRequest, Objects.requireNonNull(authentication.getCredentials()).id()
        );

        assertAll(() -> {
            assertEquals(category.getId(), response.id());
            assertEquals(category.getColor(), response.color());
            assertEquals(category.getIcon(), response.icon());
            assertEquals(category.getName(), response.name());
            verify(cacheService, times(1)).invalidateCacheByPattern(anyString());
        });
    }

    @Test
    void givenPageRequest_whenListCategories_thenReturnCategoryResponsePage() {

        PageRequest<Void> pageRequest = new PageRequest<>(0, 10, locale, "search");
        Page<CategoryResponse> pageResponse = new PageImpl<>(List.of(categoryResponse), Pageable.ofSize(20), 1);

        when(categoryRepository.findByNameLikeIgnoreCase(
                "search",
                Objects.requireNonNull(authentication.getCredentials()).id(),
                pageRequest.getPageable()
        )).thenReturn(pageResponse);

        assertEquals(
                categoryResponse,
                categoryService.listCategories(pageRequest, Objects.requireNonNull(authentication.getCredentials()).id()).response().getFirst()
        );

    }

}