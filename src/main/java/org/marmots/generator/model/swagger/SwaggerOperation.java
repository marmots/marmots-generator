package org.marmots.generator.model.swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.marmots.generator.model.ChildContext;
import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.utils.NameUtils;

import io.swagger.models.ExternalDocs;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.parameters.Parameter;

public class SwaggerOperation extends GeneratorContext {
  /**
   * generated uid
   */
  private static final long serialVersionUID = -4825075068112185137L;

  private Operation operation;

  @Override
  public String getAttrName() {
    return "operation";
  }

  @Override
  public String getClassName() {
    return "Operation";
  }

  @Override
  public String getInstanceName() {
    return operation.getOperationId();
  }

  @Override
  public String getInstanceClassName() {
    return NameUtils.toClassName(operation.getOperationId());
  }

  public Operation getOperation() {
    return operation;
  }

  public void setOperation(Operation operation) {
    this.operation = operation;
  }

  public static SwaggerOperation create(Operation operation) {
    SwaggerOperation swaggerOperation = new SwaggerOperation();
    swaggerOperation.setOperation(operation);
    return swaggerOperation;
  }

  @ChildContext(value = "parameter", context = SwaggerParameter.class)
  public List<SwaggerParameter> getEntities() {
    List<SwaggerParameter> parameters = new ArrayList<>();
    for (Parameter parameter : operation.getParameters()) {
      parameters.add(SwaggerParameter.create(parameter));
    }
    return parameters;
  }

  public Operation summary(String summary) {
    return operation.summary(summary);
  }

  public Operation description(String description) {
    return operation.description(description);
  }

  public Operation operationId(String operationId) {
    return operation.operationId(operationId);
  }

  public Operation schemes(List<Scheme> schemes) {
    return operation.schemes(schemes);
  }

  public Operation scheme(Scheme scheme) {
    return operation.scheme(scheme);
  }

  public Operation consumes(List<String> consumes) {
    return operation.consumes(consumes);
  }

  public Operation consumes(String consumes) {
    return operation.consumes(consumes);
  }

  public Operation produces(List<String> produces) {
    return operation.produces(produces);
  }

  public Operation produces(String produces) {
    return operation.produces(produces);
  }

  public Operation security(SecurityRequirement security) {
    return operation.security(security);
  }

  public Operation parameter(Parameter parameter) {
    return operation.parameter(parameter);
  }

  public Operation response(int key, Response response) {
    return operation.response(key, response);
  }

  public Operation defaultResponse(Response response) {
    return operation.defaultResponse(response);
  }

  public Operation tags(List<String> tags) {
    return operation.tags(tags);
  }

  public Operation tag(String tag) {
    return operation.tag(tag);
  }

  public Operation externalDocs(ExternalDocs externalDocs) {
    return operation.externalDocs(externalDocs);
  }

  public Operation deprecated(Boolean deprecated) {
    return operation.deprecated(deprecated);
  }

  public List<String> getTags() {
    return operation.getTags();
  }

  public void setTags(List<String> tags) {
    operation.setTags(tags);
  }

  public void addTag(String tag) {
    operation.addTag(tag);
  }

  public String getSummary() {
    return operation.getSummary();
  }

  public void setSummary(String summary) {
    operation.setSummary(summary);
  }

  public String getDescription() {
    return operation.getDescription();
  }

  public void setDescription(String description) {
    operation.setDescription(description);
  }

  public String getOperationId() {
    return operation.getOperationId();
  }

  public void setOperationId(String operationId) {
    operation.setOperationId(operationId);
  }

  public List<Scheme> getSchemes() {
    return operation.getSchemes();
  }

  public void setSchemes(List<Scheme> schemes) {
    operation.setSchemes(schemes);
  }

  public void addScheme(Scheme scheme) {
    operation.addScheme(scheme);
  }

  public List<String> getConsumes() {
    return operation.getConsumes();
  }

  public void setConsumes(List<String> consumes) {
    operation.setConsumes(consumes);
  }

  public void addConsumes(String consumes) {
    operation.addConsumes(consumes);
  }

  public List<String> getProduces() {
    return operation.getProduces();
  }

  public void setProduces(List<String> produces) {
    operation.setProduces(produces);
  }

  public void addProduces(String produces) {
    operation.addProduces(produces);
  }

  public List<Parameter> getParameters() {
    return operation.getParameters();
  }

  public void setParameters(List<Parameter> parameters) {
    operation.setParameters(parameters);
  }

  public void addParameter(Parameter parameter) {
    operation.addParameter(parameter);
  }

  public Map<String, Response> getResponses() {
    return operation.getResponses();
  }

  public void setResponses(Map<String, Response> responses) {
    operation.setResponses(responses);
  }

  public void addResponse(String key, Response response) {
    operation.addResponse(key, response);
  }

  public List<Map<String, List<String>>> getSecurity() {
    return operation.getSecurity();
  }

  public void setSecurity(List<Map<String, List<String>>> security) {
    operation.setSecurity(security);
  }

  public void addSecurity(String name, List<String> scopes) {
    operation.addSecurity(name, scopes);
  }

  public ExternalDocs getExternalDocs() {
    return operation.getExternalDocs();
  }

  public void setExternalDocs(ExternalDocs value) {
    operation.setExternalDocs(value);
  }

  public Boolean isDeprecated() {
    return operation.isDeprecated();
  }

  public void setDeprecated(Boolean value) {
    operation.setDeprecated(value);
  }

  public Map<String, Object> getVendorExtensions() {
    return operation.getVendorExtensions();
  }

  public void setVendorExtension(String name, Object value) {
    operation.setVendorExtension(name, value);
  }

  public void setVendorExtensions(Map<String, Object> vendorExtensions) {
    operation.setVendorExtensions(vendorExtensions);
  }

  public int hashCode() {
    return operation.hashCode();
  }

  public boolean equals(Object obj) {
    return operation.equals(obj);
  }

  public Operation vendorExtensions(Map<String, Object> vendorExtensions) {
    return operation.vendorExtensions(vendorExtensions);
  }

  public String toString() {
    return operation.toString();
  }

}
