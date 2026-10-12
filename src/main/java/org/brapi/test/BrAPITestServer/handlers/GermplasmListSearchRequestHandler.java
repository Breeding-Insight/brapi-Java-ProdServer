package org.brapi.test.BrAPITestServer.handlers;

import io.swagger.model.core.GermplasmListSearchRequest;
import io.swagger.model.core.ListDetails;
import io.swagger.model.core.ListItemWithFullItem;
import io.swagger.model.core.ListTypes;
import org.brapi.test.BrAPITestServer.service.germ.GermplasmService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GermplasmListSearchRequestHandler implements ListItemSearchRequestHandler<GermplasmListSearchRequest> {
    private final GermplasmService germplasmService;

    public GermplasmListSearchRequestHandler(GermplasmService germplasmService) {
        this.germplasmService = germplasmService;
    }

    @Override
    public ListTypes getListType() {
        return ListTypes.GERMPLASM;
    }

    @Override
    public Class<GermplasmListSearchRequest> getSearchRequestClass() {
        return GermplasmListSearchRequest.class;
    }

    @Override
    public List<ListItemWithFullItem<?>> search(
            ListDetails list,
            GermplasmListSearchRequest searchRequest) {
        return germplasmService.searchGermplasmByList(list, searchRequest);
    }
}
