package io.swagger.model.core;

import io.swagger.model.germ.GermplasmSearchRequest;
import org.brapi.test.BrAPITestServer.model.dto.EntityColumnNameAndType;
import org.brapi.test.BrAPITestServer.model.dto.EntityType;

import java.util.Collections;
import java.util.Map;

public class GermplasmListSearchRequest extends GermplasmSearchRequest {

    // The only real difference in this request is that there's an extra sort/filter field that GermplasmSearchRequest doesn't have.
    // It is specific to lists.
    private static final Map<String, EntityColumnNameAndType>
            LIST_ALLOWED_FIELDS;

    static {
        Map<String, EntityColumnNameAndType> fields =
                copyAllowedSortAndFilterFields();

        fields.put(
                "listItemPosition",
                new EntityColumnNameAndType("listItem.position", EntityType.NUMBER)
        );

        LIST_ALLOWED_FIELDS = Collections.unmodifiableMap(fields);
    }

    @Override
    public Map<String, EntityColumnNameAndType> getEntityColAndTypeBySubmittedNameMap() {
        return LIST_ALLOWED_FIELDS;
    }


}
