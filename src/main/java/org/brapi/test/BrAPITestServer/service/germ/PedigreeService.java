package org.brapi.test.BrAPITestServer.service.germ;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import io.swagger.model.IndexPagination;
import org.apache.commons.lang3.tuple.Pair;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerDbIdNotFoundException;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.brapi.test.BrAPITestServer.model.entity.germ.CrossingProjectEntity;
import org.brapi.test.BrAPITestServer.model.entity.germ.GermplasmEntity;
import org.brapi.test.BrAPITestServer.model.entity.germ.PedigreeEdgeEntity;
import org.brapi.test.BrAPITestServer.model.entity.germ.PedigreeNodeEntity;
import org.brapi.test.BrAPITestServer.repository.germ.PedigreeEdgeRepository;
import org.brapi.test.BrAPITestServer.repository.germ.PedigreeRepository;
import org.brapi.test.BrAPITestServer.service.PagingUtility;
import org.brapi.test.BrAPITestServer.service.SearchQueryBuilder;
import org.brapi.test.BrAPITestServer.service.UpdateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.swagger.model.Metadata;
import io.swagger.model.germ.Germplasm;
import io.swagger.model.germ.ParentType;
import io.swagger.model.germ.PedigreeNode;
import io.swagger.model.germ.PedigreeNodeParents;
import io.swagger.model.germ.PedigreeNodeSiblings;
import io.swagger.model.germ.PedigreeSearchRequest;
import io.swagger.model.germ.ProgenyNode;
import io.swagger.model.germ.ProgenyNodeProgeny;

@Service
public class PedigreeService {
	private static final Logger log = LoggerFactory.getLogger(PedigreeService.class);

	private final PedigreeRepository pedigreeRepository;
	private final PedigreeEdgeRepository pedigreeEdgeRepository;
	private final CrossingProjectService crossingProjectService;
	private final GermplasmService germplasmService;

	@Autowired
	public PedigreeService(PedigreeRepository pedigreeRepository, PedigreeEdgeRepository pedigreeEdgeRepository,
			CrossingProjectService crossingProjectService, GermplasmService germplasmService) {
		this.pedigreeRepository = pedigreeRepository;
		this.pedigreeEdgeRepository = pedigreeEdgeRepository;
		this.crossingProjectService = crossingProjectService;
		this.germplasmService = germplasmService;
	}

	public List<PedigreeNode> findPedigree(String germplasmPUI, String germplasmDbId, String germplasmName,
			String accessionNumber, String collection, String binomialName, String genus, String species,
			String trialDbId, String studyDbId, String synonym, String commonCropName, String programDbId,
			String externalReferenceId, String externalReferenceSource, Boolean includeParents, Boolean includeSiblings,
			Boolean includeProgeny, Boolean includeFullTree, Integer pedigreeDepth, Integer progenyDepth,
			Metadata metadata)
		throws BrAPIServerException {

		PedigreeSearchRequest request = new PedigreeSearchRequest();
		if (germplasmPUI != null)
			request.addGermplasmPUIsItem(germplasmPUI);
		if (germplasmDbId != null)
			request.addGermplasmDbIdsItem(germplasmDbId);
		if (germplasmName != null)
			request.addGermplasmNamesItem(germplasmName);
		if (accessionNumber != null)
			request.addAccessionNumbersItem(accessionNumber);
		if (collection != null)
			request.addCollectionsItem(collection);
		if (binomialName != null)
			request.addBinomialNamesItem(binomialName);
		if (genus != null)
			request.addGenusItem(genus);
		if (species != null)
			request.addSpeciesItem(species);
		if (trialDbId != null)
			request.addTrialDbIdsItem(trialDbId);
		if (studyDbId != null)
			request.addStudyDbIdsItem(studyDbId);
		if (synonym != null)
			request.addSynonymsItem(synonym);
		if (commonCropName != null)
			request.addCommonCropNamesItem(commonCropName);
		if (programDbId != null)
			request.addProgramDbIdsItem(programDbId);
		if (includeParents != null)
			request.setIncludeParents(includeParents);
		if (includeProgeny != null)
			request.setIncludeProgeny(includeProgeny);
		if (includeSiblings != null)
			request.setIncludeSiblings(includeSiblings);
		if (includeFullTree != null)
			request.setIncludeFullTree(includeFullTree);
		if (pedigreeDepth != null)
			request.setPedigreeDepth(pedigreeDepth);
		if (progenyDepth != null)
			request.setProgenyDepth(progenyDepth);

		request.addExternalReferenceItem(externalReferenceId, null, externalReferenceSource);

		return findPedigree(request, metadata);
	}

	public List<PedigreeNode> findPedigree(PedigreeSearchRequest request, Metadata metadata)
		throws BrAPIServerException {
		List<PedigreeNodeEntity> page = findPedigreeEntities(request, metadata);
		List<PedigreeNode> pedigreeNodes = convertFromEntities(page, request);
		return pedigreeNodes;
	}

