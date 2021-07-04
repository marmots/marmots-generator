package org.marmots.generator.model;

import java.io.Serializable;

public abstract class GeneratorContext extends AbstractGeneratorModel implements Serializable {
  /**
   * generated uid
   */
  private static final long serialVersionUID = -289568870186065055L;
  private GeneratorContext parent;

  /**
   * returns the unique (avoid collisions) class name for the context (first-capitalized-name)
   * 
   * @return class name for the context
   */
  public abstract String getClassName();

  /**
   * returns the unique (avoid collisions) name for the context ($application, $entity, $attribute)
   * 
   * @return the unique uncollisionable name
   */
  public abstract String getAttrName();

  /**
   * returns the unique name for an instance (database-name, table-name, column-name)
   * 
   * @return the unique name of the instance
   */
  public abstract String getInstanceName();

  public abstract String getInstanceClassName();

  /**
   * returns the parent context (null for root) override for subcontexts
   * @return returns the parent context 
   */
  public GeneratorContext getParent() {
    return parent;
  }

  public void setParent(GeneratorContext parent) {
    this.parent = parent;
  }

}
