/*
 * Copyright 2018 marmots
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.marmots.generator.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationConverter;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.marmots.generator.utils.beautifier.Beautifier;
import org.marmots.generator.utils.beautifier.BeautifierCommand;
import org.marmots.generator.utils.resources.URLClasspathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneratorConfiguration {
  private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorConfiguration.class);

  private static final String PROPERTIES_FILE = "generator.properties";

  private Charset charset = Charset.forName("utf-8");
  private String templateExtension = "gen";
  private String generatorParser = "dbmd-parser";
  private String targetFolder = "target/projects/";
  private String basePackage = "org.marmots";
  private String sourcePackage = "$source/";
  private String extensionsPackage = "$extensions/";
  private String pluginsPackage = "$plugins/";
  private String macrosPackage = "$macros/";

  private static GeneratorConfiguration instance;

  private GeneratorConfiguration() {
    // hide default constructor: all static
  }

  public static GeneratorConfiguration getInstance() {
    if (instance == null) {
      instance = new GeneratorConfiguration();
      instance.read();
    }
    return instance;
  }

  // TODO validations
  public Properties read() {
    Properties properties = new Properties();
    try {
      Configurations configs = new Configurations();
      Configuration config = configs.properties(PROPERTIES_FILE);

      // access configuration properties
      charset = Charset.forName(config.getString("generator.charset", "utf-8"));

      templateExtension = config.getString("generator.template-extension", "gen");

      generatorParser = config.getString("generator.parser", "dbmd-parser");

      targetFolder = config.getString("generator.target-folder", "target/projects/");
      targetFolder = parseConfigurationPath(targetFolder);

      basePackage = config.getString("generator.base-package", "org.marmots");

      sourcePackage = config.getString("generator.source-package", "$source/");
      sourcePackage = parseConfigurationPath(sourcePackage);

      extensionsPackage = config.getString("generator.extensions-package", "$extensions/");
      extensionsPackage = parseConfigurationPath(extensionsPackage);

      pluginsPackage = config.getString("generator.plugins-package", "$plugins/");
      pluginsPackage = parseConfigurationPath(pluginsPackage);

      macrosPackage = config.getString("generator.macros-package", "$macros/");
      macrosPackage = parseConfigurationPath(macrosPackage);

      if (config.containsKey("generator.beautifiers")) {
        List<BeautifierCommand> beautifierCommands = new ArrayList<>();
        for (String beautifierId : config.getList(String.class, "generator.beautifiers")) {
          String command = config.getString("generator.beautifiers." + beautifierId + ".command");
          String extensions = config.getString("generator.beautifiers." + beautifierId + ".extensions");
          beautifierCommands.add(BeautifierCommand.create(beautifierId, command, extensions));
        }
        if (!beautifierCommands.isEmpty()) {
          Beautifier.getInstance().setCommands(beautifierCommands.toArray(new BeautifierCommand[beautifierCommands.size()]));
        }
      }

      properties = ConfigurationConverter.getProperties(config);

    } catch (ConfigurationException e) {
      LOGGER.debug("Exception reading configuration, first time?", e);
    }

    return properties;
  }

  private String parseConfigurationPath(String path) {
    if (!path.endsWith("/")) {
      path += "/";
    }
    return path;
  }

  public static void create(String baseFolder) throws IOException {
    // get base folder
    if (baseFolder == null) {
      baseFolder = ".";
    } else {
      File folder = new File(baseFolder);
      if (!folder.exists()) {
        folder.mkdirs();
      }
    }
    LOGGER.info("creating generator application into {}...", baseFolder);

    // create property file
    try (FileOutputStream output = new FileOutputStream(baseFolder + "/generator.properties")) {
      IOUtils.copy(new URL(null, "classpath:generator.properties.sample", new URLClasspathHandler(ClassLoader.getSystemClassLoader())).openStream(), output);
    }

    // create project template dir
    File templates = new File(baseFolder + "/$source");
    if (!templates.exists()) {
      templates.mkdirs();
    }
  }

  public static String getPropertiesFile() {
    return PROPERTIES_FILE;
  }

  public Charset getCharset() {
    return charset;
  }

  public String getTemplateExtension() {
    return templateExtension;
  }

  public String getGeneratorParser() {
    return generatorParser;
  }

  public String getTargetFolder() {
    return targetFolder;
  }

  public String getBasePackage() {
    return basePackage;
  }

  public String getSourcePackage() {
    return sourcePackage;
  }

  public String getExtensionsPackage() {
    return extensionsPackage;
  }

  public String getPluginsPackage() {
    return pluginsPackage;
  }

  public String getMacrosPackage() {
    return macrosPackage;
  }

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

}
