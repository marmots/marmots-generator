package org.marmots.generator.velocity;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.generic.ClassTool;
import org.apache.velocity.tools.generic.CollectionTool;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.ContextTool;
import org.apache.velocity.tools.generic.DisplayTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.FieldTool;
import org.apache.velocity.tools.generic.LinkTool;
import org.apache.velocity.tools.generic.LogTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.apache.velocity.tools.generic.RenderTool;
import org.apache.velocity.tools.generic.XmlTool;
import org.marmots.generator.utils.Extensions;
import org.marmots.generator.utils.TranslationUtils;

public class VelocityHelper {
  private static VelocityEngine engine;

  public static VelocityEngine getVelocityEngine() {
    return getVelocityEngine(null);
  }

  public static VelocityEngine getVelocityEngine(String macroLibrary) {
    if (engine == null) {
      engine = new VelocityEngine();
      engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
      engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
      if (!StringUtils.isEmpty(macroLibrary)) {
        engine.setProperty("velocimacro.library", macroLibrary);
      }
      engine.init();
    }
    return engine;
  }

  public static Context createContext(String library) {
    ToolManager manager = new ToolManager();
    manager.setVelocityEngine(getVelocityEngine(library));
    Context context = manager.createContext();

    // velocity generic tools
    context.put("class", new ClassTool());
    context.put("context", new ContextTool());
    context.put("display", new DisplayTool());
    context.put("esc", new EscapeTool());
    context.put("field", new FieldTool());
    context.put("link", new LinkTool());
    context.put("log", new LogTool());
    context.put("math", new MathTool());
    context.put("number", new NumberTool());
    context.put("date", new ComparisonDateTool());
    context.put("render", new RenderTool());
    context.put("collection", new CollectionTool());
    context.put("xml", new XmlTool());

    // generator tools
    context.put("TranslationUtils", TranslationUtils.class);
    context.put("extensions", new Extensions());

    return context;
  }

  public static String apply(String templatePath, Context context) throws IOException {
    Template template = getVelocityEngine().getTemplate(templatePath, "UTF-8");
    try (StringWriter writer = new StringWriter()) {
      template.merge(context, writer);
      return writer.toString();
    }
  }

  public static void test(Context context) {
    StringWriter swOut = new StringWriter();
    String templateStr = "package org.marmots.$schemaName";

    /**
     * Merge data and template
     */
    Velocity.evaluate(context, swOut, "log tag name", templateStr);
    System.out.println(swOut);
  }
}
