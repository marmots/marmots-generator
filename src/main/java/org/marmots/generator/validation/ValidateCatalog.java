package org.marmots.generator.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import schemacrawler.schema.Catalog;

public class ValidateCatalog {
  // TODO look for validators in all classpath
  private static final Reflections reflections = new Reflections("org.marmots.generator.validation.validators");

  public static List<ValidatorMessage> validate(Catalog catalog) throws InstantiationException, IllegalAccessException {
    List<ValidatorMessage> messages = new ArrayList<>();
    Set<Class<? extends Validator>> validators = reflections.getSubTypesOf(Validator.class);
    for (Class<? extends Validator> validatorClass : validators) {
      messages.addAll(validatorClass.newInstance().validate(catalog));
    }
    return messages;
  }

}
