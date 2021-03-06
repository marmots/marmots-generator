package org.marmots.generator.model.databasemetdata;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.marmots.generator.model.AbstractGeneratorModel;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class Index extends AbstractGeneratorModel {
  /**
   * generated uid
   */
  private static final long serialVersionUID = -7077948150027374983L;

  private String name;
  private String fullName;
  private int cardinality;
  private String definition;
  private String remarks;
  private boolean unique;

  private String clazz;
  private String clazzPlural;
  private String attr;
  private String attrPlural;

  private List<Attribute> attributes;

  public Set<Attribute> getNonTextAttributes() {
    Set<Attribute> attrs = new LinkedHashSet<>();
    for (Attribute attr : getAttributes()) {
      if (!attr.isLongText()) {
        attrs.add(attr);
      }
    }
    return attrs;
  }

  public String getPKAttributes() {
    return getPKAttributes(false);
  }

  public String getPKAttributes(boolean pathVariable) {
    String attrs = "";
    for (Attribute column : getAttributes()) {
      attrs += (StringUtils.isEmpty(attrs) ? "" : ", ") + (pathVariable ? "@PathVariable " : "") + (pathVariable && column.isDateType() ? "long" : column.getJavaType()) + " "
          + column.getAttr();
    }
    return attrs;
  }

  public String getPKAttributesCall() {
    return getPKAttributesCall(false);
  }

  public String getPKAttributesCall(boolean pathVariable) {
    String attrs = "";
    for (Attribute attribute : getAttributes()) {
      attrs += (StringUtils.isEmpty(attrs) ? "" : ", ") + (pathVariable && attribute.isDateType() ? "new java.util.Date(" + attribute.getAttr() + ")" : attribute.getAttr());
    }
    return attrs;
  }

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

  public int getCardinality() {
    return cardinality;
  }

  public void setCardinality(int cardinality) {
    this.cardinality = cardinality;
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

  public boolean isUnique() {
    return unique;
  }

  public void setUnique(boolean unique) {
    this.unique = unique;
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  public String getClazzPlural() {
    return clazzPlural;
  }

  public void setClazzPlural(String clazzPlural) {
    this.clazzPlural = clazzPlural;
  }

  public String getAttr() {
    return attr;
  }

  public void setAttr(String attr) {
    this.attr = attr;
  }

  public String getAttrPlural() {
    return attrPlural;
  }

  public void setAttrPlural(String attrPlural) {
    this.attrPlural = attrPlural;
  }
}
