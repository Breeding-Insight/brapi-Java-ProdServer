package io.swagger.model.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets ListTypes
 */
public enum BatchDeleteTypes {
  GERMPLASM("germplasm"),
  LISTS("lists"),
  TRIALS("trials"),
  SAMPLES("samples");


  private final String value;

  BatchDeleteTypes(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static BatchDeleteTypes fromValue(String text) {
    for (BatchDeleteTypes b : BatchDeleteTypes.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
