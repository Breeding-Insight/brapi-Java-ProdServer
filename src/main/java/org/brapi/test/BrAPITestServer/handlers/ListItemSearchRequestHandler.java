package org.brapi.test.BrAPITestServer.handlers;

import io.swagger.model.core.ListDetails;
import io.swagger.model.core.ListItemWithFullItem;
import io.swagger.model.core.ListItemsListResponseResult;
import io.swagger.model.core.ListTypes;
import org.brapi.test.BrAPITestServer.model.entity.core.ListEntity;

import java.util.List;

public interface ListItemSearchRequestHandler<T> {
    ListTypes getListType();

    Class<T> getSearchRequestClass();

    List<ListItemWithFullItem<?>> search(
            ListDetails list,
            T searchRequest
    );
}