	public List<PedigreeNodeEntity> findPedigreeEntities(PedigreeSearchRequest request, Metadata metadata)
		throws BrAPIServerException {
		Pageable pageReq = PagingUtility.getPageRequest(metadata);
		SearchQueryBuilder<PedigreeNodeEntity> searchQuery = new SearchQueryBuilder<PedigreeNodeEntity>(
				PedigreeNodeEntity.class);

		if (request.getProgramDbIds() != null || request.getProgramNames() != null || request.getTrialDbIds() != null
				|| request.getTrialNames() != null || request.getStudyDbIds() != null
				|| request.getStudyNames() != null) {
			searchQuery = searchQuery.join("germplasm.observationUnits", "obsunit")
					.appendList(request.getProgramDbIds(), "*obsunit.program.id")
					.appendList(request.getProgramNames(), "*obsunit.program.name")
					.appendList(request.getTrialDbIds(), "*obsunit.trial.id")
					.appendList(request.getTrialNames(), "*obsunit.trial.name")
					.appendList(request.getStudyDbIds(), "*obsunit.study.id")
					.appendList(request.getStudyNames(), "*obsunit.study.studyName");
		}
		if (request.getSynonyms() != null) {
			searchQuery = searchQuery.join("germplasm.synonyms", "synonym").appendList(request.getSynonyms(),
					"*synonym.synonym");
		}
		if (request.getInstituteCodes() != null) {
			searchQuery = searchQuery.join("germplasm.institutes", "institute").appendList(request.getSynonyms(),
					"*institute.instituteCode");
		}

		searchQuery.withExRefs(request.getExternalReferenceIDs(), request.getExternalReferenceSources())
				.appendList(request.getAccessionNumbers(), "germplasm.accessionNumber")
				.appendList(request.getCollections(), "germplasm.collection")
				.appendList(request.getCommonCropNames(), "germplasm.crop.cropName")
				.appendList(request.getGermplasmDbIds(), "germplasm.id")
				.appendList(request.getGermplasmNames(), "germplasm.germplasmName")
				.appendList(request.getGermplasmPUIs(), "germplasm.germplasmPUI")
				.appendList(request.getGenus(), "germplasm.genus").appendList(request.getSpecies(), "germplasm.species")
				.appendNamesList(request.getBinomialNames(), "germplasm.genus", "germplasm.genus", "germplasm.species")
				.appendList(request.getFamilyCodes(), "familyCode");

		Page<PedigreeNodeEntity> page = pedigreeRepository.findAllBySearchAndPaginate(searchQuery, pageReq);

		List<PedigreeNodeEntity> filteredNodes = filterGenerations(request, page.getContent());

		filteredNodes = PagingUtility.paginateSimpleList(filteredNodes, metadata);

		return filteredNodes;
	}

	public Optional<PedigreeNodeEntity> getPedigreeNode(String germplasmDbId) {
		Optional<PedigreeNodeEntity> node = Optional.empty();
		List<PedigreeNodeEntity> nodeList = pedigreeRepository.findByGermplasm_Id(germplasmDbId);
		if (nodeList.size() == 1) {
			node = Optional.of(nodeList.get(0));
		} else if (nodeList.size() > 1) {
			log.error("multiple pedigree nodes found for a single germplasm");
		}
		return node;
	}

	public List<PedigreeNodeEntity> getPedigreeNodes(List<String> germplasmDbIds) {
		List<PedigreeNodeEntity> nodes = new ArrayList<>();

		// TODO: Might have to make a custom query for this that fetches the germ eagerly, bc need to compare the germIds.  Have to see if this is a significant performance hit.
		List<PedigreeNodeEntity> dbNodeList = pedigreeRepository.findByGermplasm_IdIn(germplasmDbIds);

		Map<String, List<PedigreeNodeEntity>> nodesGroupedByGerm = dbNodeList.stream().collect(Collectors.groupingBy(pn -> pn.getGermplasm().getId()));

		nodesGroupedByGerm.forEach((germId, nodesByGerm) -> {
			if (nodesByGerm.size() > 1) {
				log.error("multiple pedigree nodes found for a single germplasm");
			}

			Optional<PedigreeNodeEntity> node = nodesByGerm.stream().findFirst();

			node.ifPresent(nodes::add);
		});

		return nodes;
	}

	public List<PedigreeNodeEntity> findOrCreatePedigreeNodesFromGermplasmIds(List<String> germplasmDbIds) throws BrAPIServerException {

        List<PedigreeNodeEntity> dbNodes = getPedigreeNodes(germplasmDbIds);

        List<PedigreeNodeEntity> resultingNodes = new ArrayList<>(dbNodes);

		// Find out which germIds were not found in the DB. Use a set for improved performance on the contains check.
		// TODO: Check if the germEntity is already populated by getPedigreeNodes, and if this block results in more DB transactions.
		Set<String> germIdsOfFoundNodes = dbNodes.stream()
				.map(PedigreeNodeEntity::getGermplasm)
				.map(BrAPIBaseEntity::getId)
				.collect(Collectors.toSet());

		List<String> germIdsWithNoPedigreeRecord = germplasmDbIds.stream()
				.filter(gId -> !germIdsOfFoundNodes.contains(gId))
				.collect(Collectors.toList());

		// Now see if there are germplasm records that exist from the list created above that have a pedigree associated.
		// If they do have a pedigree associated, add it to the result list.
		// If not, create a Pedigree with that Germplasm record.
		// TODO: Might need this to fetch the pedigree records on this query, otherwise a lazy load occurs
		List<GermplasmEntity>  germplasms = new ArrayList<>();

		if (!germIdsWithNoPedigreeRecord.isEmpty()) {
			germplasms = germplasmService.findByIds(germIdsWithNoPedigreeRecord);
		}

		List<PedigreeNodeEntity> nodesToCreate = new ArrayList<>();

		for (GermplasmEntity germplasm : germplasms) {
			// This is a lazy load
			if (germplasm.getPedigree() != null) {
				resultingNodes.add(germplasm.getPedigree());
			} else {
				// No pedigree exists for this germplasm.  Create one and add the germplasm to it.
				PedigreeNodeEntity newNode = new PedigreeNodeEntity();
				newNode.setGermplasm(germplasm);
				nodesToCreate.add(newNode);
			}
		}

		// If any new nodes were made, save them and add them to the result list.
		if (!nodesToCreate.isEmpty()) {
			resultingNodes.addAll(pedigreeRepository.saveAll(nodesToCreate));
		}

		return resultingNodes;
	}

