package org.brapi.test.BrAPITestServer.handlers;

import io.swagger.model.Metadata;
import io.swagger.model.core.ListDetails;
import io.swagger.model.core.ListItemWithFullItem;
import io.swagger.model.core.ListTypes;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;

import java.util.List;

public interface ListItemSearchRequestHandler<T> {
    ListTypes getListType();

    Class<T> getSearchRequestClass();

    List<ListItemWithFullItem<?>> search(
            ListDetails list,
            T searchRequest,
            Metadata metadata
    ) throws BrAPIServerException;
}
