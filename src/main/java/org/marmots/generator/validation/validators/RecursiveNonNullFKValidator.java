package org.marmots.generator.validation.validators;

import java.util.ArrayList;
import java.util.List;

import org.marmots.generator.validation.Validator;
import org.marmots.generator.validation.ValidatorMessage;

import schemacrawler.schema.Catalog;

public class RecursiveNonNullFKValidator implements Validator {

  @Override
  public List<ValidatorMessage> validate(Catalog catalog) {
    List<ValidatorMessage> messages = new ArrayList<ValidatorMessage>();

    return messages;
  }

}
