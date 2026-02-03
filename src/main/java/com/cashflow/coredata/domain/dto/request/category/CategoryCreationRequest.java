package com.cashflow.coredata.domain.dto.request.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Category creation request")
public record CategoryCreationRequest(

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
        String icon
) {
}
