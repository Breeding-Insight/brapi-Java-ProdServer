package io.swagger.api.pheno;

import io.swagger.annotations.*;
import io.swagger.model.pheno.*;
import jakarta.validation.Valid;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@javax.annotation.processing.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-21T16:32:22.556Z[GMT]")
@Api(value = "observationlevelnames", description = "the observationlevelnames API")
public interface ObservationLevelNamesApi {
    @ApiOperation(value = "Get the Observation Level Names", nickname = "observationlevelnamesGet", notes = "Call to retrieve the list of supported observation level names", response = ObservationLevelListResponse.class, authorizations = {
            @Authorization(value = "AuthorizationToken") }, tags = { "Observation level names", })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ObservationLevelListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = String.class),
            @ApiResponse(code = 403, message = "Forbidden", response = String.class) })
    @RequestMapping(value = "/observationlevelnames", produces = { "application/json" }, method = RequestMethod.GET)
    ResponseEntity<ObservationLevelListResponse> observationlevelnamesGet(
            @ApiParam(value = "programDbId") @Valid @RequestParam(value = "programDbId", required = false) String programDbId,
            @ApiParam(value = "all") @Valid @RequestParam(value = "all", required = false) Boolean all,
            @ApiParam(value = "page") @Valid @RequestParam(value = "page", required = false) Integer page,
            @ApiParam(value = "pageSize") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "HTTP HEADER - Token used for Authorization   <strong> Bearer {token_string} </strong>") @RequestHeader(value = "Authorization", required = false) String authorization)
            throws BrAPIServerException;

    @ApiOperation(value = "Create Observation Level Names", nickname = "observationlevelnamesPost", notes = "Call to save a list of observation level names", response = ObservationLevelListResponse.class, authorizations = {
            @Authorization(value = "AuthorizationToken") }, tags = { "Observation level names", })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ObservationLevelListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = String.class),
            @ApiResponse(code = 403, message = "Forbidden", response = String.class) })
    @RequestMapping(value = "/observationlevelnames", produces = { "application/json" }, method = RequestMethod.POST)
    ResponseEntity<ObservationLevelListResponse> observationlevelnamesPost(
            @ApiParam(value = "") @Valid @RequestBody List<ObservationLevelNewRequest> body,
            @ApiParam(value = "HTTP HEADER - Token used for Authorization   <strong> Bearer {token_string} </strong>") @RequestHeader(value = "Authorization", required = false) String authorization)
            throws BrAPIServerException;

    @ApiOperation(value = "Delete Observation Level Names", nickname = "observationlevelnamesGet", notes = "Call to delete a list of observation level names", response = ObservationLevelListResponse.class, authorizations = {
            @Authorization(value = "AuthorizationToken") }, tags = { "Observation level names", })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ObservationLevelListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = String.class),
            @ApiResponse(code = 403, message = "Forbidden", response = String.class) })
    @RequestMapping(value = "/observationlevelnames/{observationlevelnameDbId}", produces = { "application/json" }, method = RequestMethod.DELETE)
    ResponseEntity<ObservationLevelListResponse> observationlevelnamesDelete(
            @ApiParam(value = "The unique ID of this generic list", required = true) @PathVariable("observationlevelnameDbId") String observationLevelNameDbId,
            @ApiParam(value = "HTTP HEADER - Token used for Authorization   <strong> Bearer {token_string} </strong>") @RequestHeader(value = "Authorization", required = false) String authorization)
            throws BrAPIServerException;

    @ApiOperation(value = "Update an existing Observation Level Name", nickname = "observationlevelnamesDbIdPut", notes = "Update an existing Observation Level Name", response = ObservationLevelListResponse.class, authorizations = {
            @Authorization(value = "AuthorizationToken") }, tags = { "Observation Units", })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ObservationUnitHierarchyLevel.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = String.class),
            @ApiResponse(code = 403, message = "Forbidden", response = String.class) })
    @RequestMapping(value = "/observationlevelnames/{observationlevelnameDbId}", produces = { "application/json" }, consumes = {
            "application/json" }, method = RequestMethod.PUT)
    ResponseEntity<ObservationLevelSingleResponse> observationlevelnamesDbIdPut(
            @ApiParam(value = "The unique ID of the specific Observation Level Name", required = true) @PathVariable("observationlevelnameDbId") String observationlevelnameDbId,
            @ApiParam(value = "") @Valid @RequestBody ObservationLevelNewRequest body,
            @ApiParam(value = "HTTP HEADER - Token used for Authorization   <strong> Bearer {token_string} </strong>") @RequestHeader(value = "Authorization", required = false) String authorization)
            throws BrAPIServerException;
}

