package com.cashflow.coredata.service.category;

import com.cashflow.auth.core.domain.authentication.CashFlowAuthentication;
import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.coredata.domain.validator.category.CategoryValidator;
import com.cashflow.coredata.repository.category.CategoryRepository;
import com.cashflow.exception.core.CashFlowException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import templates.category.CategoryTemplates;
import templates.security.AuthenticationTemplates;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MessageSource messageSource;

    private Locale locale;

    private final CategoryCreationRequest request = CategoryTemplates.categoryCreationRequest();

    private final BaseRequest<CategoryCreationRequest> baseRequest = new BaseRequest<>(locale, request);

    @Test
    void givenExistentCategory_whenRegisterCategory_thenThrowCashFlowException() {

        when(categoryRepository.existsByNameIgnoreCase(request.name())).thenReturn(1L);
        when(messageSource.getMessage("category.already.exists.title", null, locale)).thenReturn("Title");
        when(messageSource.getMessage("category.already.exists.message", null, locale)).thenReturn("Message");

        CashFlowException exception = assertThrows(CashFlowException.class, () -> categoryService.registerCategory(baseRequest));

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
        CashFlowAuthentication authentication = AuthenticationTemplates.cashFlowAuthentication();

        when(categoryRepository.existsByNameIgnoreCase(request.name())).thenReturn(0L);
        when(categoryRepository.save(any())).thenReturn(category);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        CategoryResponse response = categoryService.registerCategory(baseRequest);

        assertAll(() -> {
            assertEquals(category.getId(), response.id());
            assertEquals(category.getColor(), response.color());
            assertEquals(category.getIcon(), response.icon());
            assertEquals(category.getName(), response.name());
        });
    }

}