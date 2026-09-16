package org.brapi.test.BrAPITestServer.model.dto;

public enum EntityType {
    TEXT,
    UUID,
    BOOLEAN,
    // Sorting is done at the database schema/formula level for numbers.  See GermplasmEntity.maleParentGid.  No changes to filtering required for this datatype.
    NUMBER
    // Add any other entity data types we should filter different in SearchQueryBuilder here
}
