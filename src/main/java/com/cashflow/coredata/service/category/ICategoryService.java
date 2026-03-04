package com.cashflow.coredata.service.category;

import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.commons.core.dto.request.PageRequest;
import com.cashflow.commons.core.dto.response.PageResponse;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.request.category.CategoryEditionRequest;
import com.cashflow.coredata.domain.dto.response.category.CategoryResponse;
import com.cashflow.coredata.domain.dto.response.category.CategorySummaryResponse;
import com.cashflow.exception.core.CashFlowException;

public interface ICategoryService {

    CategorySummaryResponse registerCategory(BaseRequest<CategoryCreationRequest> baseRequest) throws CashFlowException;
    PageResponse<CategorySummaryResponse> listCategories(PageRequest<Void> request);
    CategoryResponse getCategoryById(BaseRequest<Long> baseRequest) throws CashFlowException;
    CategoryResponse editCategoryById(BaseRequest<CategoryEditionRequest> baseRequest) throws CashFlowException;
    void deleteCategoryById(BaseRequest<Long> baseRequest) throws CashFlowException;

}
