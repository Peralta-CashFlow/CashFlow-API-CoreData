package templates.category;

import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.category.CategorySummaryResponse;
import com.cashflow.coredata.domain.entities.Category;

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
        return new Category(
                1L,
                "name",
                "color",
                "icon",
                true,
                1L
        );
    }
}
