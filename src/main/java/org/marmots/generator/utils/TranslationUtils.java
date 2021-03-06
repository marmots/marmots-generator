package org.marmots.generator.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

// TODO google credentials are personal and intransferible (add to configuration)
// TODO disable component if credentials are not provided
@Component
public class TranslationUtils {
  private static final boolean TRANSLATE_ENABLED = false;
  private Translate translate;

  private ObjectMapper objectMapper = new ObjectMapper();

  public TranslationUtils() throws IOException {
    Credentials credentials = GoogleCredentials.fromStream(TranslationUtils.class.getResourceAsStream("/META-INF/marmots-credentials.json"));
    translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
  }

  // translation feature
  private static TranslationUtils TRANSLATION_UTILS;

  public static String translate(String text, String language) throws IOException {
    if (TRANSLATION_UTILS == null) {
      TRANSLATION_UTILS = new TranslationUtils();
    }
    return TRANSLATE_ENABLED ? TRANSLATION_UTILS.translate(text, "en", language) : text;
  }

  public String translate(String text, String source, String destination) {
    Translation translation = translate.translate(text, TranslateOption.sourceLanguage(source), TranslateOption.targetLanguage(destination));
    return translation.getTranslatedText();
  }

  public void translate(File file) throws IOException {
    if (file.isDirectory()) {
      for (File dir : file.listFiles()) {
        translate(dir);
      }
    } else if ("json".equals(FilenameUtils.getExtension(file.getName()))) {
      System.out.println("----- " + file.getName() + " -----");
      // read json file data to String
      byte[] jsonData = Files.readAllBytes(Paths.get(file.getPath()));

      // read JSON like DOM Parser
      JsonNode rootNode = objectMapper.readTree(jsonData);
      System.out.println(traverse(rootNode).toString());
    }
  }

  private JsonNode traverse(JsonNode rootNode) {
    JsonNode resultNode = objectMapper.createObjectNode();

    Iterator<Map.Entry<String, JsonNode>> nodes = rootNode.fields();
    while (nodes.hasNext()) {
      Map.Entry<String, JsonNode> node = nodes.next();
      if (node.getValue().isObject()) {
        ((ObjectNode) resultNode).set(node.getKey(), traverse(node.getValue()));
      } else {
        ((ObjectNode) resultNode).put(node.getKey(), translate(node.getValue().asText(), "en", "es"));
      }
    }

    return resultNode;
  }

  // move this ugly main method to a JUnit test case
  public static void main(String[] args) throws IOException {
    TranslationUtils t = new TranslationUtils();
    t.translate(new File("/home/marmots/treballs/marmots/generator/workspace/marmots-realestate/src/main/webapp/resources/locales/dev"));
  }
}