	public PedigreeNode getGermplasmPedigree(String germplasmDbId, Boolean includeSiblings)
			throws BrAPIServerException {
		if (includeSiblings == null)
			includeSiblings = false;

		Optional<PedigreeNodeEntity> pedigreeEntityOpt = getPedigreeNode(germplasmDbId);
		PedigreeNode result = null;
		if (pedigreeEntityOpt.isPresent()) {
			PedigreeSearchRequest psr = new PedigreeSearchRequest();
			psr.setIncludeParents(true);
			psr.setIncludeProgeny(false);
			psr.includeSiblings(includeSiblings);
			result = convertFromEntity(pedigreeEntityOpt.get(), psr);
		}
		return result;
	}

	public ProgenyNode getGermplasmProgeny(String germplasmDbId) throws BrAPIServerException {
		Optional<PedigreeNodeEntity> pedigreeEntityOpt = getPedigreeNode(germplasmDbId);

		ProgenyNode result = null;
		if (pedigreeEntityOpt.isPresent()) {
			PedigreeNodeEntity pedigreeEntity = pedigreeEntityOpt.get();
			result = new ProgenyNode();
			result.setProgeny(new ArrayList<>());
			result.setGermplasmDbId(pedigreeEntity.getGermplasm().getId());
			result.setGermplasmName(pedigreeEntity.getGermplasm().getGermplasmName());

			for (PedigreeNodeEntity progenyNode : pedigreeEntity.getProgenyNodes()) {
				ProgenyNodeProgeny progeny = new ProgenyNodeProgeny();
				progeny.setGermplasmName(progenyNode.getGermplasm().getGermplasmName());
				progeny.setGermplasmDbId(progenyNode.getGermplasm().getId());
				if (progenyNode.getParentEdges() != null && !progenyNode.getParentEdges().isEmpty()) {
					progeny.setParentType(progenyNode.getParentEdges().get(0).getParentType());
				}
				result.getProgeny().add(progeny);
			}

		}

		return result;
	}

	public List<PedigreeNode> savePedigreeNodes(List<PedigreeNode> request)
		throws BrAPIServerException {
		return savePedigreeNodes(request, true);
	}

	public List<PedigreeNode> savePedigreeNodes(List<PedigreeNode> request,
												boolean returnValues) throws BrAPIServerException {
		Map<String, PedigreeNodeEntity> nodesByGermplasm = getExistingPedigreeNodes(
				request.stream().map(PedigreeNode::getGermplasmDbId).collect(Collectors.toList()));

		if (!nodesByGermplasm.isEmpty()) {
			String errorMsg = "The following germplasmDbIds already have existing pedigree data. Please use PUT /pedigree to update these germplasm. \n"
					+ nodesByGermplasm.keySet();
			throw new BrAPIServerException(HttpStatus.BAD_REQUEST, errorMsg);
		}

		// TODO: Batch this
		List<PedigreeNodeEntity> newEntities = createEntitiesInBatch(request);
		// save all the new nodes without edges
		//TODO: Fix to save nodes and edges at same time
		pedigreeRepository.saveAll(newEntities);

		Map<String, PedigreeNode> updateRequest = new HashMap<>();
		for (PedigreeNode newNode : request) {
			updateRequest.put(newNode.getGermplasmDbId(), newNode);
		}
		// update the new nodes with requested edges
		if (returnValues) {
			return updatePedigreeNodes(updateRequest);
		} else {
			updatePedigreeNodes(updateRequest, false);
			return Collections.emptyList();
		}
	}

	public List<PedigreeNode> updatePedigreeNodes(Map<String, PedigreeNode> request)
		throws BrAPIServerException {
		return updatePedigreeNodes(request, true);
	}

