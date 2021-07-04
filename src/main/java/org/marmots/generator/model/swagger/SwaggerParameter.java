package org.marmots.generator.model.swagger;

import java.util.Map;

import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.utils.NameUtils;

import io.swagger.models.parameters.Parameter;

public class SwaggerParameter extends GeneratorContext {
  /**
   * generated uid
   */
  private static final long serialVersionUID = -3893364632380444521L;

  private Parameter parameter;

  @Override
  public String getAttrName() {
    return "parameter";
  }

  @Override
  public String getClassName() {
    return "Parameter";
  }

  @Override
  public String getInstanceName() {
    return parameter.getName();
  }

  @Override
  public String getInstanceClassName() {
    return NameUtils.toClassName(parameter.getName());
  }

  public Parameter getParameter() {
    return parameter;
  }

  public void setParameter(Parameter parameter) {
    this.parameter = parameter;
  }

  public static SwaggerParameter create(Parameter parameter) {
    SwaggerParameter swaggerParameter = new SwaggerParameter();
    swaggerParameter.setParameter(parameter);
    return swaggerParameter;
  }

  public String getIn() {
    return parameter.getIn();
  }

  public void setIn(String in) {
    parameter.setIn(in);
  }

  public String getAccess() {
    return parameter.getAccess();
  }

  public void setAccess(String access) {
    parameter.setAccess(access);
  }

  public String getName() {
    return parameter.getName();
  }

  public void setName(String name) {
    parameter.setName(name);
  }

  public String getDescription() {
    return parameter.getDescription();
  }

  public void setDescription(String description) {
    parameter.setDescription(description);
  }

  public boolean getRequired() {
    return parameter.getRequired();
  }

  public void setRequired(boolean required) {
    parameter.setRequired(required);
  }

  public String getPattern() {
    return parameter.getPattern();
  }

  public void setPattern(String pattern) {
    parameter.setPattern(pattern);
  }

  public Map<String, Object> getVendorExtensions() {
    return parameter.getVendorExtensions();
  }

  public Boolean isReadOnly() {
    return parameter.isReadOnly();
  }

  public void setReadOnly(Boolean readOnly) {
    parameter.setReadOnly(readOnly);
  }

}
