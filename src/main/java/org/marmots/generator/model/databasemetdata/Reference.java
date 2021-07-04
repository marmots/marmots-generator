package org.marmots.generator.model.databasemetdata;

import org.marmots.generator.model.AbstractGeneratorModel;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class Reference extends AbstractGeneratorModel {
  /**
   * generated uid
   */
  private static final long serialVersionUID = 6342157006260874016L;

  private Attribute primaryKey;
  private Attribute foreignKey;

  public Attribute getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Attribute primaryKey) {
    this.primaryKey = primaryKey;
  }

  public Attribute getForeignKey() {
    return foreignKey;
  }

  public void setForeignKey(Attribute foreignKey) {
    this.foreignKey = foreignKey;
  }

}
