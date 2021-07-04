package org.marmots.generator.utils.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.marmots.generator.configuration.GeneratorConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ClasspathResourceTraverseTest {

  ClassLoader classLoader = ClasspathResourceTraverseTest.class.getClassLoader();

  private void traverse(String location) throws IOException {
    Reflections reflections = new Reflections(
        new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(location)).setScanners(new ResourcesScanner()).filterInputsBy(new FilterBuilder().includePackage(location)));

    Set<String> resources = reflections.getResources(Pattern.compile(".*"));
    for (String resource : resources) {
      System.out.println("-> " + resource);
      InputStream stream = classLoader.getResourceAsStream(resource);
      System.out.println("-----\n" + IOUtils.toString(stream, GeneratorConfiguration.getInstance().getCharset()) + "\n-----");
    }
  }

  @Test
  public void testTraverse() throws Exception {
    System.out.println("===== templates =====");
    traverse("$test-source");

    System.out.println("===== plugins =====");
    traverse("$test-plugins");

    System.out.println("===== macros =====");
    traverse("$test-macros");
  }
}