	public List<PedigreeNode> updatePedigreeNodes(Map<String, PedigreeNode> request,
												  boolean returnValues) throws BrAPIServerException {
		Map<String, PedigreeNodeEntity> nodesByGermplasm = getExistingPedigreeNodes(new ArrayList<>(request.keySet()));

		Map<String, Pair<PedigreeNodeEntity, PedigreeNode>> entityDtoPairsByGermplasmId = new HashMap<>();

		for (Entry<String, PedigreeNode> entry : request.entrySet()) {
			String germId = entry.getKey();
			PedigreeNodeEntity entity = nodesByGermplasm.get(germId);

			if (entity != null) {
				entityDtoPairsByGermplasmId.put(germId, Pair.of(entity, entry.getValue()));
			} else {
				throw new BrAPIServerDbIdNotFoundException("germplasm", entry.getKey(), HttpStatus.BAD_REQUEST);
			}
		}

		// First, update the basic properties of the nodes in batch.
		updateEntitiesWithEdgesInBatch(entityDtoPairsByGermplasmId);

		if (returnValues) {
			List<PedigreeNodeEntity> savedEntities = pedigreeRepository.saveAll(entityDtoPairsByGermplasmId.values().stream().map(Pair::getLeft).collect(Collectors.toList()));
			List<PedigreeNode> saved = convertFromEntities(savedEntities,
					new PedigreeSearchRequest().includeParents(true).includeProgeny(true).includeSiblings(true));
			return saved;
		} else {
			// If no values are required, simply return an empty list.
			pedigreeRepository.saveAll(entityDtoPairsByGermplasmId.values().stream().map(Pair::getLeft).collect(Collectors.toList()));
			return Collections.emptyList();
		}
	}

	public void updateGermplasmPedigree(List<Germplasm> data) throws BrAPIServerException {
		List<PedigreeNode> createPedigreeNodes = new ArrayList<>();
		Map<String, PedigreeNode> updatePedigreeNodes = new HashMap<>();

		// TODO: This always returns empty for the germplasm post path, since this method is always passed a newly created germplasm record.
		Map<String, PedigreeNodeEntity> nodesByGermplasm = getExistingPedigreeNodes(
				data.stream().map(p -> p.getGermplasmDbId()).collect(Collectors.toList()));

		if (data.stream().noneMatch(g -> g.getPedigree() != null) || !nodesByGermplasm.isEmpty()) {
			for (Germplasm germplasm : data) {
				if (nodesByGermplasm.containsKey(germplasm.getGermplasmDbId())) {
					updatePedigreeNodes.put(germplasm.getGermplasmDbId(), convertFromGermplasmToPedigree(germplasm));
				} else {
					createPedigreeNodes.add(convertFromGermplasmToPedigree(germplasm));
				}
			}

			if (!createPedigreeNodes.isEmpty()) {
				savePedigreeNodes(createPedigreeNodes, false);
			}

			if (!updatePedigreeNodes.isEmpty()) {
				updatePedigreeNodes(updatePedigreeNodes, false);
			}
		} else {
			savePedigreeNodes(convertFromGermplasmToPedigreeBatchUsingNames(data), false);
		}

	}

	private Map<String, PedigreeNodeEntity> getExistingPedigreeNodes(List<String> germplasmDbIds)
		throws BrAPIServerException {
		Map<String, PedigreeNodeEntity> nodesByGermplasm = new HashMap<>();

		if (null != germplasmDbIds && !germplasmDbIds.isEmpty()) {
			PedigreeSearchRequest searchReq = new PedigreeSearchRequest();
			searchReq.setGermplasmDbIds(germplasmDbIds);
			searchReq.setIncludeParents(false);
			searchReq.setIncludeProgeny(false);
			searchReq.setIncludeSiblings(false);
			searchReq.setPedigreeDepth(0);
			searchReq.setProgenyDepth(0);

			List<PedigreeNodeEntity> nodeEntities = findPedigreeEntities(searchReq, null);

			for (PedigreeNodeEntity nodeEntity : nodeEntities) {
				// TODO: nodeEntity.getGermplasm() causes a lazy load
				if (nodeEntity.getGermplasm() != null && nodeEntity.getGermplasm().getId() != null) {
					nodesByGermplasm.put(nodeEntity.getGermplasm().getId(), nodeEntity);
				}
			}
		}

		return nodesByGermplasm;
	}

	private List<PedigreeNodeEntity> filterGenerations(PedigreeSearchRequest request,
			List<PedigreeNodeEntity> baseNodes) {
		int pedigreeDepth = request.getPedigreeDepth();
		int progenyDepth = request.getProgenyDepth();

		if (request.isIncludeFullTree()) {
			pedigreeDepth = Integer.MAX_VALUE;
			progenyDepth = Integer.MAX_VALUE;
		}

		// TODO: Should the pedigree node records written to the db be different? Converting to a hashset keeps
		// an entry for records with null germplasm which is not desired in output. Filtering out here for now.
		List<PedigreeNodeEntity> filteredBaseNodes = baseNodes.stream()
				.filter(node -> node.getGermplasm() != null)
				.collect(Collectors.toList());

		Set<PedigreeNodeEntity> baseNodesSet = new HashSet<>(filteredBaseNodes);
		Set<PedigreeNodeEntity> pedigreeTree = new HashSet<>(filteredBaseNodes);

		getGenerationsRecursively(baseNodesSet, pedigreeDepth, true, pedigreeTree);
		getGenerationsRecursively(baseNodesSet, progenyDepth, false, pedigreeTree);

		return new ArrayList<>(pedigreeTree);
	}

	private void getGenerationsRecursively(final Set<PedigreeNodeEntity> baseNodes, final int generations,
			final boolean traverseUp, final Set<PedigreeNodeEntity> pedigreeTree) {

		if (generations > 0) {
			Set<PedigreeNodeEntity> newGeneration = new HashSet<>();
			for (PedigreeNodeEntity node : baseNodes) {
				if (traverseUp) {
					newGeneration.addAll(node.getParentNodes());
				} else {
					newGeneration.addAll(node.getProgenyNodes());
				}
			}
			int treeSize = pedigreeTree.size();
			pedigreeTree.addAll(newGeneration);

			if (treeSize == pedigreeTree.size()) {
				// tree has not grown, boundary of the tree or loop has been reached
				return;
			}
			getGenerationsRecursively(newGeneration, generations - 1, traverseUp, pedigreeTree);
		}
	}

