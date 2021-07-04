package org.marmots.generator.model.databasemetdata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.RandomUtils;
import org.marmots.generator.model.ChildContext;
import org.marmots.generator.model.ContextAttribute;
import org.marmots.generator.model.GeneratorContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import schemacrawler.schemacrawler.DatabaseConnectionOptions;

/**
 * Root context for databasemetadata model. <br>
 * Accessible as $application in your templates. For routes, use $application for variable name and $Application for class name.
 * 
 * @author marmots
 */
@JsonPropertyOrder(alphabetic = true)
public class DatabaseMetadataApplication extends GeneratorContext {
  /**
   * generated uid
   */
  private static final long serialVersionUID = 357812084335299820L;

  private static final int DEFAULT_QUERY_DEPTH = 3;

  private String packageName;
  private String database;
  private String driver;
  private String url;
  private String user;
  private String password;

  private int defaultQueryDepth = DEFAULT_QUERY_DEPTH;

  private String schema;

  private String clazz;
  private String attr;
  private String sonarProjectKey;

  private Entity mainEntity;
  private List<Entity> entities;
  private List<Entity> localizationEntities;

  /**
   * package for the application, available as $package in your routes (replaced from . separator to / separator)
   * 
   * @return the package as string
   */
  @ContextAttribute("package")
  public String getPackage() {
    return packageName;
  }

  public void setPackage(String packageName) {
    this.packageName = packageName;
  }

  /**
   * the name of the database as supplied as a generator parameter
   * 
   * @return database name
   */
  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  /**
   * jdbc driver as a string
   * 
   * @return jdbc driver
   */
  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  /**
   * jdbc url as a string (%s is used to replace by database name)
   * 
   * @return jdbc url
   */
  public String getUrl() {
    return url;
  }

  public String getParsedUrl() {
    return String.format(url, database);
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * database username
   * 
   * @return database username
   */
  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  /**
   * database username password
   * 
   * @return database username password
   */
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * default query depth used in SQLUtils to generate select clauses
   * 
   * @return the default query depth
   */
  public int getDefaultQueryDepth() {
    return defaultQueryDepth;
  }

  public void setDefaultQueryDepth(int defaultQueryDepth) {
    this.defaultQueryDepth = defaultQueryDepth;
  }

  /**
   * database name
   * 
   * @return database name
   */
  @Deprecated
  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  /**
   * database name parsed to remove underscores, spaces, etc. and uppercased first (like a java class name) <br>
   * available in your routes as $Application.
   * 
   * @return parsed database name as java class name
   */
  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  /**
   * database name parsed to remove underscores, spaces, etc. and lowercased first (like a java attribute name) <br>
   * available in your routes as $application and as $schema (deprecated)
   * 
   * @return parsed database name as attribute class name
   */
  @ContextAttribute("schema")
  public String getAttr() {
    return attr;
  }

  public void setAttr(String attr) {
    this.attr = attr;
  }

  @ChildContext(value = "entity", context = Entity.class)
  public List<Entity> getEntities() {
    return entities;
  }

  public void setEntities(List<Entity> entities) {
    this.entities = entities;
  }

  public String getSonarProjectKey() {
    return sonarProjectKey;
  }

  public void setSonarProjectKey(String sonarProjectKey) {
    this.sonarProjectKey = sonarProjectKey;
  }

  public Entity getMainEntity() {
    return mainEntity;
  }

  public void setMainEntity(Entity mainEntity) {
    this.mainEntity = mainEntity;
  }

  @JsonIgnore
  public Connection getConnection() throws SQLException {
    String url = getParsedUrl();
    String user = getUser();
    String password = getPassword();

    // Create a database connection
    final DataSource dataSource = new DatabaseConnectionOptions(url);
    return dataSource.getConnection(user, password);
  }

  public List<Entity> getLocalizationEntities() {
    return localizationEntities;
  }

  public void setLocalizationEntities(List<Entity> localizationEntities) {
    this.localizationEntities = localizationEntities;
  }

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
    return attr;
  }

  @Override
  public String getInstanceClassName() {
    return clazz;
  }

  @Override
  public String toString() {
    return getInstanceName();
  }
  
  /**
   * Generates a random long
   * @return a random long
   */
  public static Long generateSerialVersionUID() {
    return RandomUtils.nextLong();
  }

}
