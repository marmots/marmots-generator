package org.marmots.generator.model.factories;

import java.io.File;
import java.io.PrintWriter;
import java.util.Properties;

import org.junit.Test;
import org.marmots.generator.exceptions.CommandException;
import org.marmots.generator.model.databasemetdata.DatabaseMetadataApplication;
import org.marmots.generator.parsers.databasemetadata.DatabaseMetadataParser;

import com.fasterxml.jackson.databind.ObjectMapper;

// TODO h2 database & assertions
public class ApplicationFactoryTest {

  private ObjectMapper mapper = new ObjectMapper();

  private Properties getProperties(String database) {
    Properties props = new Properties();
    props.put("package", "org.marmots");
    props.put("app", database);
    props.put("dbmd-parser.driver", "com.mysql.jdbc.Driver");
    props.put("dbmd-parser.url", String.format(
        "jdbc:mysql://localhost:3306/%s?useUnicode=yes&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", database));
    props.put("dbmd-parser.user", "root");

    return props;
  }

  private void writeJSON(DatabaseMetadataApplication application) throws Exception {
    File targetDirectory = new File("target/model");
    if (!targetDirectory.exists()) {
      targetDirectory.mkdirs();
    }

    try (PrintWriter out = new PrintWriter(targetDirectory.getPath() + "/" + application.getDatabase() + ".json")) {
      out.println(mapper.writeValueAsString(application));
    }
  }

  @Test
  public void testCreateApplication() throws Exception {
    // TODO asserts
    String[] databases = { "generator_test", "imdb_full", "realestate", "blogger2", "sailing", "reservations" };

    for (String database : databases) {
      try {
        DatabaseMetadataParser databaseMetadataParser = DatabaseMetadataParser.getInstance(database);
        writeJSON(databaseMetadataParser.parse(getProperties(database)));
      } catch (CommandException e) {
        // No problem
      }
    }
  }

}