	private List<PedigreeNode> convertFromEntities(List<PedigreeNodeEntity> entities, PedigreeSearchRequest request) {
		List<PedigreeNode> convertedNodes = new ArrayList<>();
		for (PedigreeNodeEntity entity : entities) {
			convertedNodes.add(convertFromEntity(entity, request));
		}
		return convertedNodes;
	}

	private PedigreeNode convertFromEntity(PedigreeNodeEntity entity, PedigreeSearchRequest request) {
		PedigreeNode node = new PedigreeNode();
		if (entity != null) {
			UpdateUtility.convertFromEntity(entity, node);
			if (entity.getGermplasm() != null) {
				node.setDefaultDisplayName(entity.getGermplasm().getDefaultDisplayName());
				node.setGermplasmDbId(entity.getGermplasm().getId());
				node.setGermplasmName(entity.getGermplasm().getGermplasmName());
				node.setGermplasmPUI(entity.getGermplasm().getGermplasmPUI());
				if (entity.getGermplasm().getBreedingMethod() != null) {
					node.setBreedingMethodDbId(entity.getGermplasm().getBreedingMethod().getId());
					node.setBreedingMethodName(entity.getGermplasm().getBreedingMethod().getName());
				}
			}
			if (entity.getCrossingProject() != null) {
				node.setCrossingProjectDbId(entity.getCrossingProject().getId());
			}
			node.setCrossingYear(entity.getCrossingYear());
			node.setFamilyCode(entity.getFamilyCode());
			node.setPedigreeString(getPedigreeString(entity));
			if (entity.getParentEdges() != null && request.isIncludeParents()) {
				node.setParents(entity.getParentEdges().stream().map(edge -> {
					PedigreeNodeParents parent = new PedigreeNodeParents();
					parent.setGermplasmDbId(edge.getConnectedNode().getGermplasm().getId());
					parent.setGermplasmName(edge.getConnectedNode().getGermplasm().getGermplasmName());
					parent.setParentType(edge.getParentType());
					return parent;
				}).collect(Collectors.toList()));
			}
			if (entity.getProgenyEdges() != null && request.isIncludeProgeny()) {
				node.setProgeny(entity.getProgenyEdges().stream().map(edge -> {
					PedigreeNodeParents progeny = new PedigreeNodeParents();
					progeny.setGermplasmDbId(edge.getConnectedNode().getGermplasm().getId());
					progeny.setGermplasmName(edge.getConnectedNode().getGermplasm().getGermplasmName());
					progeny.setParentType(edge.getParentType());
					return progeny;
				}).collect(Collectors.toList()));
			}
			if (request.isIncludeSiblings()) {
				List<PedigreeNodeEntity> siblingEntities = pedigreeRepository.findPedigreeSiblings(entity);
				node.setSiblings(siblingEntities.stream().map(sibNode -> {
					PedigreeNodeSiblings progeny = new PedigreeNodeSiblings();
					progeny.setGermplasmDbId(sibNode.getGermplasm().getId());
					progeny.setGermplasmName(sibNode.getGermplasm().getGermplasmName());
					return progeny;
				}).collect(Collectors.toList()));
			}
		}
		return node;
	}

	static public String getPedigreeString(PedigreeNodeEntity entity) {
		if (entity.getPedigreeString() == null || entity.getPedigreeString().isEmpty()) {
			String pedStr = "";
			if (entity.getParentEdges() != null && !entity.getParentEdges().isEmpty()) {
				Optional<PedigreeNodeEntity> mother = entity.getParentEdges().stream().filter(parentEdge -> {
					return ParentType.FEMALE == parentEdge.getParentType();
				}).map(PedigreeEdgeEntity::getConnectedNode).findFirst();
				Optional<PedigreeNodeEntity> father = entity.getParentEdges().stream().filter(parentEdge -> {
					return ParentType.MALE == parentEdge.getParentType();
				}).map(PedigreeEdgeEntity::getConnectedNode).findFirst();

				if (mother.isPresent()) {
					pedStr += mother.get().getGermplasm().getGermplasmName() + "/";
				} else {
					pedStr += "Unknown/";
				}

				if (father.isPresent()) {
					pedStr += father.get().getGermplasm().getGermplasmName();
				} else {
					pedStr += "Unknown";
				}
			}
			return pedStr;
		} else {
			return entity.getPedigreeString();
		}
	}

	private void updateEntity(PedigreeNodeEntity entity, PedigreeNode node) throws BrAPIServerException {
		if (node.getGermplasmDbId() != null && entity.getGermplasm() == null) {
			GermplasmEntity germplasm = germplasmService.getGermplasmEntity(node.getGermplasmDbId());
			entity.setGermplasm(germplasm);
		}

		UpdateUtility.updateEntity(node, entity);

		if (node.getCrossingYear() != null)
			entity.setCrossingYear(node.getCrossingYear());
		if (node.getFamilyCode() != null)
			entity.setFamilyCode(node.getFamilyCode());
		if (node.getPedigreeString() != null)
			entity.setPedigreeString(node.getPedigreeString());

		if (node.getCrossingProjectDbId() != null) {
			CrossingProjectEntity crossingProject = crossingProjectService
					.getCrossingProjectEntity(node.getCrossingProjectDbId());
			entity.setCrossingProject(crossingProject);
		}
	}



