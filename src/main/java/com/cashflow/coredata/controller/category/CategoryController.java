package com.cashflow.coredata.controller.category;

import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.coredata.service.category.ICategoryService;
import com.cashflow.exception.core.CashFlowException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Validated
@RestController
@RequestMapping("/core/category")
@CrossOrigin("${application.cross.origin}")
@Tag(name = "Category", description = "Category related operations")
public class CategoryController implements ICategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final ICategoryService categoryService;

    public CategoryController(final ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    @PostMapping
    public CategoryResponse registerCategory(CategoryCreationRequest request, Locale language) throws CashFlowException {
        log.info("Received request to register a new category with name: {}", request.name());
        return categoryService.registerCategory(new BaseRequest<>(language, request));
    }

}
