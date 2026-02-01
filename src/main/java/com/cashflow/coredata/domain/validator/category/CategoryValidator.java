package com.cashflow.coredata.domain.validator.category;

import com.cashflow.exception.core.CashFlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

public class CategoryValidator {

    private static final Logger log = LoggerFactory.getLogger(CategoryValidator.class);

    private CategoryValidator() {
    }

    public static void validateCategoryCreation(
            boolean alreadyExistsByName, MessageSource messageSource, Locale locale
    ) throws CashFlowException {
        if (alreadyExistsByName) {
            log.error("Category with the given name already exists.");
            throw new CashFlowException(
                    HttpStatus.BAD_REQUEST.value(),
                    messageSource.getMessage("category.already.exists.title", null, locale),
                    messageSource.getMessage("category.already.exists.message", null, locale),
                    CategoryValidator.class.getName(),
                    "validateCategoryCreation"
            );
        }
    }
}