	// This method should be used in use cases where there are no existing node entities representing the list being passed through.
	private List<PedigreeNodeEntity> createEntitiesInBatch(List<PedigreeNode> nodes)
		throws BrAPIServerException {
		List<String> germIds = nodes.stream().map(PedigreeNode::getGermplasmDbId).collect(Collectors.toList());
		List<String> crossingProjIds = nodes.stream().map(PedigreeNode::getCrossingProjectDbId).collect(Collectors.toList());

		List<GermplasmEntity> germs = germplasmService.findByIds(germIds);
		List<CrossingProjectEntity> crossingProjs = crossingProjectService.findCrossingProjectsByIds(crossingProjIds);

		List<PedigreeNodeEntity> result = new ArrayList<PedigreeNodeEntity>();
		for (PedigreeNode node : nodes) {
			PedigreeNodeEntity entity = new PedigreeNodeEntity();

			if (node.getGermplasmDbId() != null) {
				String germId = node.getGermplasmDbId();
				Optional<GermplasmEntity> germEntity = germs.stream().filter(ge -> ge.getId().equals(germId)).findFirst();
				germEntity.ifPresent(entity::setGermplasm);
			}

			UpdateUtility.updateEntity(node, entity);

			if (node.getCrossingYear() != null)
				entity.setCrossingYear(node.getCrossingYear());
			if (node.getFamilyCode() != null)
				entity.setFamilyCode(node.getFamilyCode());
			if (node.getPedigreeString() != null)
				entity.setPedigreeString(node.getPedigreeString());

			if (node.getCrossingProjectDbId() != null) {
				String cpId = node.getCrossingProjectDbId();
				Optional<CrossingProjectEntity> crossingProjectEntity = crossingProjs.stream().filter(cp -> cp.getId().equals(cpId)).findFirst();
                crossingProjectEntity.ifPresent(entity::setCrossingProject);
			}

			result.add(entity);
		}

		return result;
	}

	// This method should be used in use cases where there are existing node entities that may have edges.
	private void updateEntitiesWithEdgesInBatch(Map<String, Pair<PedigreeNodeEntity, PedigreeNode>> entityDtoPairsByGermId)
			throws BrAPIServerException {
		List<String> germIds = new ArrayList<>(entityDtoPairsByGermId.keySet());
		List<String> crossingProjIds = entityDtoPairsByGermId.values()
				.stream()
				.map(nodePair -> nodePair.getRight().getCrossingProjectDbId())
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		List<String> germIdsWithParentNodes = entityDtoPairsByGermId.entrySet()
				.stream()
				.filter(entry -> entry.getValue().getRight().getParents() != null)
				.map(Entry::getKey)
				.collect(Collectors.toList());

		List<String> germIdsWithProgenyNodes = entityDtoPairsByGermId.entrySet()
				.stream()
				.filter(entry -> entry.getValue().getRight().getProgeny() != null)
				.map(Entry::getKey)
				.collect(Collectors.toList());

		List<GermplasmEntity> germs = new ArrayList<>();
		List<CrossingProjectEntity> crossingProjs = new ArrayList<>();

		if (!germIds.isEmpty()) {
			germs = germplasmService.findByIds(germIds);
		}
		if (!crossingProjIds.isEmpty()) {
			crossingProjs = crossingProjectService.findCrossingProjectsByIds(crossingProjIds);
		}

		if (!germIdsWithParentNodes.isEmpty()) {
			updateParentEdges(germIdsWithParentNodes, entityDtoPairsByGermId);
		}

		if (!germIdsWithProgenyNodes.isEmpty()) {
			updateChildEdges(germIdsWithProgenyNodes, entityDtoPairsByGermId);
		}

		for (Entry<String, Pair<PedigreeNodeEntity, PedigreeNode>>  entityDtoPairByGermId: entityDtoPairsByGermId.entrySet()) {
			PedigreeNodeEntity entity = entityDtoPairByGermId.getValue().getLeft();
			PedigreeNode node = entityDtoPairByGermId.getValue().getRight();

			if (node.getGermplasmDbId() != null && entity.getGermplasm() == null) {
				String germId = node.getGermplasmDbId();
				Optional<GermplasmEntity> germEntity = germs.stream().filter(ge -> ge.getId().equals(germId)).findFirst();
				germEntity.ifPresent(entity::setGermplasm);
			}

			UpdateUtility.updateEntity(node, entity);

			if (node.getCrossingYear() != null)
				entity.setCrossingYear(node.getCrossingYear());
			if (node.getFamilyCode() != null)
				entity.setFamilyCode(node.getFamilyCode());
			if (node.getPedigreeString() != null)
				entity.setPedigreeString(node.getPedigreeString());

			if (node.getCrossingProjectDbId() != null) {
				String cpId = node.getCrossingProjectDbId();
				Optional<CrossingProjectEntity> crossingProjectEntity = crossingProjs.stream().filter(cp -> cp.getId().equals(cpId)).findFirst();
				crossingProjectEntity.ifPresent(entity::setCrossingProject);
			}
		}
	}

