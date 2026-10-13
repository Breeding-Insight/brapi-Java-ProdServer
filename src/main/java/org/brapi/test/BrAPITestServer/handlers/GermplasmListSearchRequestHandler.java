package org.brapi.test.BrAPITestServer.handlers;

import io.swagger.model.Metadata;
import io.swagger.model.core.*;
import io.swagger.model.germ.Germplasm;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.model.entity.core.ListItemEntity;
import org.brapi.test.BrAPITestServer.model.entity.germ.GermplasmEntity;
import org.brapi.test.BrAPITestServer.service.PagingUtility;
import org.brapi.test.BrAPITestServer.service.germ.GermplasmService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
            GermplasmListSearchRequest searchRequest,
            Metadata metadata) throws BrAPIServerException {

        List<Object[]> results = germplasmService.getGermplasmInList();

        List<ListItemWithFullItem<?>> listItems = new ArrayList<>();

        for (Object[] result : results) {
            ListItemWithFullItem<Germplasm>  item = new ListItemWithFullItem<>();

            ListItemEntity listItemEntity = (ListItemEntity) result[0];
            GermplasmEntity germplasmEntity = (GermplasmEntity) result[1];

            ListItem listItem = new ListItem();
            listItem.setListItemDbId(listItemEntity.getId().toString());
            listItem.setItem(listItemEntity.getItem());
            listItem.setPosition(listItemEntity.getPosition());

            item.setlistItem(listItem);

            Germplasm germplasm = germplasmService.convertFromEntity(germplasmEntity);

            item.setItem(germplasm);

            listItems.add(item);
        }

        return listItems;
    }
}
