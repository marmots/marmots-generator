package org.marmots.generator.validation;

import java.util.List;

import schemacrawler.schema.Catalog;

// TODO Generalize validators (usage via properties and specifying parser)
public interface Validator {
  List<ValidatorMessage> validate(Catalog catalog);
}
