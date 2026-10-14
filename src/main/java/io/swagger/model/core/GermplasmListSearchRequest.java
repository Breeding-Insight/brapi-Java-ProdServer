package io.swagger.model.core;

import io.swagger.model.germ.GermplasmSearchRequest;
import org.brapi.test.BrAPITestServer.model.dto.SortAndFilterEntityColumnInfo;
import org.brapi.test.BrAPITestServer.model.dto.EntityType;

import java.util.Collections;
import java.util.Map;

public class GermplasmListSearchRequest extends GermplasmSearchRequest {

    // The only real difference in this request is that there's an extra sort/filter field that GermplasmSearchRequest doesn't have.
    // It is specific to lists.
    private static final Map<String, SortAndFilterEntityColumnInfo>
            LIST_ALLOWED_FIELDS;

    static {
        Map<String, SortAndFilterEntityColumnInfo> fields =
                copyAllowedSortAndFilterFields();

        fields.put(
                "listItemPosition",
                new SortAndFilterEntityColumnInfo("*listItem.positionIndexedBy1", EntityType.NUMBER, false, true)
        );

        LIST_ALLOWED_FIELDS = Collections.unmodifiableMap(fields);
    }

    @Override
    public Map<String, SortAndFilterEntityColumnInfo> getEntityColInfoBySubmittedNameMap() {
        return LIST_ALLOWED_FIELDS;
    }


}
