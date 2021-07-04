package org.marmots.generator.model.swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.marmots.generator.model.ChildContext;
import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.utils.NameUtils;

import io.swagger.models.ExternalDocs;
import io.swagger.models.Info;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;

public class SwaggerApplication extends GeneratorContext {
  /**
   * generator uid
   */
  private static final long serialVersionUID = 4520342502826158101L;

  private Swagger swagger;

  @Override
  public String getAttrName() {
    return "application";
  }

  @Override
  public String getClassName() {
    return "Application";
  }

  @Override
  public String getInstanceName() {
    return NameUtils.toAttributeName(swagger.getInfo().getTitle());
  }

  @Override
  public String getInstanceClassName() {
    return NameUtils.toClassName(swagger.getInfo().getTitle());
  }

  public Swagger getSwagger() {
    return swagger;
  }

  public void setSwagger(Swagger swagger) {
    this.swagger = swagger;
  }

  public static SwaggerApplication create(Swagger swagger) {
    SwaggerApplication application = new SwaggerApplication();
    application.setSwagger(swagger);
    return application;
  }

  @ChildContext(value = "tag", context = SwaggerTag.class)
  public List<SwaggerTag> getTags() {
    List<SwaggerTag> tags = new ArrayList<>();
    if (swagger.getTags() != null) {
      for (Tag tag : swagger.getTags()) {
        List<SwaggerOperation> operations = new ArrayList<>();
        for (Path path : swagger.getPaths().values()) {
          for (Operation operation : path.getOperations()) {
            if (operation.getTags().contains(tag.getName())) {
              operations.add(SwaggerOperation.create(operation));
            }
          }
        }
        tags.add(SwaggerTag.create(tag, operations));
      }
    }
    return tags;
  }

  @ChildContext(value = "operation", context = SwaggerOperation.class)
  public List<SwaggerOperation> getOperations() {
    List<SwaggerOperation> operations = new ArrayList<>();
    for (Path path : swagger.getPaths().values()) {
      for (Operation operation : path.getOperations()) {
        operations.add(SwaggerOperation.create(operation));
      }
    }
    return operations;
  }

  @ChildContext(value = "entity", context = SwaggerEntity.class)
  public List<SwaggerEntity> getEntities() {
    List<SwaggerEntity> entities = new ArrayList<>();
    for (Map.Entry<String, Model> modelEntry : swagger.getDefinitions().entrySet()) {
      entities.add(SwaggerEntity.create(modelEntry.getKey(), modelEntry.getValue()));
    }
    return entities;
  }

  public Swagger info(Info info) {
    return swagger.info(info);
  }

  public Swagger host(String host) {
    return swagger.host(host);
  }

  public Swagger basePath(String basePath) {
    return swagger.basePath(basePath);
  }

  public Swagger externalDocs(ExternalDocs value) {
    return swagger.externalDocs(value);
  }

  public Swagger tags(List<Tag> tags) {
    return swagger.tags(tags);
  }

  public Swagger tag(Tag tag) {
    return swagger.tag(tag);
  }

  public Swagger schemes(List<Scheme> schemes) {
    return swagger.schemes(schemes);
  }

  public Swagger scheme(Scheme scheme) {
    return swagger.scheme(scheme);
  }

  public Swagger consumes(List<String> consumes) {
    return swagger.consumes(consumes);
  }

  public Swagger consumes(String consumes) {
    return swagger.consumes(consumes);
  }

  public Swagger produces(List<String> produces) {
    return swagger.produces(produces);
  }

  public Swagger produces(String produces) {
    return swagger.produces(produces);
  }

  public Swagger paths(Map<String, Path> paths) {
    return swagger.paths(paths);
  }

  public Swagger path(String key, Path path) {
    return swagger.path(key, path);
  }

  public Swagger responses(Map<String, Response> responses) {
    return swagger.responses(responses);
  }

  public Swagger response(String key, Response response) {
    return swagger.response(key, response);
  }

  public Swagger parameter(String key, Parameter parameter) {
    return swagger.parameter(key, parameter);
  }

  public Swagger securityDefinition(String name, SecuritySchemeDefinition securityDefinition) {
    return swagger.securityDefinition(name, securityDefinition);
  }

  public Swagger model(String name, Model model) {
    return swagger.model(name, model);
  }

  public Swagger security(SecurityRequirement securityRequirement) {
    return swagger.security(securityRequirement);
  }

