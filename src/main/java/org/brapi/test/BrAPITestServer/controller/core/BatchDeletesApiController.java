package org.brapi.test.BrAPITestServer.controller.core;

import io.swagger.api.core.BatchDeletesApi;
import io.swagger.model.BrAPIResponse;
import io.swagger.model.Metadata;
import io.swagger.model.SearchRequest;
import io.swagger.model.core.*;

import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.factory.BrAPIComponent;
import org.brapi.test.BrAPITestServer.factory.BrAPIComponentFactory;
import org.brapi.test.BrAPITestServer.model.entity.SearchRequestEntity;
import org.brapi.test.BrAPITestServer.service.SearchService;
import org.brapi.test.BrAPITestServer.service.core.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-20T16:31:52.030Z[GMT]")
@Controller
public class BatchDeletesApiController extends BrAPIController implements BatchDeletesApi {

	private static final Logger log = LoggerFactory.getLogger(BatchDeletesApiController.class);

	private final HttpServletRequest request;
	private final BatchService batchService;
	private final SearchService searchService;
	private final BrAPIComponentFactory componentFactory;

	@Autowired
	public BatchDeletesApiController(BatchService batchService, SearchService searchService, BrAPIComponentFactory componentFactory, HttpServletRequest request) {
		this.batchService = batchService;
		this.searchService = searchService;
		this.componentFactory = componentFactory;
		this.request = request;
	}

	@CrossOrigin
	@Override
	public ResponseEntity<BatchDeletesSingleResponse> batchDeletesBatchDbIdGet(
			@PathVariable("batchDeleteDbId") String batchDeleteDbId,
			@RequestHeader(value = "Authorization", required = false) String authorization)
			throws BrAPIServerException {

		log.debug("Request: " + request.getRequestURI());
		validateSecurityContext(request, "ROLE_ANONYMOUS", "ROLE_USER");
		validateAcceptHeader(request);
		BatchDeleteDetails data = batchService.getBatch(batchDeleteDbId);
		return responseOK(new BatchDeletesSingleResponse(), data);
	}

	@CrossOrigin
	@Override
	public ResponseEntity<BatchDeleteResponse> batchDeletesBatchDbIdItemsPost(
			@PathVariable("batchDeleteDbId") String batchDeleteDbId,
			@Valid @RequestBody ArrayList<String> body,
			@RequestHeader(value = "Authorization", required = false) String authorization) throws BrAPIServerException {

		return batchDeletesBatchDbIdDataPost(batchDeleteDbId, body, authorization);
	}

	@CrossOrigin
	@Override
	public ResponseEntity<BatchDeleteResponse> batchDeletesBatchDbIdDataPost(
			@PathVariable("batchDeleteDbId") String batchDeleteDbId,
			@Valid @RequestBody ArrayList<String> body,
			@RequestHeader(value = "Authorization", required = false) String authorization) throws BrAPIServerException {

		log.debug("Request: " + request.getRequestURI());
		validateSecurityContext(request, "ROLE_USER");
		validateAcceptHeader(request);
		BatchDeleteDetails data = batchService.updateBatchItems(batchDeleteDbId, body);
		return responseOK(new BatchDeleteResponse(), data);
	}

	@CrossOrigin
	@Override
	public ResponseEntity<BatchDeletesSingleResponse> batchDeletesBatchDbIdPut(
			@PathVariable("batchDeleteDbId") String batchDeleteDbId,
			@Valid @RequestBody BatchDeleteNewRequest body,
			@RequestHeader(value = "Authorization", required = false) String authorization) throws BrAPIServerException {

		log.debug("Request: " + request.getRequestURI());
		validateSecurityContext(request, "ROLE_USER");
		validateAcceptHeader(request);
		BatchDeleteDetails data = batchService.updateBatch(batchDeleteDbId, body);
		return responseOK(new BatchDeletesSingleResponse(), data);
	}

