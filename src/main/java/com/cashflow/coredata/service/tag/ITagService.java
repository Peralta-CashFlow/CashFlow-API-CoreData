package com.cashflow.coredata.service.tag;

import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.coredata.domain.dto.request.tag.TagRequest;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.exception.core.CashFlowException;

import java.util.List;

public interface ITagService {

    void editTagsFromRequest(Category category, BaseRequest<List<TagRequest>> baseRequest) throws CashFlowException;

}