  public Swagger vendorExtension(String key, Object extension) {
    return swagger.vendorExtension(key, extension);
  }

  public Info getInfo() {
    return swagger.getInfo();
  }

  public void setInfo(Info info) {
    swagger.setInfo(info);
  }

  public String getHost() {
    return swagger.getHost();
  }

  public void setHost(String host) {
    swagger.setHost(host);
  }

  public String getBasePath() {
    return swagger.getBasePath();
  }

  public void setBasePath(String basePath) {
    swagger.setBasePath(basePath);
  }

  public List<Scheme> getSchemes() {
    return swagger.getSchemes();
  }

  public void setSchemes(List<Scheme> schemes) {
    swagger.setSchemes(schemes);
  }

  public void addScheme(Scheme scheme) {
    swagger.addScheme(scheme);
  }

  public void setTags(List<Tag> tags) {
    swagger.setTags(tags);
  }

  public Tag getTag(String tagName) {
    return swagger.getTag(tagName);
  }

  public void addTag(Tag tag) {
    swagger.addTag(tag);
  }

  public List<String> getConsumes() {
    return swagger.getConsumes();
  }

  public void setConsumes(List<String> consumes) {
    swagger.setConsumes(consumes);
  }

  public void addConsumes(String consumes) {
    swagger.addConsumes(consumes);
  }

  public List<String> getProduces() {
    return swagger.getProduces();
  }

  public void setProduces(List<String> produces) {
    swagger.setProduces(produces);
  }

  public void addProduces(String produces) {
    swagger.addProduces(produces);
  }

  public Map<String, Path> getPaths() {
    return swagger.getPaths();
  }

  public void setPaths(Map<String, Path> paths) {
    swagger.setPaths(paths);
  }

  public Path getPath(String path) {
    return swagger.getPath(path);
  }

  public Map<String, SecuritySchemeDefinition> getSecurityDefinitions() {
    return swagger.getSecurityDefinitions();
  }

  public void setSecurityDefinitions(Map<String, SecuritySchemeDefinition> securityDefinitions) {
    swagger.setSecurityDefinitions(securityDefinitions);
  }

  public void addSecurityDefinition(String name, SecuritySchemeDefinition securityDefinition) {
    swagger.addSecurityDefinition(name, securityDefinition);
  }

  public List<SecurityRequirement> getSecurity() {
    return swagger.getSecurity();
  }

  public void setSecurity(List<SecurityRequirement> securityRequirements) {
    swagger.setSecurity(securityRequirements);
  }

  public void addSecurity(SecurityRequirement securityRequirement) {
    swagger.addSecurity(securityRequirement);
  }

  public Map<String, Model> getDefinitions() {
    return swagger.getDefinitions();
  }

  public void setDefinitions(Map<String, Model> definitions) {
    swagger.setDefinitions(definitions);
  }

  public void addDefinition(String key, Model model) {
    swagger.addDefinition(key, model);
  }

  public Map<String, Parameter> getParameters() {
    return swagger.getParameters();
  }

  public void setParameters(Map<String, Parameter> parameters) {
    swagger.setParameters(parameters);
  }

  public Parameter getParameter(String parameter) {
    return swagger.getParameter(parameter);
  }

  public void addParameter(String key, Parameter parameter) {
    swagger.addParameter(key, parameter);
  }

  public Map<String, Response> getResponses() {
    return swagger.getResponses();
  }

  public void setResponses(Map<String, Response> responses) {
    swagger.setResponses(responses);
  }

  public ExternalDocs getExternalDocs() {
    return swagger.getExternalDocs();
  }

  public void setExternalDocs(ExternalDocs value) {
    swagger.setExternalDocs(value);
  }

  public Map<String, Object> getVendorExtensions() {
    return swagger.getVendorExtensions();
  }

  public void setVendorExtension(String name, Object value) {
    swagger.setVendorExtension(name, value);
  }

  public String toString() {
    return swagger.toString();
  }

  public int hashCode() {
    return swagger.hashCode();
  }

  public boolean equals(Object obj) {
    return swagger.equals(obj);
  }

  public Swagger vendorExtensions(Map<String, Object> vendorExtensions) {
    return swagger.vendorExtensions(vendorExtensions);
  }

  public void setVendorExtensions(Map<String, Object> vendorExtensions) {
    swagger.setVendorExtensions(vendorExtensions);
  }

}
