package org.brapi.test.BrAPITestServer.service.pheno;

import io.swagger.model.Metadata;
import io.swagger.model.pheno.*;
import jakarta.validation.Valid;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerDbIdNotFoundException;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.model.dto.ObservationUnitGermplasmData;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.brapi.test.BrAPITestServer.model.entity.core.SeasonEntity;
import org.brapi.test.BrAPITestServer.model.entity.core.StudyEntity;
import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationEntity;
import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationUnitEntity;
import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationVariableEntity;
import org.brapi.test.BrAPITestServer.repository.primaryEntities.pheno.ObservationRepository;
import org.brapi.test.BrAPITestServer.service.*;
import org.brapi.test.BrAPITestServer.service.core.SeasonService;
import org.brapi.test.BrAPITestServer.service.core.StudyService;
import org.brapi.test.BrAPITestServer.service.germ.GermplasmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class ObservationService {

	private static final Logger log = LoggerFactory.getLogger(ObservationService.class);

	private final ObservationRepository observationRepository;
	private final SeasonService seasonService;
	private final ObservationUnitService observationUnitService;
	private final StudyService studyService;
	private final ObservationVariableService observationVariableService;
	private final GermplasmService germplasmService;

	public ObservationService(ObservationRepository observationRepository, SeasonService seasonService,
							  @Lazy ObservationUnitService observationUnitService, StudyService studyService,
							  ObservationVariableService observationVariableService, GermplasmService germplasmService) {
		this.observationRepository = observationRepository;
		this.seasonService = seasonService;
		this.observationUnitService = observationUnitService;
		this.studyService = studyService;
		this.observationVariableService = observationVariableService;
		this.germplasmService = germplasmService;
	}

	public List<Observation> findObservations(String observationDbId, String observationUnitDbId, String germplasmDbId,
			String observationVariableDbId, String studyDbId, String locationDbId, String trialDbId, String programDbId,
			String seasonDbId, String observationUnitLevelName, String observationUnitLevelOrder,
			String observationUnitLevelCode, String observationTimeStampRangeStart, String observationTimeStampRangeEnd,
			String observationUnitLevelRelationshipName, String observationUnitLevelRelationshipOrder,
			String observationUnitLevelRelationshipCode, String observationUnitLevelRelationshipDbId,
			String commonCropName, String externalReferenceId, String externalReferenceID,
			String externalReferenceSource, Metadata metadata) throws BrAPIServerException {
		ObservationSearchRequest request = buildObservationsSearchRequest(observationDbId, observationUnitDbId,
				germplasmDbId, observationVariableDbId, studyDbId, locationDbId, trialDbId, programDbId, seasonDbId,
				observationUnitLevelName, observationUnitLevelOrder, observationUnitLevelCode,
				observationTimeStampRangeStart, observationTimeStampRangeEnd, observationUnitLevelRelationshipName,
				observationUnitLevelRelationshipOrder, observationUnitLevelRelationshipCode,
				observationUnitLevelRelationshipDbId, commonCropName, externalReferenceId, externalReferenceID,
				externalReferenceSource);

		return findObservations(request, metadata);
	}

	private ObservationSearchRequest buildObservationsSearchRequest(String observationDbId, String observationUnitDbId,
			String germplasmDbId, String observationVariableDbId, String studyDbId, String locationDbId,
			String trialDbId, String programDbId, String seasonDbId, String observationUnitLevelName,
			String observationUnitLevelOrder, String observationUnitLevelCode, String observationTimeStampRangeStart,
			String observationTimeStampRangeEnd, String observationUnitLevelRelationshipName,
			String observationUnitLevelRelationshipOrder, String observationUnitLevelRelationshipCode,
			String observationUnitLevelRelationshipDbId, String commonCropName, String externalReferenceId,
			String externalReferenceID, String externalReferenceSource) throws BrAPIServerException {
		ObservationSearchRequest request = new ObservationSearchRequest();
		if (commonCropName != null)
			request.addCommonCropNamesItem(commonCropName);
		if (germplasmDbId != null)
			request.addGermplasmDbIdsItem(germplasmDbId);
		if (studyDbId != null)
			request.addStudyDbIdsItem(studyDbId);
		if (locationDbId != null)
			request.addLocationDbIdsItem(locationDbId);
		if (trialDbId != null)
			request.addTrialDbIdsItem(trialDbId);
		if (programDbId != null)
			request.addProgramDbIdsItem(programDbId);
		if (seasonDbId != null)
			request.addSeasonDbIdsItem(seasonDbId);
		if (observationDbId != null)
			request.addObservationDbIdsItem(observationDbId);
		if (observationUnitDbId != null)
			request.addObservationUnitDbIdsItem(observationUnitDbId);
		if (observationVariableDbId != null)
			request.addObservationVariableDbIdsItem(observationVariableDbId);
		if (observationUnitLevelName != null || observationUnitLevelOrder != null || observationUnitLevelCode != null) {
			ObservationUnitLevel level = new ObservationUnitLevel();
			if (observationUnitLevelName != null)
				level.setLevelName(observationUnitLevelName);
			if (observationUnitLevelOrder != null)
				level.setLevelOrder(Integer.decode(observationUnitLevelOrder));
			if (observationUnitLevelCode != null)
				level.setLevelCode(observationUnitLevelCode);
			request.addObservationLevelsItem(level);
		}
		if (observationUnitLevelRelationshipName != null || observationUnitLevelRelationshipOrder != null
				|| observationUnitLevelRelationshipCode != null) {
			ObservationUnitLevelRelationship level = new ObservationUnitLevelRelationship();
			if (observationUnitLevelRelationshipName != null)
				level.setLevelName(observationUnitLevelRelationshipName);
			if (observationUnitLevelRelationshipOrder != null)
				level.setLevelOrder(Integer.decode(observationUnitLevelRelationshipOrder));
			if (observationUnitLevelRelationshipCode != null)
				level.setLevelCode(observationUnitLevelRelationshipCode);
			if (observationUnitLevelRelationshipDbId != null)
				level.setObservationUnitDbId(observationUnitLevelRelationshipDbId);
			request.addObservationLevelRelationshipsItem(level);
		}
		if (observationTimeStampRangeStart != null)
			request.setObservationTimeStampRangeStart(DateUtility.toOffsetDateTime(observationTimeStampRangeStart));
		if (observationTimeStampRangeEnd != null)
			request.setObservationTimeStampRangeEnd(DateUtility.toOffsetDateTime(observationTimeStampRangeEnd));

		request.addExternalReferenceItem(externalReferenceId, externalReferenceID, externalReferenceSource);

		return request;
	}

	public ObservationTable findObservationsTable(String observationUnitDbId, String germplasmDbId,
			String observationVariableDbId, String studyDbId, String locationDbId, String trialDbId, String programDbId,
			String seasonDbId, String observationLevel, String observationUnitLevelName,
			String observationUnitLevelOrder, String observationUnitLevelCode,
			String observationUnitLevelRelationshipName, String observationUnitLevelRelationshipOrder,
			String observationUnitLevelRelationshipCode, String observationUnitLevelRelationshipDbId,
			String observationTimeStampRangeStart, String observationTimeStampRangeEnd, String searchResultsDbId, Metadata metadata)
			throws BrAPIServerException {
		ObservationSearchRequest obsRequest = buildObservationsSearchRequest(null, observationUnitDbId, germplasmDbId,
				observationVariableDbId, studyDbId, locationDbId, trialDbId, programDbId, seasonDbId,
				observationUnitLevelName, observationUnitLevelOrder, observationUnitLevelCode,
				observationTimeStampRangeStart, observationTimeStampRangeEnd, observationUnitLevelRelationshipName,
				observationUnitLevelRelationshipOrder, observationUnitLevelRelationshipCode,
				observationUnitLevelRelationshipDbId, null, null, null, null);
		return findObservationsTable(obsRequest, metadata);
	}

	public ObservationTable findObservationsTable(ObservationSearchRequest obsRequest, Metadata metadata)
		throws BrAPIServerException {
		Page<ObservationEntity> page = findObservationEntities(obsRequest, metadata);
		log.debug("converting "+page.getSize()+" entities");

		List<ObservationVariableEntity> variables = page.stream().map(ObservationEntity::getObservationVariable)
				.filter(Objects::nonNull).distinct().collect(Collectors.toList());

		ObservationTable table = new ObservationTable();
		// TODO: Add support for dynamic observation unit level names for ouPosition in both buildDataMatrix() and buildHeaderRow(), using rq as input
		table.setData(buildDataMatrix(page, variables));
		table.setHeaderRow(buildHeaderRow());
		table.setObservationVariables(variables.stream().map(this::convertVariables).collect(Collectors.toList()));
		log.debug("done converting entities");
		PagingUtility.calculateMetaData(metadata, page);
		return table;
	}

	public List<Observation> findObservations(@Valid ObservationSearchRequest request, Metadata metadata)
		throws BrAPIServerException {
		Page<ObservationEntity> page = findObservationEntities(request, metadata);
		log.debug("converting "+page.getSize()+" entities");
		List<Observation> observations = convertFromEntitiesInBatch(page.getContent());
		log.debug("done converting entities");
		PagingUtility.calculateMetaData(metadata, page);
		return observations;
	}

	public Page<ObservationEntity> findObservationEntities(@Valid ObservationSearchRequest request, Metadata metadata)
		throws BrAPIServerException {
		Pageable pageReq = PagingUtility.getPageRequest(metadata);
		SearchQueryBuilder<ObservationEntity> searchQuery = new SearchQueryBuilder<ObservationEntity>(
				ObservationEntity.class);
		searchQuery.leftJoinFetch("observationVariable", "observationVariable")
				.leftJoinFetch("*observationVariable.crop", "varCrop")
				.leftJoinFetch("*observationVariable.method", "varMethod")
				.leftJoinFetch("*observationVariable.ontology", "varOntology")
				.leftJoinFetch("*observationVariable.scale", "varScale")
				.leftJoinFetch("*observationVariable.trait", "varTrait")
				.leftJoinFetch("season", "season")
				.leftJoinFetch("program", "program")
				.leftJoinFetch("trial", "trial")
				.leftJoinFetch("geoCoordinates", "geoCoordinates")
				.leftJoinFetch("observationUnit", "observationUnit")
				.leftJoinFetch("*observationUnit.position", "position")
				.leftJoinFetch("*position.geoCoordinates", "ouGeoCoordinates")
				.leftJoinFetch("*observationUnit.germplasm", "ouGermplasm")
				.leftJoinFetch("*ouGermplasm.pedigree", "pedigree")
				.leftJoinFetch("*observationUnit.study", "ouStudy")
				.leftJoinFetch("study", "study");
		if (request.getObservationLevels() != null) {
			searchQuery = searchQuery
					// TODO: This will likely need to be updated so the search works by program and with globally available level names
					.appendList(
							request.getObservationLevels().stream().filter(r -> r.getLevelName() != null)
									.map(r -> r.getLevelName()).collect(Collectors.toList()),
							"observationUnit.position.level.levelName")
					.appendList(
							request.getObservationLevels().stream().filter(r -> r.getLevelCode() != null)
									.map(r -> r.getLevelCode()).collect(Collectors.toList()),
							"observationUnit.position.level.levelCode")
					.appendIntList(
							request.getObservationLevels().stream().filter(r -> r.getLevelOrder() != null)
									.map(r -> r.getLevelOrder()).collect(Collectors.toList()),
							"observationUnit.position.level.levelOrder");
		}
		if (request.getObservationLevelRelationships() != null) {
			searchQuery = searchQuery.join("observationUnit.position.levelRelationships", "levelRelationship")
					// TODO: This will likely need to be updated so the search works by program and with globally available level names
					.appendList(
							request.getObservationLevelRelationships().stream().filter(r -> r.getLevelName() != null)
									.map(r -> r.getLevelName()).collect(Collectors.toList()),
							"levelRelationship.levelName")
					.appendList(
							request.getObservationLevelRelationships().stream().filter(r -> r.getLevelCode() != null)
									.map(r -> r.getLevelCode()).collect(Collectors.toList()),
							"levelRelationship.levelCode")
					.appendIntList(
							request.getObservationLevelRelationships().stream().filter(r -> r.getLevelOrder() != null)
									.map(r -> r.getLevelOrder()).collect(Collectors.toList()),
							"levelRelationship.levelOrder");
		}
		searchQuery = searchQuery.withExRefs(request.getExternalReferenceIDs(), request.getExternalReferenceSources())
				.appendList(request.getGermplasmDbIds(), "observationUnit.germplasm.id")
				.appendList(request.getGermplasmNames(), "observationUnit.germplasm.germplasmName")
				.appendList(request.getLocationDbIds(), "study.location.id")
				.appendList(request.getLocationNames(), "study.location.locationName")
				.appendList(request.getObservationDbIds(), "id")
				.appendList(request.getObservationUnitDbIds(), "observationUnit.id")
				.appendDateRange(request.getObservationTimeStampRangeStart(), request.getObservationTimeStampRangeEnd(),
						"observationTimeStamp")
				.appendList(request.getObservationVariableDbIds(), "observationVariable.id")
				.appendList(request.getObservationVariableNames(), "observationVariable.name")
				.appendList(request.getCommonCropNames(), "crop.crop_name")
				.appendList(request.getProgramDbIds(), "program.id")
				.appendList(request.getProgramNames(), "program.name").appendList(request.getSeasonDbIds(), "season.id")
				.appendList(request.getStudyDbIds(), "study.id").appendList(request.getStudyNames(), "study.studyName")
				.appendList(request.getTrialDbIds(), "trial.id").appendList(request.getTrialNames(), "trial.trialName");

		log.debug("starting search");
		Page<ObservationEntity> observations = observationRepository.findAllBySearchPaginatingWithFetches(searchQuery, pageReq);

		List<UUID> ids = observations.map(BrAPIBaseEntity::getId).toList();
		log.debug("search complete");

		if(!observations.isEmpty()) {
			observationRepository.fetchXrefs(ids, observations, ObservationEntity.class);
		}
		return observations;
	}

	public Observation getObservation(String observationDbId) throws BrAPIServerException {
		return convertFromEntity(getObservationEntity(observationDbId, HttpStatus.NOT_FOUND));
	}

	public ObservationEntity getObservationEntity(String observationDbId) throws BrAPIServerException {
		return getObservationEntity(observationDbId, HttpStatus.BAD_REQUEST);
	}

	public ObservationEntity getObservationEntity(String observationDbId, HttpStatus errorStatus)
			throws BrAPIServerException {
		ObservationEntity observation = null;
		Optional<ObservationEntity> entityOpt = observationRepository.findById(UUID.fromString(observationDbId));
		if (entityOpt.isPresent()) {
			observation = entityOpt.get();
		} else {
			throw new BrAPIServerDbIdNotFoundException("observation", observationDbId, errorStatus);
		}
		return observation;
	}

	public List<Observation> saveObservations(@Valid List<ObservationNewRequest> requests) {
		List<ObservationEntity> toSave = createEntitiesInBatch(requests);

		var savedEntities = observationRepository.saveAll(toSave);

		return convertFromEntitiesInBatch(savedEntities);
	}

	public List<Observation> updateObservations(@Valid Map<String, ObservationNewRequest> requests)
			throws BrAPIServerException {
		List<ObservationEntity> savedObservationEntities = new ArrayList<>();

		for (Entry<String, ObservationNewRequest> entry : requests.entrySet()) {
			ObservationEntity saved = updateObservation(entry.getKey(), entry.getValue());
			savedObservationEntities.add(saved);
		}

		return convertFromEntitiesInBatch(savedObservationEntities);
	}

	public List<String> deleteObservations(ObservationSearchRequest body, Metadata metadata)
		throws BrAPIServerException {
		List<String> deletedObservationDbIds = new ArrayList<>();

		if (body.getTotalParameterCount() > 0) {
			List<ObservationEntity> deletedObservations = findObservationEntities(body, metadata).getContent();
			observationRepository.deleteAll(deletedObservations);
			deletedObservationDbIds = deletedObservations.stream().map(obs -> obs.getId().toString()).collect(Collectors.toList());
		}

		return deletedObservationDbIds;
	}

	public ObservationEntity updateObservation(String observationDbId, ObservationNewRequest request)
			throws BrAPIServerException {
		ObservationEntity entity = getObservationEntity(observationDbId, HttpStatus.NOT_FOUND);
		updateEntity(entity, request);
		return observationRepository.save(entity);
	}

	public Observation updateObservationAndConvert(String observationDbId, ObservationNewRequest request)
			throws BrAPIServerException {
		ObservationEntity updatedEntity = updateObservation(observationDbId, request);

		return convertFromEntity(updatedEntity);
	}

	// For single entity conversion use case
	public Observation convertFromEntity(ObservationEntity entity) {
		return convertFromEntitiesInBatch(List.of(entity)).getFirst();
	}

	public List<Observation> convertFromEntitiesInBatch(List<ObservationEntity> entities) {
		return convertFromEntitiesInBatch(entities, null);
	}

	public List<Observation> convertFromEntitiesInBatch(List<ObservationEntity> entities,
														Map<UUID, ObservationUnitGermplasmData> germplasmDataByOUId) {
		var result = new ArrayList<Observation>();

		if (entities.isEmpty()) {
			return result;
		}

		if (germplasmDataByOUId == null) {
			germplasmDataByOUId
					= observationUnitService.fetchObservationUnitGermplasmData(
					entities.stream()
							.filter(obs -> obs.getObservationUnit() != null)
							.map(obs -> obs.getObservationUnit().getId())
							.distinct()
							.toList()
			);
		}

		for (ObservationEntity entity : entities) {
			log.trace("converting obs: " + entity.getId().toString());
			Observation observation = new Observation();
			UpdateUtility.convertFromEntity(entity, observation);

			observation.setCollector(entity.getCollector());
			observation.setGeoCoordinates(GeoJSONUtility.convertFromEntity(entity.getGeoCoordinates()));
			observation.setObservationDbId(entity.getId().toString());
			observation.setObservationTimeStamp(DateUtility.toOffsetDateTime(entity.getObservationTimeStamp()));

			if (entity.getObservationVariable() != null) {
				observation.setObservationVariableDbId(entity.getObservationVariable().getId().toString());
				observation.setObservationVariableName(entity.getObservationVariable().getName());
			}
			if (entity.getSeason() != null) {
				observation.setSeason(seasonService.convertFromEntity(entity.getSeason()));
			}
			observation.setUploadedBy(entity.getUploadedBy());
			observation.setValue(entity.getValue());

			if (entity.getObservationUnit() != null) {

				observation.setObservationUnitDbId(entity.getObservationUnit().getId().toString());
				observation.setObservationUnitName(entity.getObservationUnit().getObservationUnitName());

				var germplasmData = germplasmDataByOUId.get(entity.getObservationUnit().getId());

				if (germplasmData != null) {
					observation.setGermplasmDbId(germplasmData.getGermplasmDbId());
					observation.setGermplasmName(germplasmData.getGermplasmName());
				}
				if (entity.getObservationUnit().getStudy() != null) {
					observation.setStudyDbId(entity.getObservationUnit().getStudy().getId().toString());
				}
			} else if (entity.getStudy() != null) {
				observation.setStudyDbId(entity.getStudy().getId().toString());
			}

			result.add(observation);
		}

		return result;
	}

	private void updateEntity(ObservationEntity entity, ObservationNewRequest observation) throws BrAPIServerException {
		UpdateUtility.updateEntity(observation, entity);

		if (observation.getCollector() != null)
			entity.setCollector(observation.getCollector());
		if (observation.getGeoCoordinates() != null)
			entity.setGeoCoordinates(GeoJSONUtility.convertToEntity(observation.getGeoCoordinates()));
		if (observation.getObservationTimeStamp() != null)
			entity.setObservationTimeStamp(DateUtility.toDate(observation.getObservationTimeStamp()));
		if (observation.getObservationVariableDbId() != null) {
			ObservationVariableEntity observationVariable = observationVariableService
					.getObservationVariableEntity(observation.getObservationVariableDbId());
			entity.setObservationVariable(observationVariable);
		}
		if (observation.getSeason() != null && observation.getSeason().getSeasonDbId() != null) {
			SeasonEntity season = seasonService.getSeasonEntity(observation.getSeason().getSeasonDbId());
			entity.setSeason(season);
		}
		if (observation.getUploadedBy() != null)
			entity.setUploadedBy(observation.getUploadedBy());
		if (observation.getValue() != null)
			entity.setValue(observation.getValue());

		if (observation.getObservationUnitDbId() != null) {
			ObservationUnitEntity observationUnit = observationUnitService
					.getObservationUnitEntity(observation.getObservationUnitDbId());
			entity.setObservationUnit(observationUnit);
		} else if (observation.getStudyDbId() != null) {
			StudyEntity study = studyService.getStudyEntity(observation.getStudyDbId());
			entity.setStudy(study);
		}
	}

	private List<ObservationEntity> createEntitiesInBatch(List<ObservationNewRequest> observations) {

		var observationVarIds = observations.stream()
				.map(ObservationNewRequest::getObservationVariableDbId)
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		var seasonIds = observations.stream()
				.map(obs -> {
					if (obs.getSeason() != null && obs.getSeason().getSeasonDbId() != null) {
						return obs.getSeason().getSeasonDbId();
					}

					return null;
				})
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		var observationUnitIds = observations.stream()
				.map(ObservationNewRequest::getObservationUnitDbId)
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		var studyIds = observations.stream()
				.map(ObservationNewRequest::getStudyDbId)
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		var foundObsVarsById = observationVariableService.findByIds(observationVarIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		var foundSeasonsById = seasonService.findByIds(seasonIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		var foundObsUnitsById = observationUnitService.findByIds(observationUnitIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		var foundStudiesById = studyService.findByIds(studyIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		var result = new ArrayList<ObservationEntity>();

		for (ObservationNewRequest observation : observations) {
			var entity = new ObservationEntity();

			UpdateUtility.updateEntity(observation, entity);

			if (observation.getCollector() != null)
				entity.setCollector(observation.getCollector());
			if (observation.getGeoCoordinates() != null)
				entity.setGeoCoordinates(GeoJSONUtility.convertToEntity(observation.getGeoCoordinates()));
			if (observation.getObservationTimeStamp() != null)
				entity.setObservationTimeStamp(DateUtility.toDate(observation.getObservationTimeStamp()));
			if (observation.getObservationVariableDbId() != null) {
				entity.setObservationVariable(foundObsVarsById.get(UUID.fromString(observation.getObservationVariableDbId())));
			}
			if (observation.getSeason() != null && observation.getSeason().getSeasonDbId() != null) {
				entity.setSeason(foundSeasonsById.get(UUID.fromString(observation.getSeason().getSeasonDbId())));
			}
			if (observation.getUploadedBy() != null)
				entity.setUploadedBy(observation.getUploadedBy());
			if (observation.getValue() != null)
				entity.setValue(observation.getValue());

			if (observation.getObservationUnitDbId() != null) {
				entity.setObservationUnit(foundObsUnitsById.get(UUID.fromString(observation.getObservationUnitDbId())));
			} else if (observation.getStudyDbId() != null) {
				entity.setStudy(foundStudiesById.get(UUID.fromString(observation.getStudyDbId())));
			}

			result.add(entity);
		}

		return result;
	}

	private List<List<String>> buildDataMatrix(Page<ObservationEntity> observations,
			List<ObservationVariableEntity> variables) {
		List<List<String>> data = new ArrayList<>();
		for (ObservationEntity obs : observations) {
			List<String> row = new ArrayList<>();

			if (obs.getObservationUnit() != null) {
				ObservationUnitEntity obsUnit = obs.getObservationUnit();
				if (obsUnit.getStudy() != null) {
					StudyEntity study = obsUnit.getStudy();
					if (study.getSeasons() != null && !study.getSeasons().isEmpty()) {
						row.add(printIfNotNull(study.getSeasons().get(0).getYear())); // YEAR
					} else {
						row.add(""); // YEAR
					}

					row.add(printIfNotNull(study.getId().toString())); // STUDYDBID
					row.add(printIfNotNull(study.getStudyName())); // STUDYNAME

//					if (study.getLocation() != null) {
//						row.add(printIfNotNull(study.getLocation().getId().toString())); // LOCATIONDBID
//						row.add(printIfNotNull(study.getLocation().getLocationName())); // LOCATIONNAME
//					} else {
//						row.add(""); // LOCATIONDBID
//						row.add(""); // LOCATIONNAME
//					}

				} else {
					row.add(""); // YEAR
					row.add(""); // STUDYDBID
					row.add(""); // STUDYNAME
					row.add(""); // LOCATIONDBID
					row.add(""); // LOCATIONNAME
				}

				if (obsUnit.getGermplasm() != null) {
					row.add(printIfNotNull(obsUnit.getGermplasm().getId().toString())); // GERMPLASMDBID
					row.add(printIfNotNull(obsUnit.getGermplasm().getGermplasmName())); // GERMPLASMNAME
				} else {
					row.add(""); // GERMPLASMDBID
					row.add(""); // GERMPLASMNAME
				}

				row.add(printIfNotNull(obsUnit.getId().toString())); // OBSERVATIONUNITDBID
				row.add(printIfNotNull(obsUnit.getObservationUnitName())); // OBSERVATIONUNITNAME

				if (obsUnit.getPosition() != null) {
					row.add(printIfNotNull(obsUnit.getPosition().getPositionCoordinateX())); // POSITIONCOORDINATEX
					row.add(printIfNotNull(obsUnit.getPosition().getPositionCoordinateY())); // POSITIONCOORDINATEY
				} else {
					row.add(""); // POSITIONCOORDINATEX
					row.add(""); // POSITIONCOORDINATEY
				}

			} else {
				continue;
			}

			row.add(printIfNotNull(obs.getObservationTimeStamp())); // OBSERVATIONTIMESTAMP

			for (ObservationVariableEntity var : variables) {
				if (obs.getObservationVariable() != null && obs.getObservationVariable().getId().toString() == var.getId().toString()) {
					row.add(obs.getValue());
				} else {
					row.add("");
				}
			}
			data.add(row);
		}
		return data;
	}

	private String printIfNotNull(Object toPrint) {
		if (toPrint == null) {
			return "";
		}
		return toPrint.toString();
	}

	private List<ObservationTableHeaderRowEnum> buildHeaderRow() {
		List<ObservationTableHeaderRowEnum> headers = new ArrayList<>();
		headers.add(ObservationTableHeaderRowEnum.YEAR);
		headers.add(ObservationTableHeaderRowEnum.STUDYDBID);
		headers.add(ObservationTableHeaderRowEnum.STUDYNAME);
//		headers.add(ObservationTableHeaderRowEnum.LOCATIONDBID);
//		headers.add(ObservationTableHeaderRowEnum.LOCATIONNAME);
		headers.add(ObservationTableHeaderRowEnum.GERMPLASMDBID);
		headers.add(ObservationTableHeaderRowEnum.GERMPLASMNAME);
		headers.add(ObservationTableHeaderRowEnum.OBSERVATIONUNITDBID);
		headers.add(ObservationTableHeaderRowEnum.OBSERVATIONUNITNAME);
		headers.add(ObservationTableHeaderRowEnum.POSITIONCOORDINATEX);
		headers.add(ObservationTableHeaderRowEnum.POSITIONCOORDINATEY);
		headers.add(ObservationTableHeaderRowEnum.OBSERVATIONTIMESTAMP);
		return headers;
	}

	public String getObservationTableText(ObservationTable table, String sep) {
		StringBuilder responseBuilder = new StringBuilder();

		for (ObservationTableHeaderRowEnum header : table.getHeaderRow()) {
			responseBuilder.append("\"" + header.toString() + "\"");
			responseBuilder.append(sep);
		}
		int i = 1;
		for (ObservationTableObservationVariables header : table.getObservationVariables()) {
			responseBuilder.append("\"" + header.getObservationVariableDbId() + "\"");
			if (i < table.getObservationVariables().size()) {
				responseBuilder.append(sep);
			}
			i++;
		}
		responseBuilder.append("\n");

		for (List<String> row : table.getData()) {
			int j = 1;
			for (String item : row) {
				responseBuilder.append("\"" + item + "\"");
				if (j < row.size()) {
					responseBuilder.append(sep);
				}
				j++;
			}
			responseBuilder.append("\n");
		}

		return responseBuilder.toString();
	}

	private ObservationTableObservationVariables convertVariables(ObservationVariableEntity variable) {
		ObservationTableObservationVariables header = new ObservationTableObservationVariables();
		header.setObservationVariableDbId(variable.getId().toString());
		header.setObservationVariableName(variable.getName());
		return header;
	}
}
