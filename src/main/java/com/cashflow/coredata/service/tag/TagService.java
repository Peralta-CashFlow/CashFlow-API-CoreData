package com.cashflow.coredata.service.tag;

import com.cashflow.commons.core.dto.request.BaseRequest;
import com.cashflow.coredata.domain.dto.request.tag.TagRequest;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.coredata.domain.entities.Tag;
import com.cashflow.coredata.domain.validator.tag.TagValidator;
import com.cashflow.exception.core.CashFlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TagService implements ITagService {

    private final Logger log = LoggerFactory.getLogger(TagService.class);

    private final MessageSource messageSource;

    public TagService(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void editTagsFromRequest(Category category, BaseRequest<List<TagRequest>> baseRequest) throws CashFlowException {

        log.info("Updating tags from request...");

        List<Tag> tags = category.getTags();
        TagValidator.validateTagsEdition(tags, baseRequest, messageSource);

        List<TagRequest> tagRequests = baseRequest.getRequest();

        removeTags(tagRequests, tags);
        updateTags(tagRequests, tags);
        addNewTags(category, tagRequests, tags);

        log.info("Tags updated successfully.");

    }

    private void removeTags(List<TagRequest> tagRequests, List<Tag> tags) {
        List<Long> tagIdsToKeep = tagRequests.stream()
                .map(TagRequest::id)
                .filter(Objects::nonNull)
                .toList();

        List<Tag> tagsToRemove = tags.stream()
                .filter(tag -> !tagIdsToKeep.contains(tag.getId()))
                .toList();

        tags.removeAll(tagsToRemove);
    }

    private void updateTags(List<TagRequest> tagRequests, List<Tag> tags) {
        Map<Long, Tag> tagsById = tags.stream().collect(Collectors.toMap(Tag::getId, Function.identity()));
        tagRequests.stream().filter(tagRequest -> tagRequest.id() != null).forEach(tagRequest -> {
            Tag tag = tagsById.get(tagRequest.id());
            tag.setName(tagRequest.name());
        });
    }

    private void addNewTags(Category category, List<TagRequest> tagRequests, List<Tag> tags) {
        List<TagRequest> newTags = tagRequests.stream().filter(tagRequest -> tagRequest.id() == null).toList();
        newTags.forEach(tag -> tags.add(new Tag(tag.name(), category)));
    }
}
