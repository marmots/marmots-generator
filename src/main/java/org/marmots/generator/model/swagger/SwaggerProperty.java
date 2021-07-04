package org.marmots.generator.model.swagger;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.marmots.generator.model.GeneratorContext;

import io.swagger.models.Xml;
import io.swagger.models.properties.Property;

public class SwaggerProperty extends GeneratorContext {
  /**
   * generated uid
   */
  private static final long serialVersionUID = 3451582199023156940L;

  private String name;
  private Property property;

  @Override
  public String getClassName() {
    return "Property";
  }

  @Override
  public String getAttrName() {
    return "property";
  }

  @Override
  public String getInstanceName() {
    return StringUtils.uncapitalize(name);
  }

  @Override
  public String getInstanceClassName() {
    return name;
  }

  public static SwaggerProperty create(String name, Property property) {
    SwaggerProperty swaggerProperty = new SwaggerProperty();
    swaggerProperty.setName(name);
    swaggerProperty.setProperty(property);
    return swaggerProperty;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Property getProperty() {
    return property;
  }

  public void setProperty(Property property) {
    this.property = property;
  }

  public Property title(String title) {
    return property.title(title);
  }

  public Property description(String description) {
    return property.description(description);
  }

  public String getType() {
    return property.getType();
  }

  public String getFormat() {
    return property.getFormat();
  }

  public String getTitle() {
    return property.getTitle();
  }

  public void setTitle(String title) {
    property.setTitle(title);
  }

  public String getDescription() {
    return property.getDescription();
  }

  public void setDescription(String title) {
    property.setDescription(title);
  }

  public Boolean getAllowEmptyValue() {
    return property.getAllowEmptyValue();
  }

  public void setAllowEmptyValue(Boolean value) {
    property.setAllowEmptyValue(value);
  }

  public boolean getRequired() {
    return property.getRequired();
  }

  public void setRequired(boolean required) {
    property.setRequired(required);
  }

  public Object getExample() {
    return property.getExample();
  }

  public void setExample(Object example) {
    property.setExample(example);
  }

  public Boolean getReadOnly() {
    return property.getReadOnly();
  }

  public void setReadOnly(Boolean readOnly) {
    property.setReadOnly(readOnly);
  }

  public Integer getPosition() {
    return property.getPosition();
  }

  public void setPosition(Integer position) {
    property.setPosition(position);
  }

  public Xml getXml() {
    return property.getXml();
  }

  public void setXml(Xml xml) {
    property.setXml(xml);
  }

  public void setDefault(String _default) {
    property.setDefault(_default);
  }

  public String getAccess() {
    return property.getAccess();
  }

  public void setAccess(String access) {
    property.setAccess(access);
  }

  public Map<String, Object> getVendorExtensions() {
    return property.getVendorExtensions();
  }

  public Property rename(String newName) {
    return property.rename(newName);
  }

}
