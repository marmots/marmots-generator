package org.marmots.generator.parsers.swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.marmots.generator.exceptions.ValidationException;
import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.model.swagger.SwaggerApplication;
import org.marmots.generator.parsers.GeneratorParser;
import org.marmots.generator.validation.ValidatorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

public class GeneratorSwaggerParser implements GeneratorParser {
  private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorSwaggerParser.class);

  private String url;

  @Override
  public String alias() {
    return "swagger-parser";
  }

  @Override
  public GeneratorContext parse(Properties props) {
    url = props.getProperty(alias() + ".url");
    if (StringUtils.isEmpty(url)) {
      throw new ValidationException("swagger documentation url is required for parsing a SwaggerApplication context");
    }

    Swagger swagger = new SwaggerParser().read(url);
    if (swagger == null) {
      throw new ValidationException(String.format("cannot parse swagger object from %s", url));
    }

    LOGGER.info("swagger parsed: {}", swagger);
    SwaggerApplication application = SwaggerApplication.create(swagger);
    LOGGER.info("application created: {}", application);

    return application;
  }

  @Override
  public void init(Properties props) {
    url = props.getProperty(alias() + ".url");
    if (StringUtils.isEmpty(url)) {
      throw new ValidationException("No url supplied, cannot generate application!!!");
    }
  }

  @Override
  public List<ValidatorMessage> getMessages() {
    return new ArrayList<>();
  }

}
