package templates.category;

import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
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

    public static CategoryResponse categoryResponse() {
        return new CategoryResponse(
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
