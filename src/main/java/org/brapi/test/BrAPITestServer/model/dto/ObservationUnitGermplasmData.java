package org.brapi.test.BrAPITestServer.model.dto;

import java.util.UUID;

public class ObservationUnitGermplasmData {
    private String germplasmDbId;
    private String germplasmName;

    public ObservationUnitGermplasmData(String germplasmDbId,
                                        String germplasmName) {
        this.germplasmDbId = germplasmDbId;
        this.germplasmName = germplasmName;
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(String germplasmName) {
        this.germplasmName = germplasmName;
    }
}
