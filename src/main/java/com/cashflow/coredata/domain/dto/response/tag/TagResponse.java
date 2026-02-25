package com.cashflow.coredata.domain.dto.response.tag;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tag response")
public record TagResponse(

        @Schema(description = "Tag ID", example = "1")
        Long id,

        @Schema(description = "Tag name", example = "Important")
        String name
) {
}
