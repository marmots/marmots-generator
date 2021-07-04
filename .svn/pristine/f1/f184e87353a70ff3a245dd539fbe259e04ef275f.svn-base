package org.marmots.generator.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.marmots.generator.model.GeneratorContext;
import org.marmots.generator.utils.resources.ResourceBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChecksumUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChecksumUtils.class);

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static String fileChecksum(String template) throws IOException {
    try (InputStream fis = ResourceBrowser.getTemplate(template)) {
      if (fis == null) {
        LOGGER.debug("Template {} get as resource is null", template);
      }
      return DigestUtils.md5Hex(fis);
    }
  }

  public static String objectChecksum(Serializable o) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      oos.writeObject(o);
      return DigestUtils.md5Hex(bos.toByteArray());
    }
  }

  public static String jsonChecksum(Object o) throws IOException {
    String json = MAPPER.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true).writerWithDefaultPrettyPrinter().writeValueAsString(o);
    if (LOGGER.isDebugEnabled()) {
      String filename = "";
      if (o instanceof GeneratorContext) {
        GeneratorContext context = (GeneratorContext) o;
        filename = context.getAttrName() + "-" + context.getInstanceName() + ".json";
        try (FileWriter writer = new FileWriter(GeneratorMetadata.METADATA_FOLDER + filename)) {
          IOUtils.write(json, writer);
        }
      }
    }
    return DigestUtils.md5Hex(json);
  }

}
