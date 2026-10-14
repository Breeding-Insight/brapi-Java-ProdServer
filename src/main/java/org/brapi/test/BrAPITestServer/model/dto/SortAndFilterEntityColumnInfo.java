package org.brapi.test.BrAPITestServer.model.dto;

/**
 * This class is used to map an entity's column name to the SQL type of lookup that should be completed for filter searches.
 * The entityColumnName is also used for sorts as well.
 */
public class SortAndFilterEntityColumnInfo {
    private final String entityColumnName;
    private final EntityType entityType;
    private final boolean isCollectionOnEntity;
    private final boolean isJoinOnUnrelatedEntity;


    public SortAndFilterEntityColumnInfo(String entityColumnName, EntityType entityType) {
        this.entityColumnName = entityColumnName;
        this.entityType = entityType;
        this.isCollectionOnEntity = false;
        this.isJoinOnUnrelatedEntity = false;
    }

    public SortAndFilterEntityColumnInfo(String entityColumnName, EntityType entityType, boolean isCollectionOnEntity, boolean isJoinOnUnrelatedEntity) {
        this.entityColumnName = entityColumnName;
        this.entityType = entityType;
        this.isCollectionOnEntity = isCollectionOnEntity;
        this.isJoinOnUnrelatedEntity = isJoinOnUnrelatedEntity;
    }

    public String getEntityColumnName() {
        return entityColumnName;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public boolean isCollectionOnEntity() {
        return isCollectionOnEntity;
    }

    public boolean isJoinOnUnrelatedEntity() {
        return isJoinOnUnrelatedEntity;
    }

    @Override
    public String toString() {
        return this.entityColumnName;
    }
}
