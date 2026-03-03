package com.cashflow.coredata.domain.dto.request.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Tag request")
public record TagRequest(

        @Nullable
        @Schema(description = "Tag ID", example = "1")
        Long id,

        @NotNull(message = "{tag.name.invalid}")
        @NotEmpty(message = "{tag.name.invalid}")
        @Size(max = 20, message = "{tag.name.tooLong}")
        @Schema(description = "Tag name", example = "Important")
        String name

) {
}
