package org.brapi.test.BrAPITestServer.model.entity.core;

import jakarta.persistence.*;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

import java.util.Date;

@Embeddable
public class StudyLastUpdateEntity {
	@Column(name = "timestamp", table = "study_last_update")
	private Date timestamp;
	@Column(name = "version", table = "study_last_update")
	private String version;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
