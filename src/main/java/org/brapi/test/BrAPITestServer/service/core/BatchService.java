package org.brapi.test.BrAPITestServer.service.core;

import io.swagger.model.core.*;
import io.swagger.model.core.BatchDeleteNewRequest;
import io.swagger.model.core.BatchDeleteSummary;
import jakarta.validation.Valid;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerDbIdNotFoundException;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.model.entity.core.BatchDeleteEntity;
import org.brapi.test.BrAPITestServer.model.entity.core.BatchDeleteItemEntity;
import org.brapi.test.BrAPITestServer.model.entity.core.PersonEntity;
import org.brapi.test.BrAPITestServer.repository.core.BatchDeleteRepository;
import org.brapi.test.BrAPITestServer.service.DateUtility;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatchService {

	private final BatchDeleteRepository batchDeleteRepository;
	private final PeopleService peopleService;

	public BatchService(BatchDeleteRepository batchDeleteRepository, PeopleService peopleService) {
		this.batchDeleteRepository = batchDeleteRepository;
		this.peopleService = peopleService;
	}

	public BatchDeleteDetails getBatch(String batchDbId) throws BrAPIServerException {
		BatchDeleteEntity entity;

		Optional<BatchDeleteEntity> entityOpt = batchDeleteRepository.findById(batchDbId);
		if (entityOpt.isPresent()) {
			entity = entityOpt.get();
		} else {
			throw new BrAPIServerDbIdNotFoundException("batch", batchDbId, HttpStatus.NOT_FOUND);
		}

		return convertToDetails(entity);
	}

	public BatchDeleteDetails updateBatchItems(String batchDbId, List<String> batchItems) throws BrAPIServerException {
		BatchDeleteEntity savedEntity;
		Optional<BatchDeleteEntity> entityOpt = batchDeleteRepository.findById(batchDbId);
		if (entityOpt.isPresent()) {
			BatchDeleteEntity entity = entityOpt.get();
			entity.setDateModified(new Date());

			List<BatchDeleteItemEntity> itemEntities = batchItems.stream().map((item) -> {
				BatchDeleteItemEntity itemEntity = new BatchDeleteItemEntity();
				itemEntity.setItem(item);
				itemEntity.setBatchDelete(entity);
				return itemEntity;
			}).collect(Collectors.toList());

			entity.getData().addAll(itemEntities);

			savedEntity = batchDeleteRepository.save(entity);
		} else {
			throw new BrAPIServerDbIdNotFoundException("batch", batchDbId, HttpStatus.NOT_FOUND);
		}

		return convertToDetails(savedEntity);
	}

	public BatchDeleteDetails updateBatch(String batchDbId, BatchDeleteNewRequest batch) throws BrAPIServerException {
		BatchDeleteEntity savedEntity;
		Optional<BatchDeleteEntity> entityOpt = batchDeleteRepository.findById(batchDbId);
		if (entityOpt.isPresent()) {
			BatchDeleteEntity entity = entityOpt.get();
			updateEntity(entity, batch);
			entity.setDateModified(new Date());

			savedEntity = batchDeleteRepository.save(entity);
		} else {
			throw new BrAPIServerDbIdNotFoundException("batch", batchDbId, HttpStatus.NOT_FOUND);
		}

		return convertToDetails(savedEntity);
	}

	public List<BatchDeleteSummary> saveNewBatch(@Valid List<BatchDeleteNewRequest> requests) throws BrAPIServerException {

		List<BatchDeleteSummary> savedBatches = new ArrayList<>();

		for (BatchDeleteNewRequest batch : requests) {

			BatchDeleteEntity entity = new BatchDeleteEntity();
			updateEntity(entity, batch);
			Date now = new Date();
			entity.setDateCreated(now);
			entity.setDateModified(now);

			BatchDeleteEntity savedEntity = batchDeleteRepository.save(entity);

			savedBatches.add(convertToSummary(savedEntity));
		}

		return savedBatches;
	}

	public void deleteBatch(String batchDbId) throws BrAPIServerException {
		batchDeleteRepository.deleteAllByIdInBatch(Arrays.asList(batchDbId));
	}

	private BatchDeleteDetails convertToDetails(BatchDeleteEntity entity) {
		BatchDeleteDetails details = new BatchDeleteDetails();
		details = (BatchDeleteDetails) convertToBaseFields(entity, details);
		details.setBatchDeleteDbId(entity.getId().toString());
		details.setData(entity.getData().stream().map((e) -> e.getItem()).collect(Collectors.toList()));

		return details;
	}

	private BatchDeleteSummary convertToSummary(BatchDeleteEntity entity) {
		BatchDeleteSummary summary = new BatchDeleteSummary();
		summary = (BatchDeleteSummary) convertToBaseFields(entity, summary);
		summary.setBatchDeleteDbId(entity.getId().toString());

		return summary;
	}

	private BatchDeleteBaseFieldsInterface convertToBaseFields(BatchDeleteEntity entity, BatchDeleteBaseFieldsInterface base) {
		base.setDateCreated(DateUtility.toOffsetDateTime(entity.getDateCreated()));
		base.setDateModified(DateUtility.toOffsetDateTime(entity.getDateModified()));
		base.setBatchDeleteDescription(entity.getDescription());
		base.setBatchDeleteName(entity.getBatchDeleteName());
		base.setBatchDeleteOwnerName(entity.getBatchDeleteOwnerName());
		base.setBatchDeleteSource(entity.getBatchDeleteSource());
		base.setBatchDeleteType(entity.getBatchDeleteType());
		base.setAdditionalInfo(entity.getAdditionalInfo());
		base.setExternalReferences(entity.getExternalReferencesMap());

		if (entity.getBatchDeleteOwnerPerson() != null) {
			base.setBatchDeleteOwnerPersonDbId(entity.getBatchDeleteOwnerPerson().getId().toString());
		}
		if (entity.getData() != null) {
			base.setBatchDeleteSize(entity.getData().size());
		}
		return base;
	}

	private void updateEntity(BatchDeleteEntity entity, @Valid BatchDeleteNewRequest batch) throws BrAPIServerException {

		if (batch.getAdditionalInfo() != null)
			entity.setAdditionalInfo(batch.getAdditionalInfo());
		if (batch.getBatchDeleteDescription() != null)
			entity.setDescription(batch.getBatchDeleteDescription());
		if (batch.getExternalReferences() != null)
			entity.setExternalReferences(batch.getExternalReferences());
		if (batch.getBatchDeleteName() != null)
			entity.setBatchDeleteName(batch.getBatchDeleteName());
		if (batch.getBatchDeleteOwnerName() != null)
			entity.setBatchDeleteOwnerName(batch.getBatchDeleteOwnerName());
		if (batch.getBatchDeleteSource() != null)
			entity.setBatchDeleteSource(batch.getBatchDeleteSource());
		if (batch.getBatchDeleteType() != null)
			entity.setBatchDeleteType(batch.getBatchDeleteType());

		if (batch.getBatchDeleteOwnerPersonDbId() != null) {
			PersonEntity person = peopleService.getPersonEntity(batch.getBatchDeleteOwnerPersonDbId());
			entity.setBatchDeleteOwnerPerson(person);
		}

		if (entity.getData() != null) {
			entity.getData().stream().forEach((item) -> {
				item.setBatchDelete(null);
			});
		}

		if (batch.getData() != null) {
			List<BatchDeleteItemEntity> items = new ArrayList<>();
			ListIterator<String> iter = batch.getData().listIterator();
			while (iter.hasNext()) {
				String item = iter.next();
				if (item != null) {
					BatchDeleteItemEntity itemEntity = new BatchDeleteItemEntity();
					itemEntity.setItem(item);
					itemEntity.setBatchDelete(entity);
					items.add(itemEntity);
				}
			}
			entity.setData(items);
		} else {
			entity.setData(new ArrayList<>());
		}
	}
}