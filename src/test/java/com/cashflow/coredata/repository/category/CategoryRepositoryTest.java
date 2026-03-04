package com.cashflow.coredata.repository.category;

import com.cashflow.cache.service.CacheService;
import com.cashflow.commons.core.dto.request.PageRequest;
import com.cashflow.coredata.domain.dto.response.category.CategorySummaryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoBean
    private CacheService cacheService;

    private final PageRequest<Void> pageRequest = new PageRequest<>(0, 2, Locale.ENGLISH, "", 1L);

    @Test
    void givenFoodFilter_whenFindByNameLikeIgnoreCase_thenReturnOneResult() {
        Page<CategorySummaryResponse> response = categoryRepository.findByNameLikeIgnoreCase("food", 5L, pageRequest.getPageable());
        assertAll(() -> {
            assertEquals(1, response.getTotalElements());
            assertEquals("Food", response.getContent().getFirst().name());
        });
    }

    @Test
    void givenUserIdWithoutCategories_whenFindByNameLikeIgnoreCase_thenReturnNoResult() {
        Page<CategorySummaryResponse> response = categoryRepository.findByNameLikeIgnoreCase("", 404L, pageRequest.getPageable());
        assertEquals(0, response.getTotalElements());
    }

    @Test
    void givenUserIdWithCategoryButNameInactive_whenFindByNameLikeIgnoreCase_thenReturnNoResult() {
        Page<CategorySummaryResponse> response = categoryRepository.findByNameLikeIgnoreCase("Entertainment", 6L, pageRequest.getPageable());
        assertEquals(0, response.getTotalElements());
    }

    @Test
    void givenEmptyFilterSearch_whenFindByNameLikeIgnoreCase_thenReturnPagedResponse() {
        Page<CategorySummaryResponse> response = categoryRepository.findByNameLikeIgnoreCase("", 5L, pageRequest.getPageable());
        assertAll(() -> {
            assertEquals(3L, response.getTotalElements());
            assertEquals(2, response.getTotalPages());
            assertEquals(2, response.getContent().size());
        });
    }

    @Test
    void givenValidCategoryIdAndUserId_whenFindByIdAndUserId_AndActiveTrue_thenReturnCategory() {
        var response = categoryRepository.findByIdAndUserIdAndActiveTrue(1L, 5L);
        assertAll(() -> {
            assertTrue(response.isPresent());
            assertEquals(1L, response.get().getId());
            assertEquals(5L, response.get().getUserId());
        });
    }

    @Test
    void givenInvalidCategoryIdAndUserId_whenFindByIdAndUserId_AndActiveTrue_thenReturnEmpty() {
        var response = categoryRepository.findByIdAndUserIdAndActiveTrue(404L, 5L);
        assertTrue(response.isEmpty());
    }

    @Test
    void givenValidCategoryIdAndInvalidUserId_whenFindByIdAndUserId_AndActiveTrue_thenReturnEmpty() {
        var response = categoryRepository.findByIdAndUserIdAndActiveTrue(1L, 404L);
        assertTrue(response.isEmpty());
    }

    @Test
    void givenValidCategoryIdAndUserIdButInactive_whenFindByIdAndUserId_AndActiveTrue_thenReturnEmpty() {
        var response = categoryRepository.findByIdAndUserIdAndActiveTrue(500L, 6L);
        assertTrue(response.isEmpty());
    }

}