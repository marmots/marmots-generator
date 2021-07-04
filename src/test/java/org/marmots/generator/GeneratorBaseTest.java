package org.marmots.generator;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.utils.StringUtils;
import org.marmots.generator.utils.NameUtils;
import org.marmots.generator.validation.ValidatorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GeneratorBaseTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorBaseTest.class);

  protected abstract String pomFile();

  protected void testDatabase(String database) throws InterruptedException {
    testDatabase(database, 0, new ArrayList<>());
  }

  protected void testDatabase(String database, int expect, List<String> messages) throws InterruptedException {
    LOGGER.info("Generating application for database: {}...", database);

    // create generator
    Generator generator = new Generator();

    // initialize properties
    Properties properties = new Properties();
    properties.put("dbmd-parser.driver", "com.mysql.jdbc.Driver");
    String url = String
        .format("jdbc:mysql://localhost:3306/%s?useUnicode=yes&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", database);
    properties.put("dbmd-parser.url", url);
    properties.put("dbmd-parser.user", "root");

    // set properties
    generator.setProperties(properties);

    // generate application
    assertTrue("generation exited with error (-1)", generator.generate(new String[] { database }) == expect);
    for (String message : messages) {
      boolean contains = false;
      for (ValidatorMessage vm : generator.getMessages()) {
        if (vm.getMessage().indexOf(message) != -1) {
          contains = true;
          break;
        }
      }
      assertTrue("Expected message {} not contained in return messages", contains);
    }

    // If expected result is to fail return so we don't want to run 'mvn test'
    if (expect == -1) {
      return;
    }

    try {
      LOGGER.info("Running mvn test on {}...", database);
      runMvnInstall(database);
      LOGGER.info("mvn test on {} done.", database);
    } catch (MavenInvocationException e) {
      LOGGER.error("Exception on {} generation", database, e);
    }
  }

  private void runMvnInstall(String database) throws MavenInvocationException {
    InvocationRequest request = new DefaultInvocationRequest();
    request.setPomFile(new File(StringUtils.replace(Generator.getTargetFolder() + pomFile(), "$schema", NameUtils.toAttributeName(database))));
    request.setShellEnvironmentInherited(true);
    request.setBatchMode(true);
    Properties props = new Properties();
    props.put("contiperf.active", "false");
    request.setProperties(props);
    request.setGoals(Collections.singletonList("test"));

    Invoker invoker = new DefaultInvoker();
    invoker.setMavenHome(new File("/usr/share/maven"));
    InvocationResult result = invoker.execute(request);
    assertTrue(database + ":: mvn test has finished with failures", result.getExitCode() == 0);
  }

}
