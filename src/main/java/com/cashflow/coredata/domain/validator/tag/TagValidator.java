package com.cashflow.coredata.domain.validator.tag;

import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.coredata.domain.dto.request.tag.TagRequest;
import com.cashflow.coredata.domain.entities.Tag;
import com.cashflow.exception.core.CashFlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TagValidator {

    private TagValidator() {}

    private static final Logger log = LoggerFactory.getLogger(TagValidator.class);

    private static final int MAX_TAGS_PER_CATEGORY = 10;

    public static void validateTagsEdition(
            List<Tag> savedTags, BaseRequest<List<TagRequest>> baseRequest, MessageSource messageSource
    ) throws CashFlowException {

        Locale locale = baseRequest.getLanguage();
        List<TagRequest> requestTags = baseRequest.getRequest();

        validateTagQuantity(messageSource, requestTags, locale);
        validateDuplicatedTagNames(messageSource, requestTags, locale);
        validateUpdatedIds(savedTags, messageSource, requestTags, locale);

        log.info("Tag edition request passed all validations successfully.");

    }

    private static void validateTagQuantity(
            MessageSource messageSource, List<TagRequest> requestTags, Locale locale
    ) throws CashFlowException {
        if (requestTags.size() > MAX_TAGS_PER_CATEGORY) {
            log.error("Categories cannot have more than {} requestTags.", MAX_TAGS_PER_CATEGORY);
            throw new CashFlowException(
                    HttpStatus.BAD_REQUEST.value(),
                    messageSource.getMessage("too.many.tags.title", null, locale),
                    messageSource.getMessage("too.many.tags.message", new Object[]{MAX_TAGS_PER_CATEGORY}, locale),
                    TagValidator.class.getName(),
                    "validateTagsEdition"
            );
        }
    }

    private static void validateDuplicatedTagNames(
            MessageSource messageSource, List<TagRequest> requestTags, Locale locale
    ) throws CashFlowException {
        Map<String, List<TagRequest>> grouped = requestTags.stream()
                .filter(t -> t != null && t.name() != null)
                .collect(Collectors.groupingBy(t -> t.name().trim().toLowerCase()));

        List<String> duplicatedNames = grouped.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .toList();

        if (!duplicatedNames.isEmpty()) {
            String joined = String.join(", ", duplicatedNames);
            log.error("Duplicate tag names found: {}", joined);
            throw new CashFlowException(
                    HttpStatus.BAD_REQUEST.value(),
                    messageSource.getMessage("duplicate.tags.title", null, locale),
                    messageSource.getMessage("duplicate.tags.message", null, locale),
                    TagValidator.class.getName(),
                    "validateTagsEdition"
            );
        }
    }

    private static void validateUpdatedIds(
            List<Tag> savedTags, MessageSource messageSource, List<TagRequest> requestTags, Locale locale
    ) throws CashFlowException {
        if (!CollectionUtils.isEmpty(savedTags)) {

            List<Long> savedTagsIds = savedTags.stream().map(Tag::getId).toList();
            List<Long> updatedTagsIds = requestTags.stream().map(TagRequest::id).filter(Objects::nonNull).toList();

            List<Long> nonExistingIds = updatedTagsIds.stream()
                    .filter(id -> !savedTagsIds.contains(id))
                    .toList();

            if (!nonExistingIds.isEmpty()) {
                String joined = nonExistingIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
                log.error("Updated Tag IDs from request not found in the category: {}", joined);
                throw new CashFlowException(
                        HttpStatus.BAD_REQUEST.value(),
                        messageSource.getMessage("tag.edited.not.found.title", null, locale),
                        messageSource.getMessage("tag.edited.not.found.message", null, locale),
                        TagValidator.class.getName(),
                        "validateTagsEdition"
                );
            }
        }
    }

}