	@CrossOrigin
	@Override
	public ResponseEntity<? extends BrAPIResponse> batchDeletesPost(@Valid @RequestBody BatchDeleteSearchRequest body,
																	@RequestHeader(value = "Authorization", required = false) String authorization) throws BrAPIServerException {

		log.debug("Request: " + request.getRequestURI());
		validateSecurityContext(request, "ROLE_USER");
		validateAcceptHeader(request);
		Metadata metadata = generateMetaDataTemplate(body);
		BatchDeleteTypes batchType = body.getBatchDeleteType();
		BrAPIComponent component = componentFactory.getComponent(batchType);

		// Return the searchDbId with a 202 if the search is too in-depth with several parameters
		String searchReqDbId = searchService.saveSearchRequest(body, SearchRequestEntity.SearchRequestTypes.BATCHES);
		if (searchReqDbId != null) {
			return responseAccepted(searchReqDbId);
		}

		// Fetch requested BrAPI entities
		SearchRequest entitySearch = body.getSearchRequest();
		List<?> entities = component.findEntities(entitySearch, metadata);

		// Create a new batch for the requested entites
		List<String> entityDbIds = component.collectDbIds(entities);
		BatchDeleteNewRequest newBatchRequest = (BatchDeleteNewRequest) new BatchDeleteNewRequest().data(entityDbIds).batchDeleteType(body.getBatchDeleteType());
		String newBatchDbID = batchService.saveNewBatch(Arrays.asList(newBatchRequest)).get(0).getBatchDeleteDbId();

		return responseOK(newBatchDbID, entityDbIds, metadata);
	}

	@CrossOrigin
	@Override
	public ResponseEntity<? extends BrAPIResponse> searchBatchDeletesSearchResultsDbIdGet(
			@PathVariable("searchResultsDbId") String searchResultsDbId,
			@Valid @RequestParam(value = "page", required = false) Integer page,
			@Valid @RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestHeader(value = "Authorization", required = false) String authorization) throws BrAPIServerException {

		log.debug("Request: " + request.getRequestURI());
		validateSecurityContext(request, "ROLE_ANONYMOUS", "ROLE_USER");
		validateAcceptHeader(request);
		Metadata metadata = generateMetaDataTemplate(page, pageSize);
		SearchRequestEntity request = searchService.findById(searchResultsDbId);

		// Return a 202 if the search results are not ready
		if (request == null) {
			responseAccepted(searchResultsDbId);
		}

		BatchDeleteSearchRequest body = request.getParameters(BatchDeleteSearchRequest.class);
		BatchDeleteTypes batchType = body.getBatchDeleteType();
		BrAPIComponent component = componentFactory.getComponent(batchType);

		// Fetch requested BrAPI entities
		SearchRequest entitySearch = body.getSearchRequest();
		List<?> entities = component.findEntities(entitySearch, metadata);

		// Create a new batch for the requested entites
		List<String> entityDbIds = component.collectDbIds(entities);
		BatchDeleteNewRequest newBatchRequest = new BatchDeleteNewRequest().data(entityDbIds);
		String newBatchDbID = batchService.saveNewBatch(Arrays.asList(newBatchRequest)).get(0).getBatchDeleteDbId();

		return responseOK(newBatchDbID, entityDbIds, metadata);
	}

	@CrossOrigin
	public ResponseEntity<BatchDeletesSingleResponse> batchDeletesBatchDbIdDelete(
			@PathVariable("batchDeleteDbId") String batchDeleteDbId,
			@Valid @RequestParam(value = "hardDelete", defaultValue = "false" ,required = false) boolean hardDelete,
			@RequestHeader(value = "Authorization", required = false) String authorization) throws BrAPIServerException {

		log.debug("Request: " + request.getRequestURI());
		validateSecurityContext(request, "ROLE_USER");
		validateAcceptHeader(request);
		BatchDeleteDetails batch = batchService.getBatch(batchDeleteDbId);
		BatchDeleteTypes batchType = batch.getBatchDeleteType();
		BrAPIComponent component = componentFactory.getComponent(batchType);
		if (hardDelete) {
			component.deleteBatchDeleteData(batch.getData());
			batchService.deleteBatch(batchDeleteDbId);
			return responseNoContent();
		}
		component.softDeleteBatchDeleteData(batch.getData());
		batchService.deleteBatch(batchDeleteDbId);
		return responseNoContent();
	}
}