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
package org.marmots.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.context.Context;
import org.marmots.generator.configuration.GeneratorConfiguration;
import org.marmots.generator.exceptions.CommandException;
import org.marmots.generator.exceptions.GeneratorException;
import org.marmots.generator.exceptions.NotGenerableException;
import org.marmots.generator.model.ChildContext;
import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.parsers.GeneratorParser;
import org.marmots.generator.utils.Extensions;
import org.marmots.generator.utils.GeneratorMetadata;
import org.marmots.generator.utils.beautifier.Beautifier;
import org.marmots.generator.utils.resources.ResourceBrowser;
import org.marmots.generator.validation.ValidatorMessage;
import org.marmots.generator.velocity.VelocityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main generator class.
 * 
 * @author marmots
 */
@EnableAutoConfiguration
@SpringBootApplication
public class Generator implements CommandLineRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(Generator.class);

  private boolean forceRegeneration = false;
  private GeneratorMetadata generatorMetadata;
  private List<String> modifiedFiles;

  private Properties properties = new Properties();
  private GeneratorHelper generatorHelper = new GeneratorHelper();
  private GeneratorParser generatorParser;

  private GeneratorConfiguration config = GeneratorConfiguration.getInstance();

  /**
   * main method
   * 
   * @param args
   *          executable arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(Generator.class, args);
  }

  /**
   * Spring's CommandLineRunner method
   * 
   * @param args
   *          executable arguments
   */
  public void run(String... args) {
    generate(args);
  }

  /**
   * Initilizes command line arguments for Generator application
   * 
   * @return the commons-cli options object
   */
  private Options getCommandLineOptions() {
    // create Options object
    Options options = new Options();

    // optional: package
    options.addOption("p", "package", true, "specify base package");

    // optional: export
    options.addOption("e", "export", false, "export zip file");

    // optional: force
    options.addOption("f", "force", false, "force generation of all files");

    // optional: save
    options.addOption("c", "create", false, "creates a default generator configuration file (generator.properties)");

    return options;
  }

  /**
   * Executes generator using arguments and returns 0 if execution is success -1 otherwise
   * 
   * @param args
   *          the Generator arguments
   * @return 0 if execution has been successful -1 if not (important for unit tests)
   */
  public int generate(String... args) {
    LOGGER.info("Application started...");

    // parse arguments
    CommandLineParser parser = new DefaultParser();
    Options options = getCommandLineOptions();

    try {

      // vars(defaults)
      boolean export = false;

      // load configuration
      properties.putAll(config.read());

      // parse the command line arguments
      CommandLine line = parser.parse(options, args, true);

      // base package
      if (line.hasOption('p')) {
        config.setBasePackage(line.getOptionValue('p'));
        LOGGER.info("base package set to: {}", config.getBasePackage());
      }

      // export
      if (line.hasOption('e')) {
        export = true;
        LOGGER.info("exporting generated project");
      }

      // force
      if (line.hasOption('f')) {
        forceRegeneration = true;
        LOGGER.info("forcing regeneration of all files in project");
      }

      // retrieve generator parser
      LOGGER.info("generator parser set to: {}", config.getGeneratorParser());
      generatorParser = GeneratorHelper.getGeneratorParser(config.getGeneratorParser());

      // create configuration
      if (line.hasOption('c')) {
        LOGGER.info("creating default configuration");
        GeneratorConfiguration.create(line.getArgs().length == 0 ? null : line.getArgs()[0]);
        LOGGER.info("configuration file created, edit to set up your environment");
        System.exit(0);
      }

      // validate generator configuration file existance
      if (!new File(GeneratorConfiguration.getPropertiesFile()).exists()) {
        throw new ParserConfigurationException("missing configuration file, use --create to create a new generator application");
      }

      // validate arguments
      if (line.getArgs().length == 0) {
        throw new ParserConfigurationException("missing argument <app>");
      }

      // set app
      String app = line.getArgs()[0];

      // initialize modified files
      modifiedFiles = new ArrayList<>();

      // read metadata to modify only required files
      generatorMetadata = GeneratorMetadata.read(app);

      // generate application
      LOGGER.info("Generating application: {}", app);
      generate(app);

      // beautify
      LOGGER.info("Beautifying sources...");
      Beautifier.getInstance().beautify(modifiedFiles);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("----------------- modified files -----------------\n{}\n----------------- --------------- -----------------", modifiedFiles);
      }
      LOGGER.info("Generation done for app {} ({} files modified/created)", app, modifiedFiles.size());

      // compress output to zip
      if (export) {
        compress(app);
      }

      // save metadata to store generation for next time
      generatorMetadata.save();

      // everything ok
      return 0;

    } catch (ParseException exp) {

      // oops, something went wrong parsing command line
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("generator [options] <app>", options);
      return -1;

    } catch (CommandException e) {

      LOGGER.error("-------------------------------------------------");
      LOGGER.error("EXCEPTION: " + e.getMessage());
      LOGGER.error("-------------------------------------------------");
      return -1;

    } catch (Exception e) {
      LOGGER.error("Exception on main", e);
      return -1;
    }
  }

  /**
   * Executes application generation for 'app'
   * 
   * @param app
   *          the application code (database for dbmd-parser, app code for swagger-parser)
   * @throws GeneratorException
   * @throws ScriptException
   * @throws IOException
   * @throws InvocationTargetException
   * 
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws CommandException
   */
  private void generate(String app)
      throws GeneratorException, IOException, ScriptException, InstantiationException, IllegalAccessException, InvocationTargetException, CommandException {
    LOGGER.info("generating application for app {}", app);

    // Initialize application (swagger)
    properties.put("package", config.getBasePackage());
    properties.put("app", app);

    // init & validate parser configuration
    generatorParser.init(properties);

    // parse generator context
    GeneratorContext application = generatorParser.parse(properties);

    // Load extensions (TODO other dynamic languages)
    if (LOGGER.isDebugEnabled()) {
      Extensions.listScriptEngines();
    }
    Extensions.initEngine(ResourceBrowser.getExtensions(config.getPluginsPackage(), config.getExtensionsPackage()));

    // Process application
    process(application);
  }

  /**
   * Initializes velocity context
   * 
   * @return velocity context initialized
   * @throws IOException
   */
  private Context initContext() throws IOException {
    StringBuilder library = new StringBuilder();
    for (String macro : ResourceBrowser.getMacros(config.getMacrosPackage())) {
      library.append(library.length() == 0 ? "" : ",").append(macro);
    }
    return VelocityHelper.createContext(library.toString());
  }

  /**
   * process GeneratorContext to generate application
   * 
   * @param application
   *          the root GeneratorContext
   * @throws IOException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  private void process(GeneratorContext application) throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
    LOGGER.info("Processing application {}", application.getAttrName());

    Context context = initContext();
    processTemplates(context, application);
    processPlugins(context, application);
  }

  /**
   * Initializes template info and puts in velocity context
   * 
   * @param context
   *          velocity context
   * @param template
   *          source template
   * @param destination
   *          destination file name
   */
  private void initTemplateInfo(Context context, String template, String destination) {
    context.put("template", template);
    // as it is a template it will have GENERATOR_TEMPLATE_EXTENSION (always)
    context.put("destination", FilenameUtils.removeExtension(destination));
  }

  /**
   * Process supplied tempalte using velocity context and generator context
   * 
   * @param template
   *          the template to process
   * @param velocityContext
   *          the velocity context
   * @param generatorContext
   *          the generator context
   * @throws IOException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws NotGenerableException
   */
  private void process(String template, Context velocityContext, GeneratorContext generatorContext)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException, NotGenerableException {
    process(template, template, velocityContext, generatorContext);
  }

  /**
   * Process supplied tempalte using velocity context and generator context (used by plugins)
   * 
   * @param template
   *          the template to process
   * @param templateDestination
   *          the destination filename
   * @param velocityContext
   *          the velocity context
   * @param generatorContext
   *          the generator context
   * @throws IOException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  @SuppressWarnings("unchecked")
  private void process(String template, String templateDestination, Context velocityContext, GeneratorContext generatorContext)
      throws NotGenerableException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
    try {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("traversing: {} - {}", generatorContext.getAttrName(), generatorContext.getInstanceName());
        LOGGER.debug("template: {}", template);
      }

      // context attribute replacements
      velocityContext = generatorHelper.initContext(velocityContext, generatorContext);
      String destination = generatorHelper.parseFileName(templateDestination, velocityContext, generatorContext);
      LOGGER.debug("(parsing...) destination: {}", destination);

      // if template path initializes context
      if (destination.toLowerCase().contains("$")) {

        // traverse
        Method[] contextMethods = generatorContext.getClass().getDeclaredMethods();
        for (Method method : contextMethods) {
          if (method.isAnnotationPresent(ChildContext.class)) {
            if (method.getReturnType().isArray()) {
              for (GeneratorContext context : (GeneratorContext[]) method.invoke(generatorContext)) {
                process(template, destination, velocityContext, context);
              }
            } else if (Collection.class.isAssignableFrom(method.getReturnType())) {
              for (GeneratorContext context : (Collection<GeneratorContext>) method.invoke(generatorContext)) {
                process(template, destination, velocityContext, context);
              }
            } else {
              process(template, destination, velocityContext, (GeneratorContext) method.invoke(generatorContext));
            }
          }
        }

      } else {

        // process template here
        LOGGER.debug(" --------------------------------------------------------------------------------- ");
        LOGGER.info("template: {}", template);
        LOGGER.info("template destination: {}", destination);

        // some debug
        if (LOGGER.isDebugEnabled()) {
          GeneratorContext context = generatorContext;
          while (context != null) {
            LOGGER.debug(" - final generator context: {}, instance: {}", context.getClassName(), context.getInstanceName());
            LOGGER.debug(" - retrieved from velocity context: {}", velocityContext.get(context.getAttrName()));
            context = context.getParent();
          }
        }

        // initialize template information
        initTemplateInfo(velocityContext, template, destination);

        if (config.getTemplateExtension().equals(FilenameUtils.getExtension(template))) {
          // context template
          LOGGER.info("generator template: {}", template);
          applyAndSave(template, FilenameUtils.removeExtension(destination), velocityContext, generatorContext);

        } else if (writeFile(template, destination)) {
          // plain resource
          LOGGER.info("Plain resource: {}", destination);
          copyFile(template, destination);
        }
      }
    } catch (NotGenerableException e) {
      LOGGER.debug("template {} is not generable.", template);
    }
  }

  // new process method using elements from classpath
  private void processTemplates(Context context, GeneratorContext application) throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
    // get source templates
    Set<String> templates = ResourceBrowser.getTemplates(config.getSourcePackage());

    // loop every resource
    for (String template : templates) {
      try {
        process(template, context, application);
      } catch (NotGenerableException e) {
        LOGGER.debug("template {} is not generable.", template);
      }
    }
  }

  private String locatePluginDestination(String name) throws NotGenerableException, IOException {
    for (String template : ResourceBrowser.getTemplates(config.getSourcePackage())) {
      int index = template.indexOf(name);
      if (index != -1) {
        LOGGER.debug("matching template: {}", template);
        return template.substring(0, index);
      }
    }
    throw new NotGenerableException(String.format("mount-point %s not found in source folder", name));
  }

  private void processPlugins(Context context, GeneratorContext application) throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
    for (String plugin : ResourceBrowser.getPlugins(config.getPluginsPackage())) {
      LOGGER.info("processing plugin: {}", plugin);

      for (String pluginFile : ResourceBrowser.getPlugin(config.getPluginsPackage(), plugin)) {
        try {
          String pluginDestination = pluginFile.substring(pluginFile.indexOf(plugin) + plugin.length() + 1);
          String destinationFolder = pluginDestination.substring(0, pluginDestination.indexOf('/'));
          String parsedDestinationFolder = locatePluginDestination(destinationFolder);
          String pluginFinalDestination = parsedDestinationFolder + pluginDestination;

          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("processing plugin file: {}", pluginFile);
            LOGGER.debug("pluginDestination: {}", pluginDestination);
            LOGGER.debug("destinationFolder: {}", destinationFolder);
            LOGGER.debug("parsedDestinationFolder: {}", parsedDestinationFolder);
            LOGGER.debug("pluginFinalDestiation: {}", pluginFinalDestination);
          }

          process(pluginFile, pluginFinalDestination, context, application);

        } catch (NotGenerableException e) {
          LOGGER.debug("plugin template {} is not generable.", pluginFile);
        }
      }
    }
  }

  private boolean writeFile(String template, String destinationFilePath) throws IOException {
    if (forceRegeneration) {
      return true;
    }

    File destination = new File(config.getTargetFolder() + destinationFilePath);
    return !destination.exists() || generatorMetadata.overwriteTemplate(template);
  }

  private void copyFile(String source, String destination) throws IOException {
    if (writeFile(source, destination)) {

      // create folders if they don't exist yet
      File destinationFile = new File(config.getTargetFolder() + destination);
      if (!destinationFile.getParentFile().exists()) {
        destinationFile.getParentFile().mkdirs();
      }

      // copy plain file
      try (FileOutputStream out = new FileOutputStream(config.getTargetFolder() + destination)) {
        IOUtils.copy(ResourceBrowser.getTemplate(source), out);
      }

      // add to modified files
      modifiedFiles.add(config.getTargetFolder() + destination);
    }
  }

  private boolean writeTemplate(String template, String destination, Context velocityContext, GeneratorContext generatorContext) throws IOException {
    if (forceRegeneration) {
      return true;
    }

    boolean exists = new File(config.getTargetFolder() + destination).exists();

    boolean templateUpdated = generatorMetadata.overwriteTemplate(template);

    // detect context changes (generically)
    boolean contextTemplate = velocityContext.get(generatorContext.getAttrName()) != null;
    boolean contextUpdated = velocityContext.get(generatorContext.getAttrName()) != null && generatorMetadata.overwriteContext(generatorContext);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("!exists:{}, templateUpdated:{}, contextTemplate:{}, contextUpdated:{} ", !exists, templateUpdated, contextTemplate, contextUpdated);
    }

    if (!exists || templateUpdated) {
      LOGGER.debug("!exists:{} || templateUpdated:{}", !exists, templateUpdated);
      return true;
    } else if (contextTemplate) {
      LOGGER.debug("contextTemplate: {}", contextUpdated);
      return contextUpdated;

    } else {
      return false;
    }
  }

  private void applyAndSave(String template, String destination, Context velocityContext, GeneratorContext generatorContext) throws IOException {
    // if template needs to be reaplied
    boolean overwrite = writeTemplate(template, destination, velocityContext, generatorContext);
    LOGGER.info("overwrite template: {} to destination {} === {}", template, destination, overwrite);

    if (overwrite) {
      File file = new File(config.getTargetFolder() + destination);
      file.getParentFile().mkdirs();

      try (FileOutputStream out = new FileOutputStream(file)) {
        IOUtils.write(VelocityHelper.apply(template, velocityContext), out, config.getCharset());
      }

      modifiedFiles.add(config.getTargetFolder() + destination);
    }
  }

  // TODO use project root
  private void compress(String app) throws IOException {
    // define variables
    String file = config.getTargetFolder() + "marmots-" + app + ".zip";
    String folder = config.getTargetFolder() + "marmots-" + app + "/";

    try (OutputStream fo = Files.newOutputStream(Paths.get(file))) {
      compress(folder, fo);
    }
  }

  private static void compress(String sourceDir, OutputStream os) throws IOException {
    try (ZipOutputStream zos = new ZipOutputStream(os)) {
      compressDirectory(sourceDir, sourceDir, zos);
    }
  }

  private static void compressDirectory(String rootDir, String sourceDir, ZipOutputStream out) throws IOException {
    File[] fileList = new File(sourceDir).listFiles();
    if (fileList.length == 0) { // empty directory
      ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + "/");
      out.putNextEntry(entry);
      out.closeEntry();
    } else {
      for (File file : fileList) {
        if (file.isDirectory()) {
          compressDirectory(rootDir, sourceDir + File.separator + file.getName(), out);
        } else {
          ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + File.separator + file.getName());
          out.putNextEntry(entry);

          try (FileInputStream in = new FileInputStream(sourceDir + File.separator + file.getName())) {
            IOUtils.copy(in, out);
          }
        }
      }
    }
  }

  public List<ValidatorMessage> getMessages() {
    return generatorParser.getMessages();
  }

  public static String getTargetFolder() {
    return GeneratorConfiguration.getInstance().getTargetFolder();
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

}