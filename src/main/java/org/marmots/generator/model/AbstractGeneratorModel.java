package org.marmots.generator.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.marmots.generator.utils.Extensions;

public abstract class AbstractGeneratorModel implements Serializable {
  /**
   * generated uid
   */
  private static final long serialVersionUID = -147101107908325807L;
  private Map<String, Serializable> customAttributes;

  public void putCustomAttribute(String key, Serializable value) {
    if (customAttributes == null) {
      customAttributes = new HashMap<>();
    }
    customAttributes.put(key, value);
  }

  public Object getCustomAttribute(String key) {
    return customAttributes == null ? null : customAttributes.get(key);
  }

  public Map<String, Serializable> getCustomAttributes() {
    return customAttributes;
  }

  public void setCustomAttributes(Map<String, Serializable> customAttributes) {
    this.customAttributes = customAttributes;
  }

  public Object run(String method) throws NoSuchMethodException, ScriptException {
    return Extensions.run(method, this);
  }
}
