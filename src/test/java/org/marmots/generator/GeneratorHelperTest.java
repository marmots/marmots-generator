package org.marmots.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.velocity.context.Context;
import org.junit.Test;
import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.model.databasemetdata.Attribute;
import org.marmots.generator.model.databasemetdata.DatabaseMetadataApplication;
import org.marmots.generator.model.databasemetdata.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneratorHelperTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorHelperTest.class);

  private GeneratorHelper generatorHelper = new GeneratorHelper();

  private GeneratorContext init() {
    DatabaseMetadataApplication application = new DatabaseMetadataApplication();
    application.setAttr("realestate");
    application.setClazz("Realestate");
    application.setPackage("org.marmots");

    List<Entity> entities = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Entity entity = new Entity();
      entity.setAttr("entity_" + i);
      entity.setClazz("Entity_" + i);
      entity.setParent(application);
      entities.add(entity);

      List<Attribute> attributes = new ArrayList<>();
      for (int j = 0; j < 8; j++) {
        Attribute attribute = new Attribute();
        attribute.setAttr("attribute_" + j);
        attribute.setClazz("Attribute_" + j);
        attribute.setParent(entity);
        attributes.add(attribute);
      }
      entity.setAttributes(attributes);
    }
    application.setEntities(entities);

    return application;
  }

  @Test
  public void initContextTest() throws Exception {
    GeneratorContext application = init();

    Context applicationContext = generatorHelper.initContext(application);
    DatabaseMetadataApplication app = (DatabaseMetadataApplication) applicationContext.get("application");
    LOGGER.info("application from context: {}", app.getAttr());

    for (Entity entity : app.getEntities()) {
      LOGGER.info("application.entity from application context: {}", entity.getAttr());

      String applicationFileName = "$application/$Application/pepe/puta";
      LOGGER.info("application filename: {}", generatorHelper.parseFileName(applicationFileName, applicationContext, application));

      Context entityContext = generatorHelper.initContext(applicationContext, entity);
      LOGGER.info("entity from entity context: {}", ((Entity) entityContext.get("entity")).getAttr());

      String entityFileName = "$application/$Application/pepe/$Entity/$entity.txt";
      LOGGER.info("entity filename: {}", generatorHelper.parseFileName(entityFileName, entityContext, entity));

      for (Attribute attribute : entity.getAttributes()) {
        LOGGER.info("entity.attribute from context: {}", attribute.getAttr());

        Context attributeContext = generatorHelper.initContext(entityContext, attribute);
        LOGGER.info("attribute from attribute context: {}", ((Attribute) attributeContext.get("attribute")).getAttr());

        String attributeFileName = "$application/$Application/pepe/$Entity/$entity/$Attribute/$attribute.jejo";
        LOGGER.info("attribute filename: {}", generatorHelper.parseFileName(attributeFileName, attributeContext, attribute));
      }
    }
  }

  @Test
  public void loopTemplates() throws Exception {
    Set<String> templates = new HashSet<>();

    templates.add("$Application/$package/$application/pepe/puta");
    templates.add("$application/test.xml");
    templates.add("$application/pepe/$entity.txt");
    templates.add("$application/$Application/random.file");
    templates.add("$application/$package/$Application/pepe/$Entity/$entity/$Attribute/$attribute.jejo");
    templates.add("$application/$Application/pepe/$Entity/$entity/$Attribute/file.jejo");

    GeneratorContext aplication = init();
    Context velocityContext = generatorHelper.initContext(aplication);
    for (String template : templates) {
      generatorHelper.process(template, velocityContext, aplication);
    }
  }

}
