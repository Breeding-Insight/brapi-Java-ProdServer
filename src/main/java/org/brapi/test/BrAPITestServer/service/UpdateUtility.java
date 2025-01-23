package org.brapi.test.BrAPITestServer.service;

import io.swagger.model.BrAPIDataModel;
import io.swagger.model.ExternalReferences;
import io.swagger.model.ExternalReferencesInner;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIPrimaryEntity;
import org.brapi.test.BrAPITestServer.model.entity.ExternalReferenceEntity;

import java.util.*;
import java.util.stream.Collectors;

public class UpdateUtility {

	public static <T> T replaceField(Optional<T> newValue, T oldValue) {
		if (newValue == null) {
			return oldValue;
		} else {
			if (newValue.isPresent()) {
				return newValue.get();
			} else {
				return null;
			}
		}
	}

	public static <T> Optional<T> setField(T value) {
		if (value == null) {
			return Optional.empty();
		} else {
			return Optional.of(value);
		}
	}

	public static <T extends BrAPIDataModel> T convertFromEntity(BrAPIPrimaryEntity entity, T model) {
		model.setAdditionalInfo(entity.getAdditionalInfo());
		model.setExternalReferences(entity.getExternalReferencesMap());
		return model;
	}

	public static <T extends BrAPIPrimaryEntity> T updateEntity(BrAPIDataModel model, T entity) {
		updateAdditionalInfo(model, entity);
		if (model.getExternalReferences() != null) {
			entity.setExternalReferences(model.getExternalReferences());
		}
		return entity;
	}

	private static <T extends  BrAPIPrimaryEntity> void updateAdditionalInfo(BrAPIDataModel model, T entity) {
		if (model.getAdditionalInfo() != null) {
			entity.setAdditionalInfo(model.getAdditionalInfo());
		}
	}

	/*
	Call this method when external references are eagerly loaded for bulk updates to entities to ensure
	unnecessary deletions and insertions don't occur.  This will improve performance in these use cases.

	WARN: If refs aren't eagerly loaded, hibernate will generate a query on the entity.getReferences() call.  This could slow performance.

	This method will check if the external references in the model already exist in the entity, and will prevent setting
	the entity with the model's refs.

	TODO: See if migrating all callers of updateEntity to this method can be done.
	 */
	public static <T extends BrAPIPrimaryEntity> T updateEntityCheckExRefs(BrAPIDataModel model, T entity) {
		updateAdditionalInfo(model, entity);
		if (model.getExternalReferences() != null) {

			ExternalReferences exRefs = model.getExternalReferences();

			Map<String, List<ExternalReferenceEntity>> existingRefsById =
					entity.getExternalReferences() != null ?
							entity.getExternalReferences().stream().collect(Collectors.groupingBy(e -> e.getExternalReferenceId().toString()))
							: Collections.emptyMap();


			List<ExternalReferencesInner> newExRefs = exRefs.stream().filter(exRef -> {
				List<ExternalReferenceEntity> existingEntityRefList = existingRefsById.get(exRef.getReferenceID());

				if (existingEntityRefList == null || existingEntityRefList.isEmpty()) {
					return true;
				}

				ExternalReferenceEntity existingEntityRef = existingEntityRefList.getFirst();

				return !existingEntityRef.getExternalReferenceSource().equals(exRef.getReferenceSource());
			}).toList();

			if (!newExRefs.isEmpty()) {
				// Detected different ex refs than what is in the original entity. Updating entity exRefs.
				entity.setExternalReferences(model.getExternalReferences());
			}
			// If there are no new exRefs no update is made.
		}
		return entity;
	}
}