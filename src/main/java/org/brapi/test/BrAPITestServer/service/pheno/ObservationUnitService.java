package org.brapi.test.BrAPITestServer.service.pheno;

import io.swagger.model.IndexPagination;
import io.swagger.model.Metadata;
import io.swagger.model.pheno.*;
import jakarta.validation.Valid;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerDbIdNotFoundException;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.model.dto.ObservationUnitGermplasmData;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.brapi.test.BrAPITestServer.model.entity.core.ProgramEntity;
import org.brapi.test.BrAPITestServer.model.entity.core.StudyEntity;
import org.brapi.test.BrAPITestServer.model.entity.core.TrialEntity;
import org.brapi.test.BrAPITestServer.model.entity.germ.CrossEntity;
import org.brapi.test.BrAPITestServer.model.entity.germ.GermplasmEntity;
import org.brapi.test.BrAPITestServer.model.entity.germ.SeedLotEntity;
import org.brapi.test.BrAPITestServer.model.entity.pheno.*;
import org.brapi.test.BrAPITestServer.repository.primaryEntities.pheno.ObservationUnitRepository;
import org.brapi.test.BrAPITestServer.service.GeoJSONUtility;
import org.brapi.test.BrAPITestServer.service.PagingUtility;
import org.brapi.test.BrAPITestServer.service.SearchQueryBuilder;
import org.brapi.test.BrAPITestServer.service.UpdateUtility;
import org.brapi.test.BrAPITestServer.service.core.ProgramService;
import org.brapi.test.BrAPITestServer.service.core.StudyService;
import org.brapi.test.BrAPITestServer.service.core.TrialService;
import org.brapi.test.BrAPITestServer.service.germ.CrossService;
import org.brapi.test.BrAPITestServer.service.germ.GermplasmService;
import org.brapi.test.BrAPITestServer.service.germ.SeedLotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class ObservationUnitService {

	private static final Logger log = LoggerFactory.getLogger(ObservationUnitService.class);
	private final ObservationUnitRepository observationUnitRepository;
	private final GermplasmService germplasmService;
	private final CrossService crossService;
	private final ObservationService observationService;
	private final StudyService studyService;
	private final TrialService trialService;
	private final ProgramService programService;
	private final SeedLotService seedLotService;
	private final ObservationVariableService observationVariableService;
	private final ObservationUnitLevelNameService observationUnitLevelNameService;

	@Autowired
	public ObservationUnitService(ObservationUnitRepository observationUnitRepository, StudyService studyService,
			TrialService trialService, ProgramService programService, ObservationService observationService,
			GermplasmService germplasmService, SeedLotService seedLotService, CrossService crossService,
			ObservationVariableService observationVariableService, ObservationUnitLevelNameService observationUnitLevelNameService) {
		this.observationUnitRepository = observationUnitRepository;

		this.studyService = studyService;
		this.trialService = trialService;
		this.programService = programService;
		this.crossService = crossService;
		this.germplasmService = germplasmService;
		this.observationService = observationService;
		this.seedLotService = seedLotService;
		this.observationVariableService = observationVariableService;
		this.observationUnitLevelNameService = observationUnitLevelNameService;
	}

	public List<ObservationUnit> findObservationUnits(String observationUnitDbId, String observationUnitName,
			String germplasmDbId, String studyDbId, String locationDbId, String trialDbId, String programDbId,
			String seasonDbId, String observationUnitLevelName, String observationUnitLevelOrder,
			String observationUnitLevelCode, String observationUnitLevelRelationshipName,
			String observationUnitLevelRelationshipOrder, String observationUnitLevelRelationshipCode,
			String observationUnitLevelRelationshipDbId, String commonCropName, Boolean includeObservations,
			String externalReferenceId, String externalReferenceID, String externalReferenceSource, Metadata metadata)
		throws BrAPIServerException {
		ObservationUnitSearchRequest request = buildObservationUnitsSearchRequest(observationUnitDbId,
				observationUnitName, germplasmDbId, studyDbId, locationDbId, trialDbId, programDbId, seasonDbId,
				observationUnitLevelName, observationUnitLevelOrder, observationUnitLevelCode,
				observationUnitLevelRelationshipName, observationUnitLevelRelationshipOrder,
				observationUnitLevelRelationshipCode, observationUnitLevelRelationshipDbId, commonCropName,
				includeObservations, externalReferenceId, externalReferenceID, externalReferenceSource);
		return findObservationUnits(request, metadata);
	}

	private ObservationUnitSearchRequest buildObservationUnitsSearchRequest(String observationUnitDbId,
			String observationUnitName, String germplasmDbId, String studyDbId, String locationDbId, String trialDbId,
			String programDbId, String seasonDbId, String observationUnitLevelName, String observationUnitLevelOrder,
			String observationUnitLevelCode, String observationUnitLevelRelationshipName,
			String observationUnitLevelRelationshipOrder, String observationUnitLevelRelationshipCode,
			String observationUnitLevelRelationshipDbId, String commonCropName, Boolean includeObservations,
			String externalReferenceId, String externalReferenceID, String externalReferenceSource) {

		ObservationUnitSearchRequest request = new ObservationUnitSearchRequest();
		if (observationUnitDbId != null)
			request.addObservationUnitDbIdsItem(observationUnitDbId);
		if (observationUnitName != null)
			request.addObservationUnitNamesItem(observationUnitName);
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
		if (includeObservations != null)
			request.setIncludeObservations(includeObservations);

		request.addExternalReferenceItem(externalReferenceId, externalReferenceID, externalReferenceSource);

		return request;
	}

	public ObservationUnitTable findObservationUnitsTable(String observationUnitDbId, String germplasmDbId,
			String observationVariableDbId, String studyDbId, String locationDbId, String trialDbId, String programDbId,
			String seasonDbId, String observationLevel, String observationUnitLevelName,
			String observationUnitLevelOrder, String observationUnitLevelCode,
			String observationUnitLevelRelationshipName, String observationUnitLevelRelationshipOrder,
			String observationUnitLevelRelationshipCode, String observationUnitLevelRelationshipDbId)
		throws BrAPIServerException {

		ObservationUnitSearchRequest ouRequest = buildObservationUnitsSearchRequest(observationUnitDbId, null,
				germplasmDbId, studyDbId, locationDbId, trialDbId, programDbId, seasonDbId, observationUnitLevelName,
				observationUnitLevelOrder, observationUnitLevelCode, observationUnitLevelRelationshipName,
				observationUnitLevelRelationshipOrder, observationUnitLevelRelationshipCode,
				observationUnitLevelRelationshipDbId, null, false, null, null, null);
		Page<ObservationUnitEntity> observationUnits = findObservationUnitEntities(ouRequest, null);

		ObservationVariableSearchRequest varRequest = new ObservationVariableSearchRequest();
		varRequest
				.setObservationUnitDbIds(observationUnits.stream().map(ou -> ou.getId().toString()).collect(Collectors.toList()));
		List<ObservationVariable> variables = observationVariableService.findObservationVariables(varRequest, null);

		ObservationUnitTable table = new ObservationUnitTable();
		// TODO: Add support for dynamic observation unit level names for ouPosition in both buildDataMatrix() and buildHeaderRow(), using rq as input
		table.setData(buildDataMatrix(observationUnits, variables));
		table.setHeaderRow(buildHeaderRow());
		table.setObservationVariables(variables.stream().map(this::convertVariables).collect(Collectors.toList()));
		return table;
	}

	public List<ObservationUnit> findObservationUnits(@Valid ObservationUnitSearchRequest request, Metadata metadata)
		throws BrAPIServerException {
		Page<ObservationUnitEntity> page = findObservationUnitEntities(request, metadata);

		boolean includeObservations = request.isIncludeObservations() != null && request.isIncludeObservations();

		if(includeObservations) {
			log.debug("Fetching observations for OUs");
			for(ObservationUnitEntity entity : page) {
				log.trace("Fetching observations for OU: " + entity.getId().toString());
				entity.getObservations();
			}
		}

		log.debug("converting "+page.getSize()+" entities");
		List<ObservationUnit> observationUnits = page.map(observationUnitEntity -> this.convertFromEntity(observationUnitEntity, includeObservations)).getContent();
		log.debug("done converting entities");
		PagingUtility.calculateMetaData(metadata, page);

		return observationUnits;
	}

	public List<ObservationUnitEntity> findByIds(List<String> observationUnitIds) {
		var result = new ArrayList<ObservationUnitEntity>();

		if (!observationUnitIds.isEmpty()) {
			return observationUnitRepository.findByIds(observationUnitIds);
		}

		return result;
	}

	public Page<ObservationUnitEntity> findObservationUnitEntities(@Valid ObservationUnitSearchRequest request,
			Metadata metadata)
		throws BrAPIServerException {
		Pageable pageReq = PagingUtility.getPageRequest(metadata);
		SearchQueryBuilder<ObservationUnitEntity> searchQuery = new SearchQueryBuilder<ObservationUnitEntity>(
				ObservationUnitEntity.class);
		searchQuery.leftJoinFetch("germplasm", "germplasm")
				   .leftJoinFetch("*germplasm.pedigree", "pedigree")
				   .leftJoinFetch("cross", "cross")
				   .leftJoinFetch("position", "position")
				   .leftJoinFetch("*position.geoCoordinates", "geoCoordinates")
				   .leftJoinFetch("seedLot", "seedLot")
				   .leftJoinFetch("study", "study")
				   .leftJoinFetch("*study.experimentalDesign", "experimentalDesign")
				   .leftJoinFetch("*study.growthFacility", "growthFacility")
				   .leftJoinFetch("*study.lastUpdate", "lastUpdate")
				   .leftJoinFetch("*study.location", "studyLocation")
				   .leftJoinFetch("*study.trial", "studyTrial")
				   .leftJoinFetch("*studyTrial.program", "studyTrialProgram")
				   .leftJoinFetch("trial", "trial")
				   .leftJoinFetch("*trial.program", "trialProgram")
				   .leftJoinFetch("program", "program");

		if (request.getObservationVariableDbIds() != null || request.getObservationVariableNames() != null) {
			searchQuery = searchQuery.join("observationsUnits", "observation")
					.appendList(request.getObservationVariableDbIds(), "*observation.variable.id")
					.appendList(request.getObservationVariableNames(), "*observation.variable.name");
		}
		if (request.getSeasonDbIds() != null) {
			searchQuery = searchQuery.join("study.seasons", "season").appendList(request.getSeasonDbIds(),
					"*season.id");

		}

		if (request.getObservationLevels() != null) {

			searchQuery = searchQuery
					// TODO: This will likely need to be updated so the search works by program and with globally available level names
					.appendList(request.getObservationLevels().stream().filter(r -> r.getLevelName() != null)
							.map(r -> r.getLevelName()).collect(Collectors.toList()), "position.levelName")
					.appendList(request.getObservationLevels().stream().filter(r -> r.getLevelCode() != null)
							.map(ObservationUnitLevel::getLevelCode).collect(Collectors.toList()), "position.levelCode")
					.appendIntList(request.getObservationLevels().stream().filter(r -> r.getLevelOrder() != null)
							.map(ObservationUnitHierarchyLevel::getLevelOrder).collect(Collectors.toList()), "position.levelOrder");
		}
		if (request.getObservationLevelRelationships() != null) {
			searchQuery = searchQuery.join("position.observationLevelRelationships", "levelRelationship")
					// TODO: This will likely need to be updated so the search works by program and with globally available level names
					.appendList(
							request.getObservationLevelRelationships().stream().filter(r -> r.getLevelName() != null)
									.map(r -> r.getLevelName()).collect(Collectors.toList()),
							"*levelRelationship.levelName")
					.appendList(
							request.getObservationLevelRelationships().stream().filter(r -> r.getLevelCode() != null)
									.map(r -> r.getLevelCode()).collect(Collectors.toList()),
							"*levelRelationship.levelCode")
					.appendIntList(
							request.getObservationLevelRelationships().stream().filter(r -> r.getLevelOrder() != null)
									.map(r -> r.getLevelOrder()).collect(Collectors.toList()),
							"*levelRelationship.levelOrder")
					.appendList(
							request.getObservationLevelRelationships().stream().filter(r -> r.getObservationUnitDbId() != null)
									.map(r -> r.getObservationUnitDbId()).collect(Collectors.toList()),
							"*levelRelationship.observationUnit.id");
		}
		searchQuery = searchQuery.withExRefs(request.getExternalReferenceIDs(), request.getExternalReferenceSources())
				.appendList(request.getGermplasmDbIds(), "germplasm.id")
				.appendList(request.getGermplasmNames(), "germplasm.germplasmName")
				.appendList(request.getLocationDbIds(), "study.location.id")
				.appendList(request.getLocationNames(), "study.location.locationName")
				.appendList(request.getObservationUnitDbIds(), "id").appendList(request.getProgramDbIds(), "program.id")
				.appendList(request.getProgramNames(), "program.name").appendList(request.getStudyDbIds(), "study.id")
				.appendList(request.getStudyNames(), "study.studyName").appendList(request.getTrialDbIds(), "trial.id")
				.appendList(request.getTrialNames(), "trial.trailName");

		log.debug("Starting search");
		Page<ObservationUnitEntity> observationsUnits = observationUnitRepository.findAllBySearchPaginatingWithFetches(searchQuery, pageReq);

		List<UUID> ids = observationsUnits.map(BrAPIBaseEntity::getId).toList();
		log.debug("Search complete");

		if(!observationsUnits.isEmpty()) {
			observationUnitRepository.fetchXrefs(ids, observationsUnits, ObservationUnitEntity.class);
			fetchTreatments(ids, observationsUnits);
			fetchObsUnitLevelRelationships(ids, observationsUnits);
		}
		return observationsUnits;
	}

	private void fetchTreatments(List<UUID> ids, Page<ObservationUnitEntity> pagedOUs) {
		SearchQueryBuilder<ObservationUnitEntity> searchQuery = new SearchQueryBuilder<ObservationUnitEntity>(
				ObservationUnitEntity.class);
		searchQuery.leftJoinFetch("treatments", "treatments")
				   .appendIds(ids);

		List<ObservationUnitEntity> treatments = observationUnitRepository.findAllBySearch(searchQuery);

		Map<String, List<TreatmentEntity>> treatmentsByOu = new HashMap<>();
		treatments.forEach(ou -> treatmentsByOu.put(ou.getId().toString(), ou.getTreatments()));

		pagedOUs.forEach(ou -> ou.setTreatments(treatmentsByOu.get(ou.getId().toString())));
	}

	private void fetchObsUnitLevelRelationships(List<UUID> ids, Page<ObservationUnitEntity> page) {
		SearchQueryBuilder<ObservationUnitEntity> searchQuery = new SearchQueryBuilder<ObservationUnitEntity>(
				ObservationUnitEntity.class);
		searchQuery.leftJoinFetch("position", "position")
				   .leftJoinFetch("*position.observationLevelRelationships", "observationLevelRelationships")
				   .appendIds(ids);

		List<ObservationUnitEntity> positions = observationUnitRepository.findAllBySearch(searchQuery);

		Map<String, ObservationUnitPositionEntity> positionByOu = new HashMap<>();
		positions.forEach(ou -> positionByOu.put(ou.getId().toString(), ou.getPosition()));

		page.forEach(ou -> {
			if(ou.getPosition() != null) {
				ou.getPosition()
				  .setObservationLevelRelationships(positionByOu.get(ou.getId().toString())
																.getObservationLevelRelationships());
			}
		});
	}

	public ObservationUnit getObservationUnit(String observationUnitDbId) throws BrAPIServerException {
		return convertFromEntity(getObservationUnitEntity(observationUnitDbId, HttpStatus.NOT_FOUND));
	}

	public ObservationUnitEntity getObservationUnitEntity(String observationUnitDbId) throws BrAPIServerException {
		return getObservationUnitEntity(observationUnitDbId, HttpStatus.BAD_REQUEST);
	}

	public ObservationUnitEntity getObservationUnitEntity(String observationUnitDbId, HttpStatus errorStatus)
			throws BrAPIServerException {
		if(observationUnitDbId == null) {
			throw new BrAPIServerDbIdNotFoundException("observationUnit", "null", errorStatus);
		}
		ObservationUnitEntity observationUnit = null;
		Optional<ObservationUnitEntity> entityOpt = observationUnitRepository.findById(UUID.fromString(observationUnitDbId));
		if (entityOpt.isPresent()) {
			observationUnit = entityOpt.get();
		} else {
			throw new BrAPIServerDbIdNotFoundException("observationUnit", observationUnitDbId, errorStatus);
		}
		return observationUnit;
	}

	public List<ObservationUnit> saveObservationUnits(@Valid List<ObservationUnitNewRequest> requests)
			throws BrAPIServerException {
		var toSave = createEntitiesInBatch(requests);

		return observationUnitRepository.saveAll(toSave)
				.stream()
				.map(this::convertFromEntity)
				.collect(Collectors.toList());
	}

	public List<ObservationUnit> updateObservationUnits(@Valid Map<String, ObservationUnitNewRequest> requests)
			throws BrAPIServerException {
		List<ObservationUnit> savedObservationUnits = new ArrayList<>();

		for (Entry<String, ObservationUnitNewRequest> entry : requests.entrySet()) {
			ObservationUnit saved = updateObservationUnit(entry.getKey(), entry.getValue());
			savedObservationUnits.add(saved);
		}

		return savedObservationUnits;
	}

	public ObservationUnit updateObservationUnit(String observationUnitDbId, @Valid ObservationUnitNewRequest request)
			throws BrAPIServerException {
		ObservationUnitEntity entity = getObservationUnitEntity(observationUnitDbId, HttpStatus.NOT_FOUND);
		updateEntity(entity, request);
		ObservationUnitEntity savedEntity = observationUnitRepository.save(entity);

		return convertFromEntity(savedEntity);
	}

	public List<ObservationUnitHierarchyLevel> findObservationLevels(String studyDbId, String trialDbId,
			String programDbId, Metadata metadata)
		throws BrAPIServerException {

		List<ObservationUnitLevelNameEntity> foundObsLevelNameEntities = observationUnitLevelNameService.findObservationUnitLevelNames(programDbId);

		List<ObservationUnitLevel> levelNames = foundObsLevelNameEntities.stream()
				.map(levelEnum -> {
					ObservationUnitLevel rel = new ObservationUnitLevel();
					rel.setLevelName(levelEnum.getLevelName());
					return rel;
				}).collect(Collectors.toList());

		ObservationUnitSearchRequest levelsSearch = buildObservationUnitsSearchRequest(null, null, null, studyDbId,
				null, trialDbId, programDbId, null, null, null, null, null, null, null, null, null, false, null, null,
				null);

		levelsSearch.setObservationLevels(levelNames);
		List<ObservationUnit> units = new ArrayList<>();
		List<ObservationUnit> someunits = findObservationUnits(levelsSearch,
				new Metadata().pagination(new IndexPagination()));
		units.addAll(someunits);

		levelsSearch.setObservationLevelRelationships(levelNames.stream().map(lvl -> {
			ObservationUnitLevelRelationship rel = new ObservationUnitLevelRelationship();
			rel.setLevelCode(lvl.getLevelCode());
			rel.setLevelName(lvl.getLevelName());
			rel.setLevelOrder(lvl.getLevelOrder());
			return rel;
		}).collect(Collectors.toList()));
		levelsSearch.setObservationLevels(null);
		List<ObservationUnit> moreUnits = findObservationUnits(levelsSearch,
				new Metadata().pagination(new IndexPagination()));
		units.addAll(moreUnits);

		List<ObservationUnitHierarchyLevel> levels = units.stream()
				.filter(unit -> unit.getObservationUnitPosition() != null)
				.filter(unit -> unit.getObservationUnitPosition().getObservationLevelRelationships() != null)
				.filter(unit -> unit.getObservationUnitPosition().getObservationLevel() != null)
				.filter(unit -> unit.getObservationUnitPosition().getObservationLevel().getLevelName() != null)
				.map(unit -> {
					List<ObservationUnitLevel> list = new ArrayList<>();
					for (ObservationUnitLevel level : unit.getObservationUnitPosition()
							.getObservationLevelRelationships()) {
						list.add(level);
					}
					list.add(unit.getObservationUnitPosition().getObservationLevel());
					return list;
				}).flatMap(Collection::stream)
				.distinct()
				.sorted()
				.map(obsUnitLevel -> {
					ObservationUnitHierarchyLevel level = new ObservationUnitHierarchyLevel();
					level.setLevelName(obsUnitLevel.getLevelName());
					level.setLevelOrder(obsUnitLevel.getLevelOrder());
					return level;
				}).collect(Collectors.toList());

		return PagingUtility.paginateSimpleList(levels, metadata);
	}

	private ObservationUnit convertFromEntity(ObservationUnitEntity entity) {
		return convertFromEntity(entity, true);
	}

	private ObservationUnit convertFromEntity(ObservationUnitEntity entity, boolean convertObservations) {
		log.trace("converting ou: " + entity.getId().toString());
		ObservationUnit unit = new ObservationUnit();
		UpdateUtility.convertFromEntity(entity, unit);

		if (entity.getGermplasm() != null) {
			unit.setGermplasmDbId(entity.getGermplasm().getId().toString());
			unit.setGermplasmName(entity.getGermplasm().getGermplasmName());
		}
		if (entity.getCross() != null) {
			unit.setCrossDbId(entity.getCross().getId().toString());
			unit.setCrossName(entity.getCross().getName());
		}
		if (convertObservations && entity.getObservations() != null) {
			unit.setObservations(entity.getObservations().stream().map(this.observationService::convertFromEntity)
					.collect(Collectors.toList()));
		}
		unit.setObservationUnitDbId(entity.getId().toString());
		unit.setObservationUnitName(entity.getObservationUnitName());
		unit.setObservationUnitPosition(convertFromEntity(entity.getPosition(), entity.getId().toString()));
		unit.setObservationUnitPUI(entity.getObservationUnitPUI());
		if (entity.getSeedLot() != null) {
			unit.setSeedLotDbId(entity.getSeedLot().getId().toString());
			unit.setSeedLotName(entity.getSeedLot().getName());
		}
		if (entity.getTreatments() != null)
			unit.setTreatments(
					entity.getTreatments().stream().map(this::convertFromEntity).collect(Collectors.toList()));

		if (entity.getStudy() != null) {
			unit.setStudyDbId(entity.getStudy().getId().toString());
			unit.setStudyName(entity.getStudy().getStudyName());
			if (entity.getStudy().getLocation() != null) {
				unit.setLocationDbId(entity.getStudy().getLocation().getId().toString());
				unit.setLocationName(entity.getStudy().getLocation().getLocationName());
			}
			if (entity.getStudy().getTrial() != null) {
				unit.setTrialDbId(entity.getStudy().getTrial().getId().toString());
				unit.setTrialName(entity.getStudy().getTrial().getTrialName());
				if (entity.getStudy().getTrial().getProgram() != null) {
					unit.setProgramDbId(entity.getStudy().getTrial().getProgram().getId().toString());
					unit.setProgramName(entity.getStudy().getTrial().getProgram().getName());
				}
			}
		} 
		if (entity.getTrial() != null) {
			unit.setTrialDbId(entity.getTrial().getId().toString());
			unit.setTrialName(entity.getTrial().getTrialName());
			if (entity.getTrial().getProgram() != null) {
				unit.setProgramDbId(entity.getTrial().getProgram().getId().toString());
				unit.setProgramName(entity.getTrial().getProgram().getName());
			}
		} 
		if (entity.getProgram() != null) {
			unit.setProgramDbId(entity.getProgram().getId().toString());
			unit.setProgramName(entity.getProgram().getName());
		}

		return unit;

	}

	private ObservationUnitPosition convertFromEntity(ObservationUnitPositionEntity entity, String ouDbId) {
		ObservationUnitPosition position = null;
		if (entity != null) {
			position = new ObservationUnitPosition();
			position.setEntryType(entity.getEntryType());
			position.setGeoCoordinates(GeoJSONUtility.convertFromEntity(entity.getGeoCoordinates()));
			ObservationUnitLevel level = new ObservationUnitLevel();
			level.setLevelCode(entity.getLevelCode());
			level.setLevelName(entity.getLevelName().getLevelName());
			level.setLevelOrder(entity.getLevelName().getLevelOrder());
			position.setObservationLevel(level);
			if (entity.getObservationLevelRelationships() != null) {
				position.setObservationLevelRelationships(entity.getObservationLevelRelationships().stream()
						.map(rel -> this.convertFromEntity(rel, ouDbId)).collect(Collectors.toList()));
			}
			position.setPositionCoordinateX(entity.getPositionCoordinateX());
			position.setPositionCoordinateXType(entity.getPositionCoordinateXType());
			position.setPositionCoordinateY(entity.getPositionCoordinateY());
			position.setPositionCoordinateYType(entity.getPositionCoordinateYType());
		}
		return position;
	}

	private ObservationTreatment convertFromEntity(TreatmentEntity entity) {
		ObservationTreatment treatment = new ObservationTreatment();
		treatment.setFactor(entity.getFactor());
		treatment.setModality(entity.getModality());
		return treatment;
	}

	private ObservationUnitLevelRelationship convertFromEntity(ObservationUnitLevelRelationshipEntity entity, String ouDbId) {
		ObservationUnitLevelRelationship level = new ObservationUnitLevelRelationship();
		level.setLevelCode(entity.getLevelCode());
		level.setLevelName(entity.getLevelName().getLevelName());
		level.setLevelOrder(entity.getLevelName().getLevelOrder());
		if (ouDbId != null)
			level.setObservationUnitDbId(ouDbId);
		return level;
	}

	private ObservationUnitEntity updateEntity(ObservationUnitEntity entity, ObservationUnitNewRequest unit)
			throws BrAPIServerException {
		UpdateUtility.updateEntity(unit, entity);

		if (unit.getGermplasmDbId() != null) {
			GermplasmEntity germplasm = germplasmService.getGermplasmEntity(unit.getGermplasmDbId());
			entity.setGermplasm(germplasm);
		}
		if (unit.getCrossDbId() != null) {
			CrossEntity cross = crossService.getCrossEntity(unit.getCrossDbId(), false);
			entity.setCross(cross);
		}
		if (unit.getObservationUnitName() != null)
			entity.setObservationUnitName(unit.getObservationUnitName());
		if (unit.getObservationUnitPUI() != null)
			entity.setObservationUnitPUI(unit.getObservationUnitPUI());
		if (unit.getObservationUnitPosition() != null) {
			if (entity.getPosition() == null)
				entity.setPosition(new ObservationUnitPositionEntity());
			ObservationUnitPositionEntity position = entity.getPosition();
			updateEntity(position, unit.getObservationUnitPosition(), entity);
			position.setObservationUnit(entity);
			entity.setPosition(position);
		}
		if (unit.getSeedLotDbId() != null) {
			SeedLotEntity seedLot = seedLotService.getSeedLotEntity(unit.getSeedLotDbId());
			entity.setSeedLot(seedLot);
		}
		if (unit.getTreatments() != null)
			entity.setTreatments(unit.getTreatments().stream().map(t -> {
				TreatmentEntity e = new TreatmentEntity();
				e.setFactor(t.getFactor());
				e.setModality(t.getModality());
				e.setObservationUnit(entity);
				return e;
			}).collect(Collectors.toList()));

		if (unit.getStudyDbId() != null) {
			StudyEntity study = studyService.getStudyEntity(unit.getStudyDbId());
			entity.setStudy(study);
		} else if (unit.getTrialDbId() != null) {
			TrialEntity trial = trialService.getTrialEntity(unit.getTrialDbId());
			entity.setTrial(trial);
		} else if (unit.getProgramDbId() != null) {
			ProgramEntity program = programService.getProgramEntity(unit.getProgramDbId());
			entity.setProgram(program);
		}

		return entity;
	}

	private void updateEntity(ObservationUnitPositionEntity entity,
							  ObservationUnitPosition position,
							  ObservationUnitEntity ouEntity) throws BrAPIServerException {

		if (position.getEntryType() != null)
			entity.setEntryType(position.getEntryType());
		if (position.getGeoCoordinates() != null)
			entity.setGeoCoordinates(GeoJSONUtility.convertToEntity(position.getGeoCoordinates()));
		if (position.getObservationLevel() != null) {
			if (position.getObservationLevel().getLevelCode() != null)
				entity.setLevelCode(position.getObservationLevel().getLevelCode());
			if (position.getObservationLevel().getLevelName() != null) {
				var programDbId = Optional.ofNullable(ouEntity.getProgram())
						.map(p -> p.getId().toString())
						.orElse(null);
				var foundLevelName = observationUnitLevelNameService.retrieveAndVerifyObservationUnitLevelNames(programDbId, List.of(position.getObservationLevel().getLevelName()));
				entity.setLevelName(foundLevelName.get(position.getObservationLevel().getLevelName()));
			}
		}
		if (position.getObservationLevelRelationships() != null)
			updateOULevelRelationships(ouEntity, entity, position);
		if (position.getPositionCoordinateX() != null)
			entity.setPositionCoordinateX(position.getPositionCoordinateX());
		if (position.getPositionCoordinateXType() != null)
			entity.setPositionCoordinateXType(position.getPositionCoordinateXType());
		if (position.getPositionCoordinateY() != null)
			entity.setPositionCoordinateY(position.getPositionCoordinateY());
		if (position.getPositionCoordinateYType() != null)
			entity.setPositionCoordinateYType(position.getPositionCoordinateYType());

	}

	private void updateOULevelRelationships(ObservationUnitEntity ouEntity,
											ObservationUnitPositionEntity pEntity,
											ObservationUnitPosition position)
		throws BrAPIServerException {

		var submittedLevelNames = position.getObservationLevelRelationships().stream()
				.map(ObservationUnitHierarchyLevel::getLevelName)
				.toList();

		var programDbId = Optional.ofNullable(ouEntity.getProgram())
				.map(p -> p.getId().toString())
				.orElse(null);

		var foundOULevelNames = observationUnitLevelNameService.retrieveAndVerifyObservationUnitLevelNames(programDbId, submittedLevelNames);

		var relationshipEntities = new ArrayList<ObservationUnitLevelRelationshipEntity>();

		for (ObservationUnitLevelRelationship level : position.getObservationLevelRelationships()) {
			ObservationUnitLevelRelationshipEntity relationshipEntity = new ObservationUnitLevelRelationshipEntity();
			relationshipEntity.setLevelCode(level.getLevelCode());
			relationshipEntity.setLevelName(foundOULevelNames.get(level.getLevelName()));
			if (level.getObservationUnitDbId() != null) {
				ObservationUnitEntity parentEntity = getObservationUnitEntity(level.getObservationUnitDbId());
				relationshipEntity.setObservationUnit(parentEntity);
			}
			relationshipEntity.setPosition(pEntity);
			relationshipEntities.add(relationshipEntity);
		}

		pEntity.setObservationLevelRelationships(relationshipEntities);
	}

	private List<ObservationUnitEntity> createEntitiesInBatch(List<ObservationUnitNewRequest> obsUnits)
		throws BrAPIServerException {
		// Gather all IDs we want to look up in a bulk lookup.
		var germplasmIds = obsUnits.stream()
				.map(ObservationUnitNewRequest::getGermplasmDbId)
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		var crossIds = obsUnits.stream()
				.map(ObservationUnitNewRequest::getCrossDbId)
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		var seedLotIds = obsUnits.stream()
				.map(ObservationUnitNewRequest::getSeedLotDbId)
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		var studyIds = obsUnits.stream()
				.map(ObservationUnitNewRequest::getStudyDbId)
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		var trialIds = obsUnits.stream()
				.map(ObservationUnitNewRequest::getTrialDbId)
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		var programIds = obsUnits.stream()
				.map(ObservationUnitNewRequest::getProgramDbId)
				.filter(Objects::nonNull)
				.distinct()
				.toList();

		// Now lookup all the IDs in bulk, creating a Map of the ID to the entity so the entities are easily
		// retrievable by IDs in the bulk creating of entities later.
		var foundGermsById = germplasmService.findByIds(germplasmIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		var foundCrossesById = crossService.findByIds(crossIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		var foundSeedLotsById = seedLotService.findByIds(seedLotIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		var foundStudiesById = studyService.findByIds(studyIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		var foundTrialsById = trialService.findByIds(trialIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		var foundProgramsById = programService.findByIds(programIds)
				.stream()
				.collect(Collectors.toMap(BrAPIBaseEntity::getId, e -> e));

		List<ObservationUnitEntity> result = new ArrayList<>();

		for (ObservationUnitNewRequest obsUnit : obsUnits) {
			var entity = new ObservationUnitEntity();

			UpdateUtility.updateEntity(obsUnit, entity);

			if (obsUnit.getGermplasmDbId() != null) {
				entity.setGermplasm(foundGermsById.get(UUID.fromString(obsUnit.getGermplasmDbId())));
			}
			if (obsUnit.getCrossDbId() != null) {
				entity.setCross(foundCrossesById.get(UUID.fromString(obsUnit.getCrossDbId())));
			}
			if (obsUnit.getObservationUnitName() != null)
				entity.setObservationUnitName(obsUnit.getObservationUnitName());
			if (obsUnit.getObservationUnitPUI() != null)
				entity.setObservationUnitPUI(obsUnit.getObservationUnitPUI());
			if (obsUnit.getSeedLotDbId() != null) {
				entity.setSeedLot(foundSeedLotsById.get(UUID.fromString(obsUnit.getSeedLotDbId())));
			}
			if (obsUnit.getTreatments() != null)
				entity.setTreatments(obsUnit.getTreatments().stream().map(t -> {
					TreatmentEntity e = new TreatmentEntity();
					e.setFactor(t.getFactor());
					e.setModality(t.getModality());
					e.setObservationUnit(entity);
					return e;
				}).collect(Collectors.toList()));

			if (obsUnit.getStudyDbId() != null) {
				entity.setStudy(foundStudiesById.get(UUID.fromString(obsUnit.getStudyDbId())));
			} else if (obsUnit.getTrialDbId() != null) {
				entity.setTrial(foundTrialsById.get(UUID.fromString(obsUnit.getTrialDbId())));
			} else if (obsUnit.getProgramDbId() != null) {
				entity.setProgram(foundProgramsById.get(UUID.fromString(obsUnit.getProgramDbId())));
			}

			// TODO: This will have to be fixed to batchable
			if (obsUnit.getObservationUnitPosition() != null) {
				if (entity.getPosition() == null)
					entity.setPosition(new ObservationUnitPositionEntity());
				ObservationUnitPositionEntity position = entity.getPosition();
				updateEntity(position, obsUnit.getObservationUnitPosition(), entity);
				position.setObservationUnit(entity);
				entity.setPosition(position);
			}

			result.add(entity);
		}

		return result;
	}

	public Map<UUID, ObservationUnitGermplasmData> fetchObservationUnitGermplasmData(List<UUID> ouIds) {

		if (ouIds.isEmpty()) {
			return new HashMap<>();
		}

		var databaseResults = observationUnitRepository.fetchGermplasmDataForOUs(ouIds);

		return databaseResults.stream()
				.collect((Collectors.toMap(
						rs -> (UUID) rs[0],
						rs -> new ObservationUnitGermplasmData(rs[1].toString(), rs[2].toString()))
				));
	}

	private List<List<String>> buildDataMatrix(Page<ObservationUnitEntity> observationUnits,
			List<ObservationVariable> variables) {
		List<List<String>> data = new ArrayList<>();
		for (ObservationUnitEntity obsUnit : observationUnits) {
			List<String> row = new ArrayList<>();

			if (obsUnit.getStudy() != null) {
				StudyEntity study = obsUnit.getStudy();
				if (study.getSeasons() != null && !study.getSeasons().isEmpty()) {
					row.add(printIfNotNull(study.getSeasons().get(0).getYear())); // YEAR
				} else {
					row.add(""); // YEAR
				}

				row.add(printIfNotNull(study.getId().toString())); // STUDYDBID
				row.add(printIfNotNull(study.getStudyName())); // STUDYNAME
//
//				if (study.getLocation() != null) {
//					row.add(printIfNotNull(study.getLocation().getId().toString())); // LOCATIONDBID
//					row.add(printIfNotNull(study.getLocation().getLocationName())); // LOCATIONNAME
//				} else {
//					row.add(""); // LOCATIONDBID
//					row.add(""); // LOCATIONNAME
//				}

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

			for (ObservationVariable var : variables) {
				Optional<ObservationEntity> obsOption = obsUnit.getObservations().stream().filter((obs) -> {
					return obs.getObservationVariable().getId().toString() == var.getObservationVariableDbId();
				}).findFirst();
				if (obsOption.isPresent()) {
					row.add(obsOption.get().getValue());
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

		return headers;
	}

	public String getObservationUnitTableText(ObservationUnitTable table, String sep) {
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

	private ObservationTableObservationVariables convertVariables(ObservationVariable variable) {
		ObservationTableObservationVariables header = new ObservationTableObservationVariables();
		header.setObservationVariableDbId(variable.getObservationVariableDbId());
		header.setObservationVariableName(variable.getObservationVariableName());
		return header;
	}
}
