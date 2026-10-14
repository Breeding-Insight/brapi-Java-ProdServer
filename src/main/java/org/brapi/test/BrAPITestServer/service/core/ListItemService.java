package org.brapi.test.BrAPITestServer.service.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.Metadata;
import io.swagger.model.core.*;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.handlers.ListItemSearchRequestHandler;
import org.brapi.test.BrAPITestServer.model.entity.core.ListItemEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
                                                JsonNode searchRequest,
                                                Metadata metadata) throws BrAPIServerException {
        ListDetails list = listService.getList(listDbId);

        ListItemSearchRequestHandler<?> handler = handlers.get(list.getListType());

        if (handler == null) {
            throw new BrAPIServerException(HttpStatus.BAD_REQUEST, "Unsupported list type: " + list.getListType());
        }

        return executeHandler(handler, list, searchRequest, metadata);
    }

    private <T> List<ListItemWithFullItem<?>> executeHandler(ListItemSearchRequestHandler<T> handler,
                                                             ListDetails list,
                                                             JsonNode request,
                                                             Metadata metadata) throws BrAPIServerException {

        T typedSearchRequest;

        try {
            typedSearchRequest = objectMapper.convertValue(
                    request,
                    handler.getSearchRequestClass()
            );
        } catch (IllegalArgumentException e) {
            // This allows us to emulate the 400 request failures that normally propagate to the BrapiExceptionHandler.handleHttpMessageNotReadable()
            // This is needed because of the generic nature of the EntityListSearchRequest objects, which come in as untyped JsonNode objects.
            // To support EntityListSearchRequest objects of any type, the jackson serialization has to be done after where it normally occurs at the filter level.
            // Throwing this type of exception obfuscates this fact, and if there are serialization/parsing issues we should see the same error messages we would normally see
            // if done at the filter level.
            throw new HttpMessageNotReadableException("Could not convert request object to JSON object.", e);
        }

        return handler.search(list, typedSearchRequest, metadata);
    }
}
