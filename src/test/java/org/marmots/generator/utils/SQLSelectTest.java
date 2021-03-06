package org.marmots.generator.utils;

import java.sql.Connection;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.DatabaseConnectionOptions;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.RegularExpressionExclusionRule;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.utility.SchemaCrawlerUtility;

// TODO assertions
public class SQLSelectTest {
  static Catalog catalog;

  static {
    // remove schemacrawler from logs at all
    java.util.logging.Logger.getLogger("schemacrawler").setLevel(Level.OFF);
  }

  private static Catalog getCatalog(String database) throws Exception {
    // Build database url
    String jdbcConnectionUrl = String
        .format("jdbc:mysql://localhost:3306/%s?useUnicode=yes&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", database);

    // Create a database connection
    final DataSource dataSource = new DatabaseConnectionOptions(jdbcConnectionUrl);
    Connection connection = dataSource.getConnection("root", null);

    // Create the options
    final SchemaCrawlerOptionsBuilder optionsBuilder = new SchemaCrawlerOptionsBuilder();
    final SchemaCrawlerOptions options = optionsBuilder.withSchemaInfoLevel(SchemaInfoLevelBuilder.maximum()).toOptions();

    // Set what details are required in the schema - this affects the time taken to crawl the schema
    options.setRoutineInclusionRule(new ExcludeAll());
    // include all database
    options.setSchemaInclusionRule(new RegularExpressionInclusionRule(database));
    // exclude (spring batch tables|liquibase tables)
    options
        .setTableInclusionRule(new RegularExpressionExclusionRule("(" + database + "\\.batch_.+|" + database + "\\.databasechangelog|" + database + "\\.databasechangeloglock)"));

    // Get the schema definition
    return SchemaCrawlerUtility.getCatalog(connection, options);
  }

  @BeforeClass
  public static void setUp() throws Exception {
    catalog = getCatalog("sailing");
  }

  @Test
  public void test() throws Exception {
    System.out.println(SQLUtils.selectDetail(getTable("article")) + ";");
    System.out.println("------------------------------------");
    System.out.println(SQLUtils.selectList(getTable("category")) + ";");
    System.out.println("------------------------------------");
    System.out.println(SQLUtils.selectCount(getTable("image")) + ";");
  }

  private Table getTable(String name) throws Exception {
    for (final Schema schema : catalog.getSchemas()) {
      for (final Table table : catalog.getTables(schema)) {
        if (name.equals(table.getName())) {
          return table;
        }
      }
    }
    throw new Exception(String.format("table %s not found", name));
  }
}
