package io.swagger.model.pheno;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ObservationTableHeaderRowEnum {
    OBSERVATIONTIMESTAMP("observationTimeStamp"),
    OBSERVATIONUNITDBID("observationUnitDbId"),
    OBSERVATIONUNITNAME("observationUnitName"),
    STUDYDBID("studyDbId"),
    STUDYNAME("studyName"),
    LOCATIONDBID("locationDbId"),
    LOCATIONNAME("locationName"),
    GERMPLASMDBID("germplasmDbId"),
    GERMPLASMNAME("germplasmName"),
    POSITIONCOORDINATEX("positionCoordinateX"),
    POSITIONCOORDINATEY("positionCoordinateY"),
    YEAR("year");
private String value;

    ObservationTableHeaderRowEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ObservationTableHeaderRowEnum fromValue(String text) {
      for (ObservationTableHeaderRowEnum b : ObservationTableHeaderRowEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
}
