package templates.tag;

import com.cashflow.coredata.domain.dto.request.tag.TagRequest;
import com.cashflow.coredata.domain.dto.response.tag.TagResponse;
import com.cashflow.coredata.domain.entities.Category;
import com.cashflow.coredata.domain.entities.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagTemplates {

    private TagTemplates() {}

    public static List<TagResponse> tagResponseList() {
        List<TagResponse> responses = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            responses.add(tagResponse(i));
        }
        return responses;
    }

    private static TagResponse tagResponse(long id) {
        return new TagResponse(
                id,
                "Tag " + id
        );
    }

    public static List<Tag> tagList(Category category) {
        List<Tag> tags = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            tags.add(tag(i, category));
        }
        return tags;
    }

    private static Tag tag(long id, Category category) {
        Tag tag = new Tag(
                id,
                "Tag " + id,
                category
        );
        tag.createAudit(category.getUserId());
        return tag;
    }

    public static List<TagRequest> tagRequestList() {
        List<TagRequest> requests = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            requests.add(tagRequest(i));
        }
        return requests;
    }

    private static TagRequest tagRequest(long id) {
        return new TagRequest(
                id,
                "Tag " + id
        );
    }
}
