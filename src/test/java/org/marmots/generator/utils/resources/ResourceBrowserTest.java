package org.marmots.generator.utils.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceBrowserTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBrowserTest.class);

  @Test
  public void testGetTemplates() throws Exception {
    Set<String> templates = ResourceBrowser.getTemplates("$test-source/");
    LOGGER.info("templates: {}", templates);
    String[] expected = { "$test-source/$schema-app/file.txt", "$test-source/$schema-app/$entity/$attribute/file.txt", "$test-source/$schema-app/$entity/file.txt" };
    assertEquals(templates.size(), expected.length);
    for (String template : templates) {
      assertTrue(String.format("template %s must be returned", template), ArrayUtils.contains(expected, template));
    }
  }

  @Test
  public void testGetPlugins() throws Exception {
    String pluginsPackage = "$test-plugins/";
    Set<String> plugins = ResourceBrowser.getPlugins(pluginsPackage);
    LOGGER.info("plugins: {}", plugins);
    String[] expected = { "simple-plugin", "marmots-model-plugin" };
    assertEquals(plugins.size(), expected.length);
    for (String plugin : plugins) {
      assertTrue(String.format("plugin %s must be returned", plugin), ArrayUtils.contains(expected, plugin));

      // TODO assert get plugin (files)
      for (String pluginFile : ResourceBrowser.getPlugin(pluginsPackage, plugin)) {
        LOGGER.info("plugin file: {}", pluginFile);
      }
    }
  }

  @Test
  public void testGetMacros() throws Exception {
    Set<String> macros = ResourceBrowser.getMacros("$test-macros/");
    LOGGER.info("macros: {}", macros);
    String[] expected = { "$test-macros/my-velocity-macros.vm" };
    assertEquals(macros.size(), expected.length);
    for (String macro : macros) {
      assertTrue(String.format("macro %s must be returned", macro), ArrayUtils.contains(expected, macro));
    }
  }

  @Test
  public void testGetExtensions() throws Exception {
    Set<String> extensions = ResourceBrowser.getExtensions("$test-plugins/", "$test-extensions/");
    LOGGER.info("extensions: {}", extensions);
    String[] expected = {};
    assertEquals(extensions.size(), expected.length);
    for (String extension : extensions) {
      assertTrue(String.format("extension %s must be returned", extension), ArrayUtils.contains(expected, extension));
    }
  }

}
