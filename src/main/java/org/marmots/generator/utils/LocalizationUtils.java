package org.marmots.generator.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.Table;

public class LocalizationUtils {

  private LocalizationUtils() {
    // hide default constructor
  }

  public static boolean isLocalizationTable(Table table) {
    return table.getName().endsWith("_tr");
  }

  public static boolean isLocalized(Column column) {
    return column.getParent().getName().endsWith("_tr");
  }

  public static boolean hasLocalizationTable(Table table) {
    return getLocalizationTable(table) != null;
  }

  public static Table getLocalizationTable(Table table) {
    if (!table.getExportedForeignKeys().isEmpty()) {
      for (ForeignKey fk : table.getExportedForeignKeys()) {
        for (ColumnReference cr : fk.getColumnReferences()) {
          if (cr.getForeignKeyColumn().getParent().getName().equals(table.getName() + "_tr")) {
            return cr.getForeignKeyColumn().getParent();
          }
        }
      }
    }
    return null; // lleig
  }

  public static Set<Column> getLocalizedColumns(Table table) {
    Set<Column> columns = new LinkedHashSet<>();
    Table localizationTable = getLocalizationTable(table);
    if (localizationTable != null) {
      for (Column column : localizationTable.getColumns()) {
        if (!NameUtils.fixAttributeName(column.getName()).equals("language") && !table.getName().equals(column.getName())) {
          column.setAttribute("localized", true);
          columns.add(column);
        }
      }
    }
    return columns;
  }

}
