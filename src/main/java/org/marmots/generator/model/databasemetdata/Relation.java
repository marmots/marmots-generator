package org.marmots.generator.model.databasemetdata;

import java.util.List;

import org.marmots.generator.model.AbstractGeneratorModel;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class Relation extends AbstractGeneratorModel {
  /**
   * generated uid
   */
  private static final long serialVersionUID = 6686798665365163621L;

  private String name;
  private String fullName;
  private String definition;
  private String remarks;
  private String constraintType;
  private String cardinality;

  private RelationType type;
  private boolean imported;

  private List<Reference> references;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getConstraintType() {
    return constraintType;
  }

  public void setConstraintType(String constraintType) {
    this.constraintType = constraintType;
  }

  public RelationType getType() {
    return type;
  }

  public void setType(RelationType type) {
    this.type = type;
  }

  public boolean isImported() {
    return imported;
  }

  public void setImported(boolean imported) {
    this.imported = imported;
  }

  public List<Reference> getReferences() {
    return references;
  }

  public void setReferences(List<Reference> references) {
    this.references = references;
  }

  public String getCardinality() {
    return cardinality;
  }

  public void setCardinality(String cardinality) {
    this.cardinality = cardinality;
  }

}
