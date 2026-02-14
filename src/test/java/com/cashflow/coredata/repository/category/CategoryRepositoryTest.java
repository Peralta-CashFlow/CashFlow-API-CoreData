package com.cashflow.coredata.repository.category;

import com.cashflow.commons.core.dto.request.PageRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private final PageRequest<Void> pageRequest = new PageRequest<>(0, 2, Locale.ENGLISH, "");

    @Test
    void givenFoodFilter_whenFindByNameLikeIgnoreCase_thenReturnOneResult() {
        Page<CategoryResponse> response = categoryRepository.findByNameLikeIgnoreCase("food", 5L, pageRequest.getPageable());
        assertAll(() -> {
            assertEquals(1, response.getTotalElements());
            assertEquals("Food", response.getContent().getFirst().name());
        });
    }

    @Test
    void givenUserIdWithoutCategories_whenFindByNameLikeIgnoreCase_thenReturnNoResult() {
        Page<CategoryResponse> response = categoryRepository.findByNameLikeIgnoreCase("", 404L, pageRequest.getPageable());
        assertEquals(0, response.getTotalElements());
    }

    @Test
    void givenEmptyFilterSearch_whenFindByNameLikeIgnoreCase_thenReturnPagedResponse() {
        Page<CategoryResponse> response = categoryRepository.findByNameLikeIgnoreCase("", 5L, pageRequest.getPageable());
        assertAll(() -> {
            assertEquals(3L, response.getTotalElements());
            assertEquals(2, response.getTotalPages());
            assertEquals(2, response.getContent().size());
        });
    }

}