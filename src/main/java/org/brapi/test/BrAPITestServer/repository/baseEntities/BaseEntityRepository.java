package org.brapi.test.BrAPITestServer.repository.baseEntities;

import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface BaseEntityRepository<T extends BrAPIBaseEntity, ID extends Serializable>
        extends JpaRepository<T, ID> {
}
