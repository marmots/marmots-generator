package org.marmots.generator.utils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.marmots.generator.configuration.GeneratorConfiguration;
import org.marmots.generator.utils.resources.ResourceBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO dynamic language support (add others)
public class Extensions {
  private static final Logger LOGGER = LoggerFactory.getLogger(Extensions.class);

  private static Invocable extensionPointEngine;

  public static void listScriptEngines() {
    ScriptEngineManager mgr = new ScriptEngineManager();
    List<ScriptEngineFactory> factories = mgr.getEngineFactories();
    for (ScriptEngineFactory factory : factories) {
      String engName = factory.getEngineName();
      String engVersion = factory.getEngineVersion();
      String langName = factory.getLanguageName();
      String langVersion = factory.getLanguageVersion();
      List<String> engNames = factory.getNames();
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("ScriptEngineFactory Info:");
        LOGGER.debug("\tScript Engine: {} ({})", engName, engVersion);
        for (String name : engNames) {
          LOGGER.debug("\tEngine Alias: {}", name);
        }
        LOGGER.debug("\tLanguage: {} ({})", langName, langVersion);
      }
    }
  }

  public static Invocable initEngine(Set<String> files) throws IOException, ScriptException {
    if (extensionPointEngine == null) {
      ScriptEngineManager manager = new ScriptEngineManager();
      ScriptEngine engine = manager.getEngineByName("JavaScript");

      // JavaScript code in a String
      String script = "";
      for (String file : files) {
        script += IOUtils.toString(ResourceBrowser.getTemplate(file), GeneratorConfiguration.getInstance().getCharset()) + "; \n";
      }

      // evaluate script
      if (!StringUtils.isEmpty(script)) {
        engine.eval(script);
      }

      // javax.script.Invocable is an optional interface.
      // Check whether your script engine implements or not!
      // Note that the JavaScript engine implements Invocable interface.
      extensionPointEngine = (Invocable) engine;
    }
    return extensionPointEngine;
  }

  public static Object run(String method, Object... args) throws ScriptException, NoSuchMethodException {
    // invoke the global function named method
    return extensionPointEngine.invokeFunction(method, args);
  }
}
