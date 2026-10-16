package org.brapi.test.BrAPITestServer.service.core;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.model.Metadata;
import io.swagger.model.core.*;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.handlers.ListItemSearchRequestHandler;
import org.brapi.test.BrAPITestServer.model.entity.core.ListItemEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public List<ListItemWithFullItem<?>> search(ListDetails list,
                                                JsonNode searchRequest,
                                                Metadata metadata) throws BrAPIServerException {
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

    private <T> void validateSearchRequest(ListDetails list, JsonNode request, ListItemSearchRequestHandler<T> handler) throws BrAPIServerException {
        JavaType javaType = objectMapper.getTypeFactory()
                .constructType(handler.getSearchRequestClass());

        BeanDescription beanDescription = objectMapper.getDeserializationConfig()
                .introspect(javaType);

        Set<String> allowedProperties = beanDescription.findProperties()
                .stream()
                .map(BeanPropertyDefinition::getName)
                .collect(Collectors.toSet());

        Iterator<String> fields = request.fieldNames();

        while (fields.hasNext()) {
            String field = fields.next();

            if (!allowedProperties.contains(field)) {
                throw new BrAPIServerException(HttpStatus.BAD_REQUEST,
                        String.format("List with dbId [%s] is of type [%s].  The expected search request to be submitted to this endpoint is a " +
                                        "[%s], but a search request property [%s] was found that does not exist in the allowed search request for this list type. " +
                                        "Please ensure any fields being searched match the schema of a [%s] exactly before submitting to ensure correct data is retrieved.",
                                list.getListDbId(),
                                list.getListType(),
                                handler.getSearchRequestClass().getSuperclass().getSimpleName(),
                                field,
                                handler.getSearchRequestClass().getSuperclass().getSimpleName())
                );
            }
        }
    }
}
