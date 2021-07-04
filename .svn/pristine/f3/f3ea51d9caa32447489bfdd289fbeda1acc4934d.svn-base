package org.marmots.generator.model.swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.marmots.generator.model.ChildContext;
import org.marmots.generator.model.GeneratorContext;

import io.swagger.models.ExternalDocs;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;

public class SwaggerEntity extends GeneratorContext {
  /**
   * generated uid
   */
  private static final long serialVersionUID = 369187370378632771L;

  private String name;
  private Model model;

  @Override
  public String getAttrName() {
    return "entity";
  }

  @Override
  public String getClassName() {
    return "Entity";
  }

  @Override
  public String getInstanceName() {
    return StringUtils.uncapitalize(name);
  }

  @Override
  public String getInstanceClassName() {
    return name;
  }

  public Model getModel() {
    return model;
  }

  public void setModel(Model model) {
    this.model = model;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static SwaggerEntity create(String name, Model model) {
    SwaggerEntity entity = new SwaggerEntity();
    entity.setName(name);
    entity.setModel(model);
    return entity;
  }

  public String getTitle() {
    return model.getTitle();
  }

  public void setTitle(String title) {
    model.setTitle(title);
  }

  public String getDescription() {
    return model.getDescription();
  }

  public void setDescription(String description) {
    model.setDescription(description);
  }

  @ChildContext(value = "property", context = SwaggerProperty.class)
  public List<SwaggerProperty> getProperties() {
    List<SwaggerProperty> properties = new ArrayList<>();
    for (Map.Entry<String, Property> property : model.getProperties().entrySet()) {
      properties.add(SwaggerProperty.create(property.getKey(), property.getValue()));
    }
    return properties;
  }

  public void setProperties(Map<String, Property> properties) {
    model.setProperties(properties);
  }

  public Object getExample() {
    return model.getExample();
  }

  public void setExample(Object example) {
    model.setExample(example);
  }

  public ExternalDocs getExternalDocs() {
    return model.getExternalDocs();
  }

  public String getReference() {
    return model.getReference();
  }

  public void setReference(String reference) {
    model.setReference(reference);
  }

  public Map<String, Object> getVendorExtensions() {
    return model.getVendorExtensions();
  }

}
