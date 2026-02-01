package com.cashflow.coredata.controller.category;

import com.cashflow.coredata.domain.dto.request.category.CategoryCreationRequest;
import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.exception.core.CashFlowException;
import com.cashflow.exception.core.domain.dto.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Locale;

public interface ICategoryController {

    @Operation(
            summary = "Register a new Category",
            description = "Should register a new category from the provided request data",
            security = @SecurityRequirement(name = "Authorization"),
            parameters = {
                    @Parameter(name = "Accept-Language", description = "Language to be used on response messages", in = ParameterIn.HEADER, example = "en"),
                    @Parameter(name = "Authorization", description = "JWT token", in = ParameterIn.HEADER, required = true, example = "JWT.TOKEN.HERE")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "Conflict - Category already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    CategoryResponse registerCategory(
            @Valid @RequestBody CategoryCreationRequest request,
            @RequestHeader(name = "Accept-Language", required = false, defaultValue = "en") Locale language
    ) throws CashFlowException;
}
