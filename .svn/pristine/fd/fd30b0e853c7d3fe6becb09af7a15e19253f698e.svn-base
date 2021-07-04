package org.marmots.generator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.context.Context;
import org.marmots.generator.configuration.GeneratorConfiguration;
import org.marmots.generator.exceptions.NotGenerableException;
import org.marmots.generator.exceptions.ValidationException;
import org.marmots.generator.model.ChildContext;
import org.marmots.generator.model.ContextAttribute;
import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.parsers.GeneratorParser;
import org.marmots.generator.plugins.GeneratorContextPlugin;
import org.marmots.generator.utils.resources.ResourceBrowser;
import org.marmots.generator.velocity.VelocityHelper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneratorHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorHelper.class);

  private static final Reflections reflections = new Reflections();

  public static GeneratorParser getGeneratorParser(String alias) throws InstantiationException, IllegalAccessException {
    Set<Class<? extends GeneratorParser>> parsers = reflections.getSubTypesOf(GeneratorParser.class);
    for (Class<? extends GeneratorParser> parserClass : parsers) {
      if (!Modifier.isAbstract(parserClass.getModifiers())) {
        GeneratorParser parser = parserClass.newInstance();
        if (alias.equals(parser.alias())) {
          return parser;
        }
      }
    }
    throw new ValidationException(String.format("Generator parser with alias %s not found", alias));
  }

  private Context initVelocityContext() throws IOException {
    StringBuilder library = new StringBuilder();
    for (String macro : ResourceBrowser.getMacros(GeneratorConfiguration.getInstance().getMacrosPackage())) {
      library.append(library.toString().isEmpty() ? "" : ",").append(macro);
    }
    return VelocityHelper.createContext(library.toString());
  }

  private void removeChildContexts(Context velocityContext, Class<? extends GeneratorContext> generatorContextClass) {
    Method[] contextMethods = generatorContextClass.getDeclaredMethods();
    for (Method method : contextMethods) {
      if (method.isAnnotationPresent(ChildContext.class)) {

        // remove child context(s)
        ChildContext childContext = method.getAnnotation(ChildContext.class);
        velocityContext.remove(childContext.value());
        removeChildContexts(velocityContext, childContext.context());

      } else if (method.isAnnotationPresent(ContextAttribute.class)) {

        // remove context attribute(s)
        ContextAttribute contextAttribute = method.getAnnotation(ContextAttribute.class);
        velocityContext.remove(contextAttribute.value());

      }
    }

  }

  public Context initContext(GeneratorContext generatorContext)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
    return initContext(null, generatorContext);
  }

  public Context initContext(Context velocityContext, GeneratorContext generatorContext)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
    // Init velocity context if it's null
    if (velocityContext == null) {
      velocityContext = initVelocityContext();
    }

    // remove child contexts
    removeChildContexts(velocityContext, generatorContext.getClass());

    // initialize context attribute
    velocityContext.put(generatorContext.getAttrName(), generatorContext);

    // initialize context attributes
    Method[] contextMethods = generatorContext.getClass().getDeclaredMethods();
    for (Method method : contextMethods) {
      if (method.isAnnotationPresent(ContextAttribute.class)) {
        ContextAttribute contextAttribute = method.getAnnotation(ContextAttribute.class);
        velocityContext.put(contextAttribute.value(), method.invoke(generatorContext));
      }
    }

    // TODO plugins: can this be changed to accept generics?
    @SuppressWarnings("rawtypes")
    Set<Class<? extends GeneratorContextPlugin>> plugins = reflections.getSubTypesOf(GeneratorContextPlugin.class);
    for (@SuppressWarnings("rawtypes")
    Class<? extends GeneratorContextPlugin> plugin : plugins) {
      if (!Modifier.isAbstract(plugin.getModifiers())) {
        @SuppressWarnings("unchecked")
        GeneratorContextPlugin<GeneratorContext> instance = plugin.newInstance();
        if (instance.getClazz().equals(generatorContext.getClass())) {
          instance.init(velocityContext, generatorContext);
        }
      }
    }

    return velocityContext;
  }

  public GeneratorContext getChildestContext(String template, GeneratorContext rootContext) throws InstantiationException, IllegalAccessException {
    if (template.toLowerCase().contains("$" + rootContext.getAttrName().toLowerCase())) {
      // context attribute replacements
      Method[] contextMethods = rootContext.getClass().getDeclaredMethods();
      for (Method method : contextMethods) {
        if (method.isAnnotationPresent(ChildContext.class)) {
          // Has child context
          ChildContext childContext = method.getAnnotation(ChildContext.class);
          GeneratorContext context = getChildestContext(template, childContext.context().newInstance());
          if (context != null) {
            rootContext = context;
          }
        }
      }
      return rootContext;
    }
    return null;
  }

  public void process(String template, Context velocityContext, GeneratorContext generatorContext) throws Exception {
    process(template, null, velocityContext, generatorContext);
  }

  @SuppressWarnings("unchecked")
  public void process(String template, String destination, Context velocityContext, GeneratorContext generatorContext) throws Exception {
    LOGGER.debug("traversing: {} - {}", generatorContext.getAttrName(), generatorContext.getInstanceName());
    LOGGER.debug("template: {}", template);

    // context attribute replacements
    velocityContext = initContext(velocityContext, generatorContext);
    if (destination == null) {
      destination = parseFileName(template, velocityContext, generatorContext);
    }
    LOGGER.debug("(parsing...) destination: {}", destination);

    // if template path initializes context
    if (destination.toLowerCase().contains("$")) {

      // traverse
      Method[] contextMethods = generatorContext.getClass().getDeclaredMethods();
      for (Method method : contextMethods) {
        if (method.isAnnotationPresent(ChildContext.class)) {
          if (method.getReturnType().isArray()) {
            for (GeneratorContext context : (GeneratorContext[]) method.invoke(generatorContext)) {
              process(destination, velocityContext, context);
            }
          } else if (Collection.class.isAssignableFrom(method.getReturnType())) {
            for (GeneratorContext context : (Collection<GeneratorContext>) method.invoke(generatorContext)) {
              process(destination, velocityContext, context);
            }
          } else {
            process(destination, velocityContext, (GeneratorContext) method.invoke(generatorContext));
          }
        }
      }

    } else {

      // process template here: moved to Generator class, here this method is only for testing
      LOGGER.debug(" --------------------------------------------------------------------------------- ");
      LOGGER.info("template: {}", template);
      LOGGER.info("template destination: {}", destination);

      if (LOGGER.isDebugEnabled()) {
        GeneratorContext context = generatorContext;
        while (context != null) {
          LOGGER.debug(" - final generator context: {}, instance: {}", context.getClassName(), context.getInstanceName());
          LOGGER.debug(" - retrieved from velocity context: {}", velocityContext.get(context.getAttrName()));
          context = context.getParent();
        }
      }

    }
  }

  public String parseFileName(String template, Context velocityContext, GeneratorContext generatorContext)
      throws NotGenerableException, IllegalAccessException, InvocationTargetException {
    // remove base source package from destination name
    String name;
    if (template.startsWith(GeneratorConfiguration.getInstance().getSourcePackage())) {
      name = template.substring(GeneratorConfiguration.getInstance().getSourcePackage().length());
    } else {
      name = template;
    }

    // parse parent(s) tempalte name
    if (generatorContext.getParent() != null) {
      name = parseFileName(name, velocityContext, generatorContext.getParent());
    }

    // parse conditional expressions
    Pattern pattern = Pattern.compile("\\$(?i)" + generatorContext.getAttrName() + "\\.(\\w+)\\?");
    Matcher matcher = pattern.matcher(name);
    if (matcher.find()) {
      try {
        String expression = matcher.group(1);
        if ((boolean) ConvertUtils.convert(PropertyUtils.getProperty(generatorContext, expression), Boolean.TYPE)) {
          name = StringUtils.replace(name, "$" + generatorContext.getAttrName() + "." + expression + "?", "$" + generatorContext.getAttrName());
          name = StringUtils.replace(name, "$" + generatorContext.getClassName() + "." + expression + "?", "$" + generatorContext.getClassName());
        } else {
          throw new NotGenerableException();
        }
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        // TODO dynamic language support: here we can try to run method via GeneratorContext.run(methodName);
        throw new ValidationException(String.format("Invalid expression: %s on %s", matcher, name));
      }
    }

    // generator context replacement
    name = StringUtils.replace(name, "$" + generatorContext.getAttrName(), generatorContext.getInstanceName());
    name = StringUtils.replace(name, "$" + generatorContext.getClassName(), generatorContext.getInstanceClassName());

    // context attribute replacements
    Method[] contextMethods = generatorContext.getClass().getDeclaredMethods();
    for (Method method : contextMethods) {
      if (method.isAnnotationPresent(ContextAttribute.class)) {
        ContextAttribute contextAttribute = method.getAnnotation(ContextAttribute.class);
        Object value = method.invoke(generatorContext);
        // TODO this is not elegant, it's for the package indivual case (look how to generalize)
        if (value instanceof String && value.toString().contains(".")) {
          value = StringUtils.replace(value.toString(), ".", "/");
        }
        name = StringUtils.replace(name, "$" + contextAttribute.value(), value == null ? "" : value.toString());
      }
    }

    return name;
  }
}
