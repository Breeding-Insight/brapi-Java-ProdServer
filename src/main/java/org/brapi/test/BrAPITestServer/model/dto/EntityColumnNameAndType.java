package org.brapi.test.BrAPITestServer.model.dto;

/**
 * This class is used to map an entity's column name to the SQL type of lookup that should be completed for filter searches.
 * The entityColumnName is also used for sorts as well.
 */
public class EntityColumnNameAndType {
    String entityColumnName;
    EntityType entityType;

    public EntityColumnNameAndType(String entityColumnName, EntityType entityType) {
        this.entityColumnName = entityColumnName;
        this.entityType = entityType;
    }

    public String getEntityColumnName() {
        return entityColumnName;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public String toString() {
        return this.entityColumnName;
    }
}
