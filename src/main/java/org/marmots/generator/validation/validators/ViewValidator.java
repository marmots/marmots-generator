package org.marmots.generator.validation.validators;

import java.util.ArrayList;
import java.util.List;

import org.marmots.generator.validation.Validator;
import org.marmots.generator.validation.ValidatorMessage;
import org.marmots.generator.validation.ValidatorMessage.Type;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Table;

public class ViewValidator implements Validator {

  @Override
  public List<ValidatorMessage> validate(Catalog catalog) {
    List<ValidatorMessage> messages = new ArrayList<ValidatorMessage>();
    for (Table table : catalog.getTables()) {
      if (table.getTableType().isView()) {
        messages.add(ValidatorMessage.create(Type.WARN, table, String.format("views are not supported: %s", table.getFullName())));
      } else if (table.getPrimaryKey() == null) {
        if (table.getImportedForeignKeys().isEmpty() && table.getExportedForeignKeys().isEmpty()) {
          messages.add(ValidatorMessage.create(Type.WARN, table, String.format("tables without primary key are not supported: %s", table.getFullName()),
              String.format("alter table `%s` add column `id` int not null auto_increment first, add primary key (`id`);", table.getFullName())));
        } else {
          messages.add(ValidatorMessage.create(Type.ERROR, table, String.format("tables without primary key are not supported: %s, and it has relationships.", table.getFullName()),
              String.format("alter table `%s` add column `id` int not null auto_increment first, add primary key (`id`);", table.getFullName())));
        }
      }
    }
    return messages;
  }

}
