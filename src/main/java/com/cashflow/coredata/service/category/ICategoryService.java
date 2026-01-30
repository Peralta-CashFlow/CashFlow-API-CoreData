package com.cashflow.coredata.service.category;

import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.exception.core.CashFlowException;

public interface ICategoryService {

    CategoryResponse registerCategory(BaseRequest<CategoryCreationRequest> baseRequest) throws CashFlowException;
}
