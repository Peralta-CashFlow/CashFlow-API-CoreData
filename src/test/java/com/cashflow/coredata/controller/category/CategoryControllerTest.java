package com.cashflow.coredata.controller.category;

import com.cashflow.coredata.service.category.ICategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import templates.category.CategoryTemplates;

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

    @Autowired
    CategoryControllerTest(final MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    void givenCategoryCreationRequest_whenRegisterCategory_thenCategoryResponseIsReturned() {

        String jsonRequest = objectMapper.writeValueAsString(CategoryTemplates.categoryCreationRequest());

        var response = CategoryTemplates.categoryResponse();
        when(categoryService.registerCategory(any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }
}