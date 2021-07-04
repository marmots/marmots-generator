package org.marmots.generator.plugins;

import org.apache.velocity.context.Context;
import org.marmots.generator.model.GeneratorContext;

/**
 * Extending this class you can add additional initialization to velocity or generator contexts You can use those initializations in your templates.
 * 
 * @author marmots
 *
 * @param <T>
 *          The GeneratorContext you are extending
 */
public abstract class GeneratorContextPlugin<T extends GeneratorContext> {
  private Class<T> clazz;

  /**
   * Constructor, we need to know the class you are extending from
   * 
   * @param clazz the generator context class extended by this plugin
   */
  public GeneratorContextPlugin(Class<T> clazz) {
    this.clazz = clazz;
  }

  /**
   * Custom initializations
   * 
   * @param velocityContext
   *          the velocity context
   * @param generatorContext
   *          the generator context
   */
  public abstract void init(Context velocityContext, T generatorContext);

  /**
   * just a getter for the generic class
   * 
   * @return the generator context you are extending
   */
  public Class<T> getClazz() {
    return clazz;
  }
}
