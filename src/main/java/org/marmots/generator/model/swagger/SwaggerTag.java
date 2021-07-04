package org.marmots.generator.model.swagger;

import java.util.List;
import java.util.Map;

import org.marmots.generator.model.ChildContext;
import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.utils.NameUtils;

import io.swagger.models.ExternalDocs;
import io.swagger.models.Tag;

public class SwaggerTag extends GeneratorContext {
  /**
   * generated uid
   */
  private static final long serialVersionUID = -2768720413575826700L;

  private Tag tag;
  private List<SwaggerOperation> operations;

  @Override
  public String getAttrName() {
    return "tag";
  }

  @Override
  public String getClassName() {
    return "Tag";
  }

  @Override
  public String getInstanceName() {
    return tag.getName();
  }

  @Override
  public String getInstanceClassName() {
    return NameUtils.toClassName(tag.getName());
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }

  public static SwaggerTag create(Tag tag, List<SwaggerOperation> operations) {
    SwaggerTag swaggerTag = new SwaggerTag();
    swaggerTag.setTag(tag);
    swaggerTag.setOperations(operations);
    return swaggerTag;
  }

  @ChildContext(value = "operation", context = SwaggerOperation.class)
  public List<SwaggerOperation> getOperations() {
    return operations;
  }

  public void setOperations(List<SwaggerOperation> operations) {
    this.operations = operations;
  }

  public Tag name(String name) {
    return tag.name(name);
  }

  public Tag description(String description) {
    return tag.description(description);
  }

  public Tag externalDocs(ExternalDocs externalDocs) {
    return tag.externalDocs(externalDocs);
  }

  public String getName() {
    return tag.getName();
  }

  public void setName(String name) {
    tag.setName(name);
  }

  public String getDescription() {
    return tag.getDescription();
  }

  public void setDescription(String description) {
    tag.setDescription(description);
  }

  public ExternalDocs getExternalDocs() {
    return tag.getExternalDocs();
  }

  public void setExternalDocs(ExternalDocs externalDocs) {
    tag.setExternalDocs(externalDocs);
  }

  public Map<String, Object> getVendorExtensions() {
    return tag.getVendorExtensions();
  }

  public void setVendorExtension(String name, Object value) {
    tag.setVendorExtension(name, value);
  }

  public void setVendorExtensions(Map<String, Object> vendorExtensions) {
    tag.setVendorExtensions(vendorExtensions);
  }

  public String toString() {
    return tag.toString();
  }

  public int hashCode() {
    return tag.hashCode();
  }

  public boolean equals(Object obj) {
    return tag.equals(obj);
  }

}
