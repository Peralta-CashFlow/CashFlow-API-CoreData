package com.cashflow.coredata.domain.dto.response.category;

import com.cashflow.coredata.domain.dto.response.tag.TagResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Category complete response")
public record CategoryResponse(

        @Schema(description = "Category ID", example = "1")
        Long id,

        @Schema(description = "Category name", example = "Groceries")
        String name,

        @Schema(description = "Category color in HEX format", example = "#FF5733")
        String color,

        @Schema(description = "Category icon", example = ":)")
        String icon,

        @Schema(description = "When the category was created", example = "2026-02-25T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "When the category was last updated", example = "2026-02-25T12:00:00")
        LocalDateTime updatedAt,

        @Schema(description = "Tags associated with the category")
        List<TagResponse> tags

) {
}
