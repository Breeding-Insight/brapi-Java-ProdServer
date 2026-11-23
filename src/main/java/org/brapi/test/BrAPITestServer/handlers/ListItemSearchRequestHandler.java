package org.brapi.test.BrAPITestServer.handlers;

import io.swagger.model.Metadata;
import io.swagger.model.core.ListDetails;
import io.swagger.model.core.ListItemWithFullItem;
import io.swagger.model.core.ListTypes;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;

import java.util.List;

// TODO: Implement for every available list type.  For now, Germplasm is the only implementation required by DeltaBreed.
/**
 * This is a generic interface for any list item search for any list type to implement.
 *
 * This interface is designed to have implementors pass a EntityListSearchRequest, like GermplasmListSearchRequest, as the parameterized object.
 * The implementations must then provide the intended ListTypes enum value for this request, the EntityListSearchRequest class object,
 * and a dedicated search method to carry out the search.
 *
 * See ListsApiController.searchListByItemRequest(), ListItemService.search() for usages, and GermplasmListSearchRequestHandler for an implementation.
 */
public interface ListItemSearchRequestHandler<T> {
    ListTypes getListType();

    Class<T> getSearchRequestClass();

    List<ListItemWithFullItem<?>> search(
            ListDetails list,
            T searchRequest,
            Metadata metadata
    ) throws BrAPIServerException;
}