	private void updateParentEdges(List<String> germIdsWithParentNodes,
								   Map<String, Pair<PedigreeNodeEntity, PedigreeNode>> entityDtoPairsByGermId)
		throws BrAPIServerException {

		List<String> parentEdgesToDelete = new ArrayList<>();

		SearchQueryBuilder<PedigreeEdgeEntity> search = new SearchQueryBuilder<>(PedigreeEdgeEntity.class);
		search.appendList(germIdsWithParentNodes, "connectedNode.germplasm.id");
		search.appendEnum(PedigreeEdgeEntity.EdgeType.child, "edgeType");
		List<PedigreeEdgeEntity> existingParentEdges = pedigreeEdgeRepository.findAllBySearch(search);

		List<String> existingParentEdgesFromPassedEntities = entityDtoPairsByGermId.entrySet()
				.stream()
				.flatMap(entry -> entry.getValue().getLeft().getParentEdges().stream())
				.map(BrAPIBaseEntity::getId)
				.collect(Collectors.toList());

		parentEdgesToDelete.addAll(existingParentEdgesFromPassedEntities);
		parentEdgesToDelete.addAll(existingParentEdges.stream().map(BrAPIBaseEntity::getId).collect(Collectors.toList()));

		if (!parentEdgesToDelete.isEmpty()) {
			pedigreeEdgeRepository.deleteAllByIdInBatch(parentEdgesToDelete);
		}

		List<Pair<PedigreeNodeEntity, PedigreeNode>> nodesWithParents = entityDtoPairsByGermId.values()
				.stream()
				.filter(p -> !p.getRight().getParents().isEmpty())
				.collect(Collectors.toList());

		List<String> germIdsOfAllParents = nodesWithParents.stream()
				.flatMap(p -> p.getRight().getParents().stream())
				.map(PedigreeNodeParents::getGermplasmDbId)
				.collect(Collectors.toList());


		List<PedigreeNodeEntity> createdOrFoundParentNodes = findOrCreatePedigreeNodesFromGermplasmIds(germIdsOfAllParents);

		for (Pair<PedigreeNodeEntity, PedigreeNode> nodeWithParent : nodesWithParents) {
			PedigreeNodeEntity nodeEntity = nodeWithParent.getLeft();
			PedigreeNode nodeDto = nodeWithParent.getRight();
			for (PedigreeNodeParents parentNode : nodeDto.getParents()) {
				// Is it possible that more that any of these parents share the same germplasm ID?  If so, we need to figure out how to handle that use case.
				PedigreeNodeEntity parentEntity = createdOrFoundParentNodes.stream()
						.filter(pne -> pne.getGermplasm().getId().equals(parentNode.getGermplasmDbId()))
						.findFirst()
						.orElse(null);

				// Impossible to be null because of exception thrown in findOrCreatePedigreeNodesFromGermplasmIds().
				// As long as the germIds of all the parents nodes in the rq were supplied, they will be found or created or this code is never executed.
				if (parentEntity != null) {
					nodeEntity.addParent(parentEntity, parentNode.getParentType());
					parentEntity.addProgeny(nodeEntity, parentNode.getParentType());
				}
			}
		}
	}

	private void updateChildEdges(List<String> germIdsWithProgenyNodes,
								  Map<String, Pair<PedigreeNodeEntity, PedigreeNode>> entityDtoPairsByGermId)
		throws BrAPIServerException {
		List<String> progenyEdgesToDelete = new ArrayList<>();

		SearchQueryBuilder<PedigreeEdgeEntity> search = new SearchQueryBuilder<PedigreeEdgeEntity>(PedigreeEdgeEntity.class);

		search.appendList(germIdsWithProgenyNodes, "conncetedNode.germplasm.id");
		search.appendEnum(PedigreeEdgeEntity.EdgeType.parent, "edgeType");
		List<PedigreeEdgeEntity> existingProgenyEdges = pedigreeEdgeRepository.findAllBySearch(search);

		List<String> existingProgenyEdgeFromPassedEntities = entityDtoPairsByGermId.entrySet()
				.stream()
				.flatMap(entry -> entry.getValue().getLeft().getProgenyEdges().stream())
				.map(BrAPIBaseEntity::getId)
				.collect(Collectors.toList());

		progenyEdgesToDelete.addAll(existingProgenyEdgeFromPassedEntities);
		progenyEdgesToDelete.addAll(existingProgenyEdges.stream().map(BrAPIBaseEntity::getId).collect(Collectors.toList()));

		if (!progenyEdgesToDelete.isEmpty()) {
			pedigreeEdgeRepository.deleteAllByIdInBatch(progenyEdgesToDelete);
		}

		List<Pair<PedigreeNodeEntity, PedigreeNode>> nodesWithProgeny = entityDtoPairsByGermId.values()
				.stream()
				.filter(p -> !p.getRight().getProgeny().isEmpty())
				.collect(Collectors.toList());

		List<String> germIdsOfAllProgeny = nodesWithProgeny.stream()
				.flatMap(p -> p.getRight().getParents().stream())
				.map(PedigreeNodeParents::getGermplasmDbId)
				.collect(Collectors.toList());

		List<PedigreeNodeEntity> createdOrFoundProgenyNodes = findOrCreatePedigreeNodesFromGermplasmIds(germIdsOfAllProgeny);

		for (Pair<PedigreeNodeEntity, PedigreeNode> nodeWithProgeny : nodesWithProgeny) {
			PedigreeNodeEntity nodeEntity = nodeWithProgeny.getLeft();
			PedigreeNode nodeDto = nodeWithProgeny.getRight();
			// Create a map of Nodes with progeny to its

			for (PedigreeNodeParents childNode : nodeDto.getProgeny()) {
				PedigreeNodeEntity childEntity = createdOrFoundProgenyNodes.stream()
						.filter(pne -> pne.getGermplasm().getId().equals(childNode.getGermplasmDbId()))
						.findFirst()
						.orElse(null);

				// Impossible to be null because of exception thrown in findOrCreatePedigreeNodesFromGermplasmIds().
				// As long as the germIds of all the parents nodes in the rq were supplied, they will be found or created or this code is never executed.
				if (childEntity != null) {
					nodeEntity.addParent(childEntity, childNode.getParentType());
					childEntity.addProgeny(nodeEntity, childNode.getParentType());
				}
			}
		}
	}

