package com.cashflow.coredata.controller.category;

import com.cashflow.auth.core.domain.authentication.CashFlowAuthentication;
import com.cashflow.commons.core.dto.response.PageResponse;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.coredata.service.category.ICategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import templates.category.CategoryTemplates;
import templates.security.AuthenticationTemplates;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockitoBean
    private ICategoryService categoryService;

    private static final String BASE_REQUEST_URL = "/core/category";
    private final CategoryResponse categoryResponse = CategoryTemplates.categoryResponse();
    private final CashFlowAuthentication authentication = AuthenticationTemplates.cashFlowAuthentication();

    @Autowired
    CategoryControllerTest(final MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setup() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @SneakyThrows
    void givenCategoryCreationRequest_whenRegisterCategory_thenCategoryResponseIsReturned() {

        String jsonRequest = objectMapper.writeValueAsString(CategoryTemplates.categoryCreationRequest());

        when(categoryService.registerCategory(any(), any())).thenReturn(categoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(categoryResponse)));
    }

    @Test
    @SneakyThrows
    void givenParameter_whenListCategories_thenReturnCategoryResponsePage() {

        PageResponse<CategoryResponse> response = new PageResponse<>(new ArrayList<>(List.of(categoryResponse)), 1, 0, 10, 1);

        when(categoryService.listCategories(any(), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_REQUEST_URL + "/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}