package com.cashflow.coredata.controller.category;

import com.cashflow.auth.core.domain.authentication.CashFlowAuthentication;
import com.cashflow.commons.core.dto.response.PageResponse;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.request.category.CategoryEditionRequest;
import com.cashflow.coredata.domain.dto.request.tag.TagRequest;
import com.cashflow.coredata.domain.dto.response.category.CategoryResponse;
import com.cashflow.coredata.domain.dto.response.category.CategorySummaryResponse;
import com.cashflow.coredata.service.category.ICategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import templates.category.CategoryTemplates;
import templates.security.AuthenticationTemplates;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final MessageSource messageSource;

    @MockitoBean
    private ICategoryService categoryService;

    private static final String BASE_REQUEST_URL = "/core/category";
    private final CategorySummaryResponse categorySummaryResponse = CategoryTemplates.categorySummaryResponse();
    private final CategoryResponse categoryResponse = CategoryTemplates.categoryResponse();
    private final CashFlowAuthentication authentication = AuthenticationTemplates.cashFlowAuthentication();

    @Autowired
    CategoryControllerTest(final MockMvc mockMvc,
                           final MessageSource messageSource) {
        this.mockMvc = mockMvc;
        this.messageSource = messageSource;
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

        when(categoryService.registerCategory(any())).thenReturn(categorySummaryResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(categorySummaryResponse)));
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource(value = {
            ", color, icon, category.name.invalid",
            "'', color, icon, category.name.invalid",
            "This name is way too long to be valid, color, icon, category.name.tooLong",
            "name, This color is way too long to be valid, icon, category.color.tooLong",
            "name, color, This icon is way too long to be valid, category.icon.tooLong"
    })
    void givenInvalidCategoryCreationRequests_whenRegisterCategory_thenReturnBadRequest(
            String name, String color, String icon, String errorMessageKey
    ) {

        String jsonRequest = objectMapper.writeValueAsString(new CategoryCreationRequest(name, color, icon));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertTrue(
                mvcResult.getResponse().getContentAsString().contains(
                        messageSource.getMessage(errorMessageKey, new Object[]{}, Locale.ENGLISH)
                )
        );
    }

    @Test
    @SneakyThrows
    void givenParameter_whenListCategories_thenReturnCategoryResponsePage() {

        PageResponse<CategorySummaryResponse> response = new PageResponse<>(new ArrayList<>(List.of(categorySummaryResponse)), 1, 0, 10, 1);

        when(categoryService.listCategories(any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_REQUEST_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    void givenCategoryId_whenGetCategoryById_thenReturnCategoryResponse() {

        when(categoryService.getCategoryById(any())).thenReturn(categoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_REQUEST_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    void givenCategoryEditionRequestWithNullId_whenEditCategoryById_thenReturnBadRequest() {

        String jsonRequest = objectMapper.writeValueAsString(CategoryTemplates.categoryEditionRequest());

        when(categoryService.editCategoryById(any())).thenReturn(categoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource(value = {
            ", name, color, icon, tag, category.id.invalid",
            "1, , color, icon, tag, category.name.invalid",
            "1, '', color, icon, tag, category.name.invalid",
            "1, This name is way too long to be valid, color, icon, tag, category.name.tooLong",
            "1, name, This color is way too long to be valid, icon, tag, category.color.tooLong",
            "1, name, color, This icon is way too long to be valid, tag, category.icon.tooLong",
            "1, name, color, icon, , category.tags.not.null",
            "1, name, color, icon, '', tag.name.invalid",
            "1, name, color, icon, This tag name is way too long to be valid, tag.name.tooLong"
    })
    void givenInvalidCategoryEditionRequests_whenEditCategoryById_thenReturnBadRequest(
            Long id, String name, String color, String icon, String tagName, String errorMessageKey
    ) {
        List<TagRequest> tagRequest = null;
        if (Objects.nonNull(tagName)) {
            tagRequest = List.of(new TagRequest(id, tagName));
        }

        String jsonRequest = objectMapper.writeValueAsString(
                new CategoryEditionRequest(id, name, color, icon, tagRequest)
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(BASE_REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertTrue(
                mvcResult.getResponse().getContentAsString().contains(
                        messageSource.getMessage(errorMessageKey, new Object[]{}, Locale.ENGLISH)
                )
        );
    }

    @Test
    void givenCategoryId_whenDeleteCategoryById_thenReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_REQUEST_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(categoryService, times(1)).deleteCategoryById(any());
    }
}