	public List<PedigreeNode> convertFromGermplasmToPedigreeBatchUsingNames(List<Germplasm> germplasms)
		throws BrAPIServerException{
		List<PedigreeNode> result = new ArrayList<PedigreeNode>();

		Map<Germplasm, String> germsByPedigreeMother = new HashMap<>();
		Map<Germplasm, String> germsByPedigreeFather = new HashMap<>();

		for (Germplasm germplasm : germplasms) {
			List<String> pedigreeList = Arrays.asList(germplasm.getPedigree().split("/"));

			if (!pedigreeList.isEmpty()) {
				germsByPedigreeMother.put(germplasm, pedigreeList.get(0));

				if (pedigreeList.size() > 1) {
					germsByPedigreeFather.put(germplasm, pedigreeList.get(1));
				}
			}
		}

		List<GermplasmEntity> motherGerms = germplasmService.findByNames(new ArrayList<>(germsByPedigreeMother.values()));
		List<GermplasmEntity> fatherGerms = germplasmService.findByNames(new ArrayList<>(germsByPedigreeFather.values()));

		for (Germplasm germplasm : germplasms) {
			GermplasmEntity motherGerm = null;
			GermplasmEntity fatherGerm = null;

			if (germsByPedigreeMother.containsKey(germplasm)) {
				String motherString = germsByPedigreeMother.get(germplasm);
				motherGerm = motherGerms.stream().filter(g -> g.getGermplasmName().equals(motherString)).findFirst().orElse(null);
			}

			if (germsByPedigreeFather.containsKey(germplasm)) {
				String fatherString = germsByPedigreeFather.get(germplasm);
				fatherGerm = fatherGerms.stream().filter(g -> g.getGermplasmName().equals(fatherString)).findFirst().orElse(null);
			}

			result.add(createPedigreeFromGermplasm(germplasm, motherGerm, fatherGerm));
		}

		return result;
	}

	public PedigreeNode convertFromGermplasmToPedigree(Germplasm germplasm)
		throws BrAPIServerException {
		PedigreeNode node = new PedigreeNode();

		List<String> pedigreeList = new ArrayList<>();
		if (germplasm.getPedigree() != null) {
			pedigreeList = Arrays.asList(germplasm.getPedigree().split("/"));
		}

		// TODO: Could split this up into one query in batch that returns a Map of Germplasm to its Nodes.
		GermplasmEntity motherGerm = null;
		GermplasmEntity fatherGerm = null;
		if (pedigreeList.size() > 0) {
			motherGerm = germplasmService.findByUnknownIdentity(pedigreeList.get(0));
			if (pedigreeList.size() > 1) {
				fatherGerm = germplasmService.findByUnknownIdentity(pedigreeList.get(1));
			}
		}

		return createPedigreeFromGermplasm(germplasm,
				motherGerm,
				fatherGerm);
	}

	public PedigreeNode createPedigreeFromGermplasm(Germplasm germplasm,
									  GermplasmEntity motherGerm,
									  GermplasmEntity fatherGerm) {
		PedigreeNode node = new PedigreeNode();

		node.setAdditionalInfo(germplasm.getAdditionalInfo());
		node.setBreedingMethodDbId(germplasm.getBreedingMethodDbId());
		node.setBreedingMethodName(germplasm.getBreedingMethodName());
		node.setDefaultDisplayName(germplasm.getDefaultDisplayName());
		node.setExternalReferences(germplasm.getExternalReferences());
		node.setGermplasmDbId(germplasm.getGermplasmDbId());
		node.setGermplasmName(germplasm.getGermplasmName());
		node.setGermplasmPUI(germplasm.getGermplasmPUI());
		node.setPedigreeString(germplasm.getPedigree());

		if (motherGerm != null) {
			PedigreeNodeParents mother = new PedigreeNodeParents();
			mother.setGermplasmDbId(motherGerm.getId());
			mother.setGermplasmName(motherGerm.getGermplasmName());
			mother.setParentType(ParentType.FEMALE);
			node.addParentsItem(mother);
		}
		if (fatherGerm != null) {
			PedigreeNodeParents father = new PedigreeNodeParents();
			father.setGermplasmDbId(fatherGerm.getId());
			father.setGermplasmName(fatherGerm.getGermplasmName());
			father.setParentType(ParentType.MALE);
			node.addParentsItem(father);
		}

		return node;
	}
}
