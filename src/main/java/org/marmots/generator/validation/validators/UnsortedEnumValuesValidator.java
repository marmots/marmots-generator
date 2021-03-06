package org.marmots.generator.validation.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.marmots.generator.utils.EntityUtils;
import org.marmots.generator.utils.TypeUtils;
import org.marmots.generator.validation.Validator;
import org.marmots.generator.validation.ValidatorMessage;
import org.marmots.generator.validation.ValidatorMessage.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;

public class UnsortedEnumValuesValidator implements Validator {
  private static final Logger LOGGER = LoggerFactory.getLogger(UnsortedEnumValuesValidator.class);

  @Override
  public List<ValidatorMessage> validate(Catalog catalog) {
    List<ValidatorMessage> messages = new ArrayList<ValidatorMessage>();
    for (Table table : catalog.getTables()) {
      if (!table.getTableType().isView() && table.getPrimaryKey() != null) {
        for (Column column : table.getColumns()) {
          if (TypeUtils.isEnum(column.getColumnDataType())) {
            try {
              List<String> values = EntityUtils.getEnumValues(column);
              String prev = null;
              for (String value : values) {
                if (prev != null && prev.compareTo(value) > 0) {
                  Collections.sort(values);
                  String sortedValues = "'" + Arrays.stream(values.toArray(new String[values.size()])).collect(Collectors.joining("', '")) + "'";
                  messages.add(ValidatorMessage.create(Type.ERROR, column, String.format("Unsorted values on %s's ENUM type", column.getName()),
                      String.format("alter table `%s` change column `%s` `%s` %s(%s) " + (column.isNullable() ? "" : "NOT") + " NULL ;", table.getName(), column.getName(),
                          column.getName(), column.getColumnDataType().getName(), sortedValues)));
                }
                prev = value;
              }
            } catch (Exception e) {
              LOGGER.error("Exception getting enum values", e);
            }
          }
        }
      }
    }
    return messages;
  }

}
