package org.marmots.generator.validation.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marmots.generator.validation.Validator;
import org.marmots.generator.validation.ValidatorMessage;
import org.marmots.generator.validation.ValidatorMessage.Type;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;

public class VariableNameValidator implements Validator {
  private static String pattern = "^[a-zA-Z_$][a-zA-Z_$0-9]*$";

  private static Map<String, String> numbers = new HashMap<String, String>();
  static {
    numbers.put("0", "zero");
    numbers.put("1", "one");
    numbers.put("2", "two");
    numbers.put("3", "three");
    numbers.put("4", "four");
    numbers.put("5", "five");
    numbers.put("6", "six");
    numbers.put("7", "seven");
    numbers.put("8", "eight");
    numbers.put("9", "nine");
  }

  private static Map<String, String> reservedAttrJavaWords = new HashMap<>();
  static {
    reservedAttrJavaWords.put("abstract", "abstractz");
    reservedAttrJavaWords.put("assert", "assertz");
    reservedAttrJavaWords.put("boolean", "booleanz");
    reservedAttrJavaWords.put("break", "breakz");
    reservedAttrJavaWords.put("byte", "bytez");
    reservedAttrJavaWords.put("case", "casez");
    reservedAttrJavaWords.put("catch", "catchz");
    reservedAttrJavaWords.put("char", "charz");
    reservedAttrJavaWords.put("class", "classz");
    reservedAttrJavaWords.put("const", "constz");
    reservedAttrJavaWords.put("continue", "continuez");
    reservedAttrJavaWords.put("default", "defaultz");
    reservedAttrJavaWords.put("double", "doublez");
    reservedAttrJavaWords.put("do", "doz");
    reservedAttrJavaWords.put("else", "elsez");
    reservedAttrJavaWords.put("enum", "enumz");
    reservedAttrJavaWords.put("extends", "extendsz");
    reservedAttrJavaWords.put("false", "falsez");
    reservedAttrJavaWords.put("final", "finalz");
    reservedAttrJavaWords.put("finally", "finallyz");
    reservedAttrJavaWords.put("float", "floatz");
    reservedAttrJavaWords.put("for", "forz");
    reservedAttrJavaWords.put("goto", "gotoz");
    reservedAttrJavaWords.put("if", "ifz");
    reservedAttrJavaWords.put("implements", "implementsz");
    reservedAttrJavaWords.put("import", "importz");
    reservedAttrJavaWords.put("instanceof", "instanceofz");
    reservedAttrJavaWords.put("int", "intz");
    reservedAttrJavaWords.put("interface", "interfacez");
    reservedAttrJavaWords.put("long", "longz");
    reservedAttrJavaWords.put("native", "nativez");
    reservedAttrJavaWords.put("new", "newz");
    reservedAttrJavaWords.put("null", "nullz");
    reservedAttrJavaWords.put("package", "packagez");
    reservedAttrJavaWords.put("private", "privatez");
    reservedAttrJavaWords.put("protected", "protectedz");
    reservedAttrJavaWords.put("public", "publicz");
    reservedAttrJavaWords.put("return", "returnz");
    reservedAttrJavaWords.put("short", "shortz");
    reservedAttrJavaWords.put("static", "staticz");
    reservedAttrJavaWords.put("strictfp", "strictfpz");
    reservedAttrJavaWords.put("super", "superz");
    reservedAttrJavaWords.put("switch", "switchz");
    reservedAttrJavaWords.put("synchronized", "synchronizedz");
    reservedAttrJavaWords.put("this", "thisz");
    reservedAttrJavaWords.put("throw", "throwz");
    reservedAttrJavaWords.put("throws", "throwsz");
    reservedAttrJavaWords.put("transient", "transientz");
    reservedAttrJavaWords.put("true", "truez");
    reservedAttrJavaWords.put("try", "tryz");
    reservedAttrJavaWords.put("void", "voidz");
    reservedAttrJavaWords.put("volatile", "volatilez");
    reservedAttrJavaWords.put("while", "whilez");
  }

  private String removeDelimiters(String name) {
    return name.toLowerCase().replaceAll("`", "");
  }

  private String fixColumnName(String columnName) {
    String name = columnName.toLowerCase().replaceAll("`", "").replaceAll(" ", "_");
    if (Character.isDigit(name.charAt(0))) {
      name = numbers.get(String.valueOf(name.charAt(0))) + "_" + name.substring(1);
    }
    if (reservedAttrJavaWords.containsKey(name)) {
      name = reservedAttrJavaWords.get(name);
    }
    if (Character.isDigit(name.charAt(0))) {
      name = numbers.get(String.valueOf(name.charAt(0))) + name.substring(1);
    }
    return name;
  }

  @Override
  public List<ValidatorMessage> validate(Catalog catalog) {
    List<ValidatorMessage> messages = new ArrayList<ValidatorMessage>();
    for (Table table : catalog.getTables()) {
      if (table.getTableType().isView()) {
        continue;
      }
      if (!table.getName().matches(pattern)) {
        messages.add(ValidatorMessage.create(Type.ERROR, table, String.format("invalid table name: %s", table.getName()),
            String.format("rename table `%s` to `%s`;", table.getName(), fixColumnName(table.getName()))));
      }
      for (Column column : table.getColumns()) {
        if (!column.getName().matches(pattern) || reservedAttrJavaWords.containsKey(removeDelimiters(column.getName()))) {
          messages.add(ValidatorMessage.create(Type.ERROR, column, String.format("invalid column name: %s", column.getFullName()),
              String.format("alter table `%s` change column `%s` `%s` %s(%d) " + (column.isNullable() ? "" : "NOT") + " NULL ;", table.getName(), column.getName(),
                  fixColumnName(column.getName()), column.getColumnDataType().getName(), column.getSize())));
        }
      }
    }
    return messages;
  }

}
