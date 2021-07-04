package org.marmots.generator.parsers;

import java.util.List;
import java.util.Properties;

import org.marmots.generator.exceptions.CommandException;
import org.marmots.generator.exceptions.GeneratorException;
import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.validation.ValidatorMessage;

public interface GeneratorParser {
  /**
   * unique name of the parser, used as command line parameter -P, --parser &lt;alias&gt;
   * 
   * @return unique name of the parser
   */
  String alias();

  /**
   * Initializes parser properties and validates
   * 
   * @param properties
   *          parser properties
   */
  void init(Properties properties);

  /**
   * Parses the generator context using supplied properties and returns the model (GeneratorContext) initialized
   * 
   * @param properties
   *          parser properties
   * @return the initialized model (GeneratorContext)
   * @throws CommandException when there are known validation errors parsing
   * @throws GeneratorException any other unknown exception
   */
  GeneratorContext parse(Properties properties) throws GeneratorException, CommandException;

  List<ValidatorMessage> getMessages();
}
