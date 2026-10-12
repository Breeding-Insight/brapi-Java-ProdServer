package org.brapi.test.BrAPITestServer.service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.core.*;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.handlers.ListItemSearchRequestHandler;
import org.brapi.test.BrAPITestServer.model.entity.core.ListEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ListItemService {
    private final ListService listService;
    private final Map<ListTypes, ListItemSearchRequestHandler<?>> handlers;
    private final ObjectMapper objectMapper;

    public ListItemService(ListService listService,
                           List<ListItemSearchRequestHandler<?>> handlerList,
                           ObjectMapper objectMapper) {
        this.listService = listService;
        this.objectMapper = objectMapper;
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(
                        ListItemSearchRequestHandler::getListType,
                        Function.identity()
                ));
    }

    public List<ListItemWithFullItem<?>> search(String listDbId,
                                             ListItemSearchRequest searchRequest) throws BrAPIServerException {
        ListDetails list = listService.getList(listDbId);

        ListItemSearchRequestHandler<?> handler = handlers.get(list.getListType());

        if (handler == null) {
            throw new BrAPIServerException(HttpStatus.BAD_REQUEST, "Unsupported list type: " + list.getListType());
        }

        return executeHandler(handler, list, searchRequest);
    }

    private <T> List<ListItemWithFullItem<?>> executeHandler(
            ListItemSearchRequestHandler<T> handler,
            ListDetails list,
            ListItemSearchRequest request) {

        T typedSearchRequest = objectMapper.convertValue(
                request.getSearchRequest(),
                handler.getSearchRequestClass()
        );

        return handler.search(list, typedSearchRequest);
    }
}
