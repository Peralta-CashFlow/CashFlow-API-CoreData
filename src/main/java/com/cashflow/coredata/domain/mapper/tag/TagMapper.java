package com.cashflow.coredata.domain.mapper.tag;

import com.cashflow.coredata.domain.dto.response.tag.TagResponse;

import java.util.List;

public class TagMapper {

    private TagMapper() {}

    public static List<TagResponse> toTagResponseList(List<com.cashflow.coredata.domain.entities.Tag> tags) {
        return tags.stream()
                .map(TagMapper::toTagResponse)
                .toList();
    }

    private static TagResponse toTagResponse(com.cashflow.coredata.domain.entities.Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName()
        );
    }

}
