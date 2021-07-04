package org.marmots.generator.parsers.databasemetadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.marmots.generator.exceptions.CommandException;
import org.marmots.generator.exceptions.GeneratorException;
import org.marmots.generator.exceptions.ValidationException;
import org.marmots.generator.model.databasemetdata.DatabaseMetadataApplication;
import org.marmots.generator.model.databasemetdata.Entity;
import org.marmots.generator.parsers.GeneratorParser;
import org.marmots.generator.utils.LocalizationUtils;
import org.marmots.generator.utils.NameUtils;
import org.marmots.generator.validation.ValidateCatalog;
import org.marmots.generator.validation.ValidatorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.RegularExpressionExclusionRule;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.utility.SchemaCrawlerUtility;

public class DatabaseMetadataParser implements GeneratorParser {
  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMetadataParser.class);
  
  static final Map<String, CompletableFuture<Entity>> FUTURE_ENTITIES = new HashMap<>();
  private static DatabaseMetadataParser instance;

  private Connection connection;
  private Catalog catalog;
  private DatabaseMetadataApplication application;

  private List<ValidatorMessage> messages = new ArrayList<>();

  // TODO fix this ulgy vomitive assignment
  public DatabaseMetadataParser() {
    // remove schemacrawler from logs at all
    java.util.logging.Logger.getLogger("schemacrawler").setLevel(Level.OFF);
    instance = this;
  }

  public static DatabaseMetadataParser getInstance() {
    if (instance == null) {
      instance = new DatabaseMetadataParser();
    }
    return instance;
  }

  public static DatabaseMetadataParser getInstance(String database) {
    // force re-initialization
    instance = new DatabaseMetadataParser();
    return instance;
  }

  @Override
  public String alias() {
    return "dbmd-parser";
  }

  @Override
  public void init(Properties props) {
    String[] requiredProperties = { "driver", "url", "user" };

    for (String property : requiredProperties) {
      if (StringUtils.isEmpty(props.getProperty(alias() + "." + property))) {
        throw new ValidationException(String.format("No required property supplied: %s, cannot generate application!!!", alias() + "." + property));
      }
    }
  }

  public DatabaseMetadataApplication parse(Properties props) throws GeneratorException, CommandException {
    try {
      application = new DatabaseMetadataApplication();

      // Validate required properties
      if (StringUtils.isEmpty(props.getProperty("package"))) {
        throw new ValidationException("required property package not found!");
      }
      application.setPackage(props.getProperty("package"));

      if (StringUtils.isEmpty(props.getProperty("app"))) {
        throw new ValidationException("required property app not found!");
      }
      application.setDatabase(props.getProperty("app"));

      if (StringUtils.isEmpty(props.getProperty(alias() + ".driver"))) {
        throw new ValidationException("required property driver not found!");
      }
      application.setDriver(props.getProperty(alias() + ".driver"));

      if (StringUtils.isEmpty(props.getProperty(alias() + ".url"))) {
        throw new ValidationException("required property url not found!");
      }
      application.setUrl(props.getProperty(alias() + ".url"));

      if (StringUtils.isEmpty(props.getProperty(alias() + ".user"))) {
        throw new ValidationException("required property user not found!");
      }
      application.setUser(props.getProperty(alias() + ".user"));
      application.setPassword(props.getProperty(alias() + ".password"));

      String defaultQueryDepth = props.getProperty(alias() + ".default.query.depth");
      if (!StringUtils.isEmpty(defaultQueryDepth)) {
        application.setDefaultQueryDepth(Integer.parseInt(defaultQueryDepth));
      }

      // Initialize catalog
      catalog = getCatalog(application);

      // validate catalog
      boolean exit = false;
      messages = ValidateCatalog.validate(catalog);
      for (ValidatorMessage message : messages) {
        if (!exit && message.isError()) {
          exit = true;
        }

        if (message.isInfo()) {
          LOGGER.info("Validation information --> {}", message.getMessage());
        } else if (message.isWarn()) {
          LOGGER.warn("Validation warning --> {}", message.getMessage());
        } else if (message.isError()) {
          LOGGER.error("Validation error --> {}", message.getMessage());
        }
      }
      if (exit) {
        String fixes = "";
        for (ValidatorMessage message : messages) {
          if (message.getFix() != null) {
            fixes += message.getFix() + "\n";
          }
        }
        if (fixes.length() > 0) {
          LOGGER.info("\n -- ----------- DATABASE FIXES ----------- -- \n {}", fixes);
        }
        throw new CommandException("There are validations errors on database");
      }

      // Initialize schema (one allowed by the moment)
      final Schema schema = catalog.getSchemas().iterator().next();
      application.setSchema(schema.getFullName());

      application.setClazz(NameUtils.toClassName(schema.getFullName()));
      application.setAttr(NameUtils.toAttributeName(schema.getFullName()));
      application.setSonarProjectKey(NameUtils.toSonarProjectKey(application.getPackage() + ":" + application.getAttr()));

      // Initialize future entities
      for (final Table table : catalog.getTables(schema)) {
        FUTURE_ENTITIES.put(table.getFullName(), new CompletableFuture<>());
      }

      // Initialize entities
      List<Entity> entities = new ArrayList<>();
      for (final Table table : catalog.getTables(schema)) {
        if (isGenerable(table)) {
          Entity entity = EntityFactory.create(application, table);
          entities.add(entity);

          // complete future entity
          FUTURE_ENTITIES.get(table.getFullName()).complete(entity);
        }
      }
      application.setEntities(entities);

      // Initialize localizationEntities
      List<Entity> localizationEntities = new ArrayList<>();
      for (final Table table : catalog.getTables(schema)) {
        if (LocalizationUtils.isLocalizationTable(table)) {
          Entity entity = EntityFactory.create(application, table);
          localizationEntities.add(entity);

          // complete future entity
          FUTURE_ENTITIES.get(table.getFullName()).complete(entity);
        }
      }
      application.setLocalizationEntities(localizationEntities);

      // Main entity
      application.setMainEntity(EntityFactory.create(application, getMainEntity(catalog)));

      return application;
    } catch (SchemaCrawlerException | SQLException | InstantiationException | IllegalAccessException e) {
      throw new GeneratorException(e);
    }
  }

  private Catalog getCatalog(DatabaseMetadataApplication application) throws SchemaCrawlerException, SQLException {
    String database = application.getDatabase();

    // Get a database connection
    connection = application.getConnection();

    // Create the options
    final SchemaCrawlerOptionsBuilder optionsBuilder = new SchemaCrawlerOptionsBuilder();
    final SchemaCrawlerOptions options = optionsBuilder.withSchemaInfoLevel(SchemaInfoLevelBuilder.maximum()).toOptions();

    // Set what details are required in the schema - this affects the time taken to crawl the schema
    options.setRoutineInclusionRule(new ExcludeAll());
    // include all database
    options.setSchemaInclusionRule(new RegularExpressionInclusionRule(database));
    // exclude (spring batch tables|liquibase tables)
    options.setTableInclusionRule(new RegularExpressionExclusionRule(
        "(" + database + "\\.batch_.+|" + database + "\\.databasechangelog|" + database + "\\.hibernate_sequence|" + database + "\\.databasechangeloglock)"));

    // Get the schema definition
    return SchemaCrawlerUtility.getCatalog(connection, options);
  }

  public static boolean isGenerable(Table table) {
    return !table.getTableType().isView() && !LocalizationUtils.isLocalizationTable(table);
  }

  public Table getTable(String tableFullName) {
    for (Schema schema : catalog.getSchemas()) {
      for (Table table : catalog.getTables(schema)) {
        if (tableFullName.equals(table.getFullName())) {
          return table;
        }
      }
    }
    return null;
  }

  public static Table getMainEntity(Catalog catalog) {
    Table main = null;
    for (Table table : catalog.getTables()) {
      if (main == null || main.getColumns().size() + main.getImportedForeignKeys().size() + main.getExportedForeignKeys().size() < table.getColumns().size()
          + table.getImportedForeignKeys().size() + table.getExportedForeignKeys().size()) {
        main = table;
      }
    }
    return main;
  }

  public List<ValidatorMessage> getMessages() {
    return messages;
  }

  public Connection getConnection() {
    return connection;
  }

  public Catalog getCatalog() {
    return catalog;
  }

  public DatabaseMetadataApplication getApplication() {
    return application;
  }
}
