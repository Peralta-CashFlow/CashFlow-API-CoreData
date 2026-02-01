package com.cashflow.coredata.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Category response")
public record CategoryResponse(

        @Schema(description = "Category ID", example = "1")
        Long id,

        @Schema(description = "Category name", example = "Groceries")
        String name,

        @Schema(description = "Category color in HEX format", example = "#FF5733")
        String color,

        @Schema(description = "Category icon", example = ":)")
        String icon,

        @Schema(description = "Category type", example = "Expense")
        String type
) {
}
