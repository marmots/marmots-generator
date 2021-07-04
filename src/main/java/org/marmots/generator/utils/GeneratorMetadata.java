package org.marmots.generator.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.marmots.generator.model.GeneratorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorMetadata implements Serializable {
  private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorMetadata.class);
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * generated uid
   */
  private static final long serialVersionUID = 3286405364097163836L;

  /**
   * Where to store METADATA files
   */
  public static final String METADATA_FOLDER = ".metadata/";

  private String database;
  private Map<String, String> contexts = new HashMap<>();
  private Map<String, String> templates = new HashMap<>();

  private transient Map<String, String> newContexts = new HashMap<>();
  private transient Map<String, String> newTemplates = new HashMap<>();

  public GeneratorMetadata() {
  }

  public GeneratorMetadata(String database) {
    File metadataFolder = new File(METADATA_FOLDER);
    if (!metadataFolder.exists()) {
      metadataFolder.mkdirs();
    }

    this.database = database;
  }

  public static GeneratorMetadata initTransient(GeneratorMetadata metadata) {
    metadata.newContexts = new HashMap<>();
    metadata.newTemplates = new HashMap<>();
    return metadata;
  }

  public static GeneratorMetadata read(String database) {
    try {
      return initTransient(MAPPER.readValue(new File(METADATA_FOLDER + database + "-metadata.json"), GeneratorMetadata.class));
    } catch (Exception e) {
      LOGGER.warn("can't read metadata file, first execution?");
      return new GeneratorMetadata(database);
    }
  }

  public void save() {
    // set new data
    setContexts(newContexts);
    setTemplates(newTemplates);

    // persist
    try {
      MAPPER.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true).writerWithDefaultPrettyPrinter().writeValue(new File(METADATA_FOLDER + database + "-metadata.json"),
          this);
    } catch (Exception e) {
      LOGGER.error("can't write metadata file", e);
    }
  }

  // Contexts
  private void setContexts(Map<String, String> contexts) {
    this.contexts = contexts;
  }

  public boolean overwriteContext(GeneratorContext context) throws IOException {
    String contextId = context.getAttrName() + ":" + context.getInstanceName();
    String checksum = newContexts.get(contextId);
    if (checksum == null) {
      checksum = ChecksumUtils.jsonChecksum(context);
      newContexts.put(contextId, checksum);
    }
    return !checksum.equals(contexts.get(contextId));
  }

  // Templates
  private void setTemplates(Map<String, String> templates) {
    this.templates = templates;
  }

  public boolean overwriteTemplate(String templatePath) throws IOException {
    String checksum = ChecksumUtils.fileChecksum(templatePath);
    try {
      return !checksum.equals(templates.get(templatePath));
    } finally {
      newTemplates.put(templatePath, checksum);
    }
  }

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public Map<String, String> getEntities() {
    return contexts;
  }

  public Map<String, String> getTemplates() {
    return templates;
  }

}
