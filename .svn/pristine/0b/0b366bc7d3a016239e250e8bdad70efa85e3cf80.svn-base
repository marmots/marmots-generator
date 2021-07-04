package org.marmots.generator.utils.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.maven.shared.utils.StringUtils;
import org.marmots.generator.configuration.GeneratorConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO retrieve resources from correct class loader
// TODO this ugly $ replacement has to be moved to a method (or class)
public class ResourceBrowser {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBrowser.class);

  private static ClassLoader classLoader = ResourceBrowser.class.getClassLoader();
  private static Map<String, Reflections> locationReflections = new HashMap<>();

  private ResourceBrowser() {
    // hide default constructor
  }

  private static Set<String> traverse(String location) throws IOException {
    return traverse(location, ".*");
  }

  private static Set<String> traverse(String location, String regex) throws IOException {
    Reflections reflections = locationReflections.get(location);
    if (reflections == null) {
      reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(location)).setScanners(new ResourcesScanner())
          .filterInputsBy(new FilterBuilder().includePackage(location)));
      locationReflections.put(location, reflections);
    }

    Set<String> resources = reflections.getResources(Pattern.compile(regex));
    if (LOGGER.isDebugEnabled()) {
      for (String resource : resources) {
        LOGGER.debug("resource -> {}", resource);
        if (LOGGER.isTraceEnabled()) {
          try (InputStream stream = classLoader.getResourceAsStream(resource)) {
            LOGGER.trace("-----\n{}\n-----", IOUtils.toString(stream, GeneratorConfiguration.getInstance().getCharset()));
          }
        }
      }
    }
    return resources;
  }

  public static Set<String> getTemplates(String sourcePackage) throws IOException {
    return traverse(parsePackageName(sourcePackage));
  }

  public static InputStream getTemplate(String template) {
    return classLoader.getResourceAsStream(template);
  }

  public static Set<String> getPlugins(String pluginsPackage) throws IOException {
    Set<String> plugins = new HashSet<>();
    for (String pluginFile : traverse(parsePackageName(pluginsPackage))) {
      pluginFile = pluginFile.substring(pluginsPackage.length());
      plugins.add(pluginFile.substring(0, pluginFile.indexOf('/')));
    }
    return plugins;
  }

  public static Set<String> getPlugin(String pluginsPackage, String pluginName) throws IOException {
    return traverse(parsePackageName(pluginsPackage) + parsePackageName(pluginName));
  }

  public static Set<String> getMacros(String macrosPackage) throws IOException {
    return traverse(parsePackageName(macrosPackage));
  }

  public static Set<String> getExtensions(String pluginsPackage, String extensionsPackage) throws IOException {
    Set<String> extensions = traverse(parsePackageName(extensionsPackage));
    for (String plugin : getPlugins(pluginsPackage)) {
      extensions.addAll(traverse(parsePackageName(pluginsPackage) + plugin + "/" + parsePackageName(extensionsPackage)));
    }
    return extensions;
  }

  private static String parsePackageName(String packageName) {
    if (packageName.indexOf("\\$") == -1 && packageName.indexOf("$") != -1) {
      packageName = StringUtils.replace(packageName, "$", "\\$");
    }
    return packageName;
  }
}
