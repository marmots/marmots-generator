package org.marmots.generator.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO assertions
public class NameUtilsTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(NameUtilsTest.class);

  private static final String SENTENCE = "How many English words do you know With this test you get a valid estimate of your English vocabulary size within 4 minutes and you help scientific research";
  private static final String[] COLUMN_NAMES = { "SystemInformationID", "database_version", "AddressLine1", "class", "protected", "`myAttr`", "my_attr", "`my_attr_rules`",
      "`my attr rules`", "`MyAttrRules`", "`3jejo_rest`" };

  @Test
  public void testFixAttributeName() {
    for (String attr : COLUMN_NAMES) {
      LOGGER.info("fixAttributeName({}): {}", attr, NameUtils.fixAttributeName(attr));
    }
  }

  @Test
  public void testToClassName() {
    for (String attr : COLUMN_NAMES) {
      LOGGER.info("toClassName({}): {}", attr, NameUtils.toClassName(attr));
    }
  }

  @Test
  public void testToVariableName() {
    for (String attr : COLUMN_NAMES) {
      LOGGER.info("toVariableName({}): {}", attr, NameUtils.toAttributeName(attr));
    }
  }

  @Test
  public void testToAngularTag() {
    for (String attr : COLUMN_NAMES) {
      LOGGER.info("toAngularTag({}): {}", attr, NameUtils.toAngularTag(attr));
    }
  }

  @Test
  public void testToSonarProjectKey() {
    for (String attr : COLUMN_NAMES) {
      LOGGER.info("toSonarProjectKey({}): {}", attr, NameUtils.toSonarProjectKey(attr));
    }
  }

  @Test
  public void testToEnglishSingular() {
    String[] words = SENTENCE.split("\\s+");
    for (String word : words) {
      LOGGER.info("toSingular({}): {}", word, NameUtils.toEnglishSingular(word));
    }
  }

  @Test
  public void testToEnglishPlural() {
    String[] words = SENTENCE.split("\\s+");
    for (String word : words) {
      LOGGER.info("toEnglishPlural({}): {}", word, NameUtils.toEnglishPlural(word));
    }
  }

  @Test
  public void testToLabel() {
    for (String attr : COLUMN_NAMES) {
      LOGGER.info("toLabel({}, capitalize): {}", attr, NameUtils.toLabel(attr, true));
      LOGGER.info("toLabel({}, NOT capitalize): {}", attr, NameUtils.toLabel(attr, false));
    }
  }

  @Test
  public void testSingularizePluralize() {
    for (String attr : COLUMN_NAMES) {
      String plural = NameUtils.pluralize(attr);
      String singular = NameUtils.singularize(attr);
      LOGGER.info("pluralize({}): {}", attr, plural);
      LOGGER.info("singularize({}): {}", attr, singular);
      assertTrue("singular and plural can't NEVER be equal", !singular.equals(plural));
    }
  }

}
