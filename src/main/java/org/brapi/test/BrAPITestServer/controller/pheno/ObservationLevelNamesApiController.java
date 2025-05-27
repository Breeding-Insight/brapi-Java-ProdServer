package org.brapi.test.BrAPITestServer.controller.pheno;


import io.swagger.annotations.ApiParam;
import io.swagger.api.pheno.ObservationLevelNamesApi;
import io.swagger.model.pheno.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.brapi.test.BrAPITestServer.controller.core.BrAPIController;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.service.pheno.ObservationUnitLevelNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@javax.annotation.processing.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-21T16:32:22.556Z[GMT]")
@Controller
public class ObservationLevelNamesApiController extends BrAPIController
    implements ObservationLevelNamesApi {

    private static final Logger log = LoggerFactory.getLogger(ObservationLevelNamesApiController.class);
    private final HttpServletRequest request;
    private final ObservationUnitLevelNameService observationUnitLevelNameService;

    @Autowired
    public ObservationLevelNamesApiController(ObservationUnitLevelNameService observationUnitLevelNameService,
                                              HttpServletRequest request) {
        this.observationUnitLevelNameService = observationUnitLevelNameService;
        this.request = request;
    }

    @CrossOrigin
    @Override
    public ResponseEntity<ObservationLevelListResponse> observationlevelnamesGet(
            // If supplied, grabs all Observation Level Names for the given programDbId.
            // If not supplied, grabs all global Observation Level Names.
            @RequestParam(value = "programDbId", required = false) String programDbId,
            // Used to grab all the Observation Level Names in the system, both global, and all level names for all params
            @RequestParam(value = "all", required = false) Boolean all,
            @RequestHeader(value = "Authorization", required = false) String authorization)
            throws BrAPIServerException {

        log.debug("Request: " + request.getRequestURI());
        validateSecurityContext(request, "ROLE_ANONYMOUS", "ROLE_USER");
        validateAcceptHeader(request);
        var foundLevelNames = observationUnitLevelNameService.findObservationUnitLevelNames(programDbId, all);
        var data = observationUnitLevelNameService.convertFromEntitiesInBatch(foundLevelNames);
        return responseOK(new ObservationLevelListResponse(), new ObservationLevelListResponseResult(), data, null);
    }

    @CrossOrigin
    @Override
    public ResponseEntity<ObservationLevelListResponse> observationlevelnamesPost(
            @RequestBody List<ObservationLevelNewRequest> body,
            @RequestHeader(value = "Authorization", required = false) String authorization)
            throws BrAPIServerException {

        log.debug("Request: " + request.getRequestURI());
        validateSecurityContext(request, "ROLE_ANONYMOUS", "ROLE_USER");
        validateAcceptHeader(request);
        var data = observationUnitLevelNameService.save(body);
        return responseOK(new ObservationLevelListResponse(), new ObservationLevelListResponseResult(), data, null);
    }

    @CrossOrigin
    @Override
    public ResponseEntity<ObservationLevelListResponse> observationlevelnamesDelete(
            @PathVariable("observationlevelnameDbId") String observationlevelnameDbId,
            @RequestHeader(value = "Authorization", required = false) String authorization)
            throws BrAPIServerException {

        log.debug("Request: " + request.getRequestURI());
        validateSecurityContext(request, "ROLE_USER");
        validateAcceptHeader(request);

        observationUnitLevelNameService.deleteObservationLevelName(observationlevelnameDbId);
        return responseNoContent();
    }

    @CrossOrigin
    @Override
    public ResponseEntity<ObservationLevelSingleResponse> observationlevelnamesDbIdPut(
            @ApiParam(value = "The unique ID of the specific Observation Level name", required = true) @PathVariable("observationlevelnameDbId") String observationlevelnameDbId,
            @ApiParam(value = "") @Valid @RequestBody ObservationLevelNewRequest body,
            @ApiParam(value = "HTTP HEADER - Token used for Authorization   <strong> Bearer {token_string} </strong>") @RequestHeader(value = "Authorization", required = false) String authorization)
            throws BrAPIServerException {
        log.debug("Request: " + request.getRequestURI());
        validateSecurityContext(request, "ROLE_USER");
        validateAcceptHeader(request);

        var data = observationUnitLevelNameService.update(observationlevelnameDbId, body);
        return responseOK(new ObservationLevelSingleResponse(), data);
    }
}
