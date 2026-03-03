package com.cashflow.coredata.service.tag;

import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.coredata.domain.dto.request.tag.TagRequest;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.coredata.domain.entities.Tag;
import com.cashflow.coredata.domain.validator.tag.TagValidator;
import com.cashflow.exception.core.CashFlowException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import templates.category.CategoryTemplates;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private Locale locale;

    private final String title = "Title";
    private final String message = "Message";
    private final Category category = CategoryTemplates.category();

    @Test
    void givenTagRequestWithMoreThanMaxTags_whenEditTagsFromRequest_thenThrowCashFlowException() {

        List<TagRequest> maxTagRequest = new ArrayList<>();
        for (int i = 0; i < TagValidator.MAX_TAGS_PER_CATEGORY + 1; i++) {
            maxTagRequest.add(new TagRequest((long) i, "Tag " + i));
        }
        BaseRequest<List<TagRequest>> baseRequest = new BaseRequest<>(locale, maxTagRequest, 1L);

        when(messageSource.getMessage("too.many.tags.title", null, locale)).thenReturn(title);
        when(messageSource.getMessage("too.many.tags.message", new Object[]{TagValidator.MAX_TAGS_PER_CATEGORY}, locale)).thenReturn(message);

        CashFlowException exception = assertThrows(
                CashFlowException.class,
                () -> tagService.editTagsFromRequest(category, baseRequest)
        );

        assertAll(() -> {
            assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatusCode());
            assertEquals(title, exception.getTitle());
            assertEquals(message, exception.getMessage());
        });
    }

    @Test
    void givenTagRequestWithDuplicatedNames_whenEditTagsFromRequest_thenThrowCashFlowException() {

        List<TagRequest> duplicatedTagRequest = List.of(
                new TagRequest(1L, "Tag 1"),
                new TagRequest(2L, "Tag 1"),
                new TagRequest(null, null),
                new TagRequest(null, "Tag 1 "),
                new TagRequest(3L, null)
        );
        BaseRequest<List<TagRequest>> baseRequest = new BaseRequest<>(locale, duplicatedTagRequest, 1L);

        when(messageSource.getMessage("duplicate.tags.title", null, locale)).thenReturn(title);
        when(messageSource.getMessage("duplicate.tags.message", null, locale)).thenReturn(message);

        CashFlowException exception = assertThrows(
                CashFlowException.class,
                () -> tagService.editTagsFromRequest(category, baseRequest)
        );

        assertAll(() -> {
            assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatusCode());
            assertEquals(title, exception.getTitle());
            assertEquals(message, exception.getMessage());
        });
    }

    @Test
    void givenInvalidTagIdsInRequest_whenEditTagsFromRequest_thenThrowCashFlowException() {

        List<TagRequest> invalidIdTagRequest = List.of(
                new TagRequest(999L, "Tag 1")
        );
        BaseRequest<List<TagRequest>> baseRequest = new BaseRequest<>(locale, invalidIdTagRequest, 1L);

        when(messageSource.getMessage("tag.edited.not.found.title", null, locale)).thenReturn(title);
        when(messageSource.getMessage("tag.edited.not.found.message", null, locale)).thenReturn(message);

        CashFlowException exception = assertThrows(
                CashFlowException.class,
                () -> tagService.editTagsFromRequest(category, baseRequest)
        );

        assertAll(() -> {
            assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatusCode());
            assertEquals(title, exception.getTitle());
            assertEquals(message, exception.getMessage());
        });
    }

    @Test
    void givenValidTagRequest_whenEditTagsFromRequest_thenTagsShouldBeUpdated() {

        List<TagRequest> validTagRequest = List.of(
                new TagRequest(1L, "Updated Tag 1"),
                new TagRequest(null, "Updated Tag 2")
        );
        BaseRequest<List<TagRequest>> baseRequest = new BaseRequest<>(locale, validTagRequest, 1L);

        assertEquals(3, category.getTags().size());

        assertDoesNotThrow(() -> tagService.editTagsFromRequest(category, baseRequest));

        Tag firstTag = category.getTags().get(0);
        Tag secondTag = category.getTags().get(1);

        assertAll(() -> {
            assertEquals(2, category.getTags().size());
            assertEquals("Updated Tag 1", firstTag.getName());
            assertNotNull(firstTag.getCreatedAt());
            assertNotNull(firstTag.getUpdatedAt());
            assertEquals("Updated Tag 2", secondTag.getName());
            assertNotNull(secondTag.getCreatedAt());
            assertNull(secondTag.getUpdatedAt());
        });
    }

}