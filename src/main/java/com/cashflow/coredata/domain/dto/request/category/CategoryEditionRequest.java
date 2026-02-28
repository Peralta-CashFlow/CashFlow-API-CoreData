package com.cashflow.coredata.domain.dto.request.category;

import com.cashflow.coredata.domain.dto.request.tag.TagRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Category edition request")
public record CategoryEditionRequest(

        @NotNull(message = "{category.id.invalid}")
        long id,

        @NotNull(message = "{category.name.invalid}")
        @NotEmpty(message = "{category.name.invalid}")
        @Size(max = 20, message = "{category.name.tooLong}")
        @Schema(description = "Category name", example = "Groceries")
        String name,

        @Size(max = 20, message = "{category.color.tooLong}")
        @Schema(description = "Category color in HEX format", example = "#FF5733")
        String color,

        @Size(max = 20, message = "{category.icon.tooLong}")
        @Schema(description = "Category icon", example = ":)")
        String icon,

        @NotNull(message = "{category.tags.not.null}")
        @Schema(description = "List of tags associated with the category")
        List<TagRequest> tags
) {
}
