package templates.category;

import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.category.CategoryResponse;
import com.cashflow.coredata.domain.dto.response.category.CategorySummaryResponse;
import com.cashflow.coredata.domain.entities.Category;
import templates.tag.TagTemplates;

import java.time.LocalDateTime;
import java.util.Collections;

public class CategoryTemplates {

    private CategoryTemplates() {}

    public static CategoryCreationRequest categoryCreationRequest() {
        return new CategoryCreationRequest(
                "Groceries",
                "#FF5733",
                ":)"
        );
    }

    public static CategorySummaryResponse categorySummaryResponse() {
        return new CategorySummaryResponse(
                1L,
                "Groceries",
                "#FF5733",
                ":)"
        );
    }

    public static Category category() {
        Category category = new Category(
                1L,
                "name",
                "color",
                "icon",
                true,
                1L,
                Collections.emptyList()
        );
        category.setTags(TagTemplates.tagList(category));
        return category;
    }

    public static CategoryResponse categoryResponse() {
        return new CategoryResponse(
                1L,
                "Groceries",
                "#FF5733",
                ":)",
                LocalDateTime.now(),
                null,
                TagTemplates.tagResponseList()
        );
    }
}
