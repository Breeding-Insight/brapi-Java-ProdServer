package org.brapi.test.BrAPITestServer.service.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.swagger.model.Metadata;
import io.swagger.model.core.*;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.handlers.ListItemSearchRequestHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        // Initializes all ListItemSearchRequestHandlers by enumerating through the available BrAPI ListTypes.
        // Right now, GermplasmListSearchRequestHandler is the only implementation available.
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(
                        ListItemSearchRequestHandler::getListType,
                        Function.identity()
                ));
    }

    public List<ListItemWithFullItem<?>> search(ListDetails list,
                                                JsonNode searchRequest,
                                                Metadata metadata) throws BrAPIServerException {
        ListItemSearchRequestHandler<?> handler = handlers.get(list.getListType());

        if (handler == null) {
            // List Type does not have a handler yet.  Implement ListItemSearchRequestHandler for the BrAPI ListType, using GermplasmListSearchRequestHandler as an example.
            throw new BrAPIServerException(HttpStatus.BAD_REQUEST, "Unsupported list type: " + list.getListType());
        }

        return executeHandler(handler, list, searchRequest, metadata);
    }

    private <T> List<ListItemWithFullItem<?>> executeHandler(ListItemSearchRequestHandler<T> handler,
                                                             ListDetails list,
                                                             JsonNode request,
                                                             Metadata metadata) throws BrAPIServerException {

        T typedSearchRequest;

        validateSearchRequest(list, request, handler);

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

    /**
     * Ensures submitted request contains fields/properties only relevant to the BrAPI search request for the list type.
     */
    private <T> void validateSearchRequest(ListDetails list, JsonNode request, ListItemSearchRequestHandler<T> handler) throws BrAPIServerException {

        ObjectReader reader = objectMapper
                .readerFor(handler.getSearchRequestClass())
                .with(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        try (JsonParser parser = request.traverse(objectMapper)) {
            reader.readValue(parser);
        } catch (UnrecognizedPropertyException e) {
                throw new BrAPIServerException(HttpStatus.BAD_REQUEST,
                        String.format("List with dbId [%s] is of type [%s].  The expected search request to be submitted to this endpoint is a " +
                                        "[%s], but a search request property [%s] was found that does not exist in the allowed search request for this list type. " +
                                        "Please ensure any fields being searched match the schema of a [%s] exactly before submitting to ensure correct data is retrieved.",
                                list.getListDbId(),
                                list.getListType(),
                                handler.getSearchRequestClass().getSuperclass().getSimpleName(),
                                e.getPropertyName(),
                                handler.getSearchRequestClass().getSuperclass().getSimpleName())
                );
        } catch (IOException e) {
            throw new BrAPIServerException(HttpStatus.BAD_REQUEST,
                    String.format("List with dbId [%s] is of type [%s].  The expected search request to be submitted to this endpoint is a " +
                                    "[%s], but a part of the request is malformed so that it is incompatible with the targeted request type." +
                                    "Please ensure any fields being searched match the schema of a [%s] exactly before submitting to ensure correct data is retrieved.",
                            list.getListDbId(),
                            list.getListType(),
                            handler.getSearchRequestClass().getSuperclass().getSimpleName(),
                            handler.getSearchRequestClass().getSuperclass().getSimpleName())
            );
        }
    }
}
