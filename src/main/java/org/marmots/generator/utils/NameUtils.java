package org.marmots.generator.utils;

import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.marmots.generator.utils.naming.Inflector;

import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.realiser.english.Realiser;

// TODO DESIGN DECISION: try to convert from column to java with reverse possibility vs. force alter database on validation
// TODO pluralize/singularize llibrary -> coreNLP
public class NameUtils {
  private static final Lexicon LEXICON = Lexicon.getDefaultLexicon();
  private static final NLGFactory NLG_FACTORY = new NLGFactory(LEXICON);
  private static final Realiser REALISER = new Realiser(LEXICON);

  private NameUtils() {
    // hide default constructor
  }

  // TODO it would be nicer to respect case (but fails on UPPERCASE fields)
  public static String fixAttributeName(String attr) {
    return attr.toLowerCase().replaceAll("`", "");
  }

  private static String toLanguageName(String s, boolean upperCaseFirst) {
    StringBuilder builder = new StringBuilder();
    String attr = fixAttributeName(s);
    StringTokenizer tokenizer = new StringTokenizer(Character.toLowerCase(attr.charAt(0)) + attr.substring(1), "_-");
    while (tokenizer.hasMoreTokens()) {
      if (StringUtils.isEmpty(builder.toString()) && !upperCaseFirst) {
        builder.append(tokenizer.nextToken());
      } else {
        builder.append(StringUtils.capitalize(tokenizer.nextToken()));
      }
    }
    return builder.toString();
  }

  public static String toClassName(String s) {
    return toLanguageName(s, true);
  }

  public static String toAttributeName(String s) {
    return toLanguageName(s, false);
  }

  public static String toAngularTag(String s) {
    return s.replaceAll("([A-Z])", "-$1").toLowerCase();
  }

  public static String toSonarProjectKey(String s) {
    String key = fixAttributeName(s);
    return StringUtils.replace(key, ".", ":");
  }

  public static String toEnglishPlural(String s) {
    NPPhraseSpec subject = NLG_FACTORY.createNounPhrase(s);
    if (!subject.isPlural()) {
      subject.setPlural(true);
    }
    String plural = REALISER.realiseSentence(subject);
    return plural.substring(0, plural.length() - 1);
  }

  public static String toEnglishSingular(String s) {
    NPPhraseSpec subject = NLG_FACTORY.createNounPhrase(s);
    if (subject.isPlural()) {
      subject.setPlural(false);
    }
    String singular = REALISER.realiseSentence(subject);
    return singular.substring(0, singular.length() - 1);
  }

  public static String humanize(String s) {
    return Inflector.getInstance().humanize(s, "`");
  }

  public static String pluralize(String s) {
    return Inflector.getInstance().pluralize(humanize(s));
  }

  public static String singularize(String s) {
    return Inflector.getInstance().singularize(humanize(s));
  }

  public static String toLabel(String s, boolean capitalize) {
    String label = fixAttributeName(s);
    return capitalize ? StringUtils.capitalize(label) : label.toLowerCase();
  }

}
