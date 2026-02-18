package com.cashflow.coredata.service.category;

import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.commons.core.dto.request.PageRequest;
import com.cashflow.commons.core.dto.response.PageResponse;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.exception.core.CashFlowException;

public interface ICategoryService {

    CategoryResponse registerCategory(BaseRequest<CategoryCreationRequest> baseRequest, Long userId) throws CashFlowException;
    PageResponse<CategoryResponse> listCategories(PageRequest<Void> request, Long userId);

}
