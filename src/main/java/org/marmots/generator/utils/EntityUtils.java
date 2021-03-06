package org.marmots.generator.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.marmots.generator.parsers.databasemetadata.DatabaseMetadataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.ForeignKeyColumnReference;
import schemacrawler.schema.Index;
import schemacrawler.schema.IndexColumn;
import schemacrawler.schema.Table;

public class EntityUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(EntityUtils.class);

  private EntityUtils() {
    // hide default constructor
  }

  public static boolean hasIncrementedKey(List<IndexColumn> columns) {
    LOGGER.debug("setting incremented key...");
    for (IndexColumn column : columns) {
      LOGGER.debug("incremented column: {} = {}", column, column.isAutoIncremented());
      if (column.isAutoIncremented()) {
        return true;
      }
    }
    return false;
  }

  public static boolean hasExportedForeignKeys(Table table) {
    boolean hasExportedForeignKeys = false;
    if (!table.getExportedForeignKeys().isEmpty()) {
      for (ForeignKey fk : table.getExportedForeignKeys()) {
        fkloop: for (ColumnReference cr : fk.getColumnReferences()) {
          if (!cr.getForeignKeyColumn().getParent().getName().endsWith("_tr")) {
            hasExportedForeignKeys = true;
            break fkloop;
          }
        }
      }
    }
    return hasExportedForeignKeys;
  }

  public static boolean hasImportedForeignKeys(Table table) {
    return !table.getImportedForeignKeys().isEmpty();
  }

  public static boolean hasNotNullableImportedForeignKeys(Table table) {
    for (ForeignKey fk : table.getImportedForeignKeys()) {
      for (ColumnReference cr : fk.getColumnReferences()) {
        if (!cr.getForeignKeyColumn().isNullable()) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean hasForeignKeys(Table table) {
    return hasExportedForeignKeys(table) || hasImportedForeignKeys(table);
  }

  public static Set<Column> getColumns(Table table) {
    Set<Column> columns = new LinkedHashSet<>(table.getColumns());
    columns.addAll(LocalizationUtils.getLocalizedColumns(table));
    return columns;
  }

  public static Set<IndexColumn> getNonTextColumns(List<IndexColumn> indexColumns) {
    Set<IndexColumn> columns = new LinkedHashSet<>();
    for (IndexColumn column : indexColumns) {
      if (!TypeUtils.isLongField(column.getColumnDataType())) {
        columns.add(column);
      }
    }
    return columns;
  }

  public static Set<Column> getPKColumns(Table table) {
    Set<Column> pkcolumns = new LinkedHashSet<>();
    for (Column column : table.getColumns()) {
      if (column.isPartOfPrimaryKey()) {
        pkcolumns.add(column);
      }
    }
    return pkcolumns;
  }

  public static boolean hasAutoIncrementedPKColumns(Table table) {
    Set<Column> pks = getPKColumns(table);
    for (Column pk : pks) {
      if (pk.isAutoIncremented()) {
        return true;
      }
    }
    return false;
  }

  public static Table getMainEntity(Catalog catalog) {
    Table main = null;
    for (Table table : catalog.getTables()) {
      if (main == null || main.getColumns().size() + main.getImportedForeignKeys().size() + main.getExportedForeignKeys().size() < table.getColumns().size()
          + table.getImportedForeignKeys().size() + table.getExportedForeignKeys().size()) {
        main = table;
      }
    }
    return main;
  }

  public static String getPKAttributes(List<IndexColumn> columns) throws Exception {
    return getPKAttributes(columns, false);
  }

  public static String getPKAttributes(List<IndexColumn> columns, boolean pathVariable) throws Exception {
    String attrs = "";
    for (IndexColumn column : columns) {
      attrs += (StringUtils.isEmpty(attrs) ? "" : ", ") + (pathVariable ? "@PathVariable " : "")
          + (pathVariable && TypeUtils.isDateType(column.getColumnDataType()) ? "long" : TypeUtils.toJavaType(column.getColumnDataType())) + " "
          + NameUtils.toAttributeName(column.getName());
    }
    return attrs;
  }

  public static String getPKAttributesCall(List<IndexColumn> columns) {
    return getPKAttributesCall(columns, false);
  }

  public static String getPKAttributesCall(List<IndexColumn> columns, boolean pathVariable) {
    String attrs = "";
    for (IndexColumn column : columns) {
      attrs += (StringUtils.isEmpty(attrs) ? "" : ", ")
          + (pathVariable && TypeUtils.isDateType(column.getColumnDataType()) ? "new java.util.Date(" + NameUtils.toAttributeName(column.getName()) + ")"
              : NameUtils.toAttributeName(column.getName()));
    }
    return attrs;
  }

  public static String getPKGetters(String attr, List<IndexColumn> columns) {
    String getters = "";
    for (IndexColumn column : columns) {
      getters += (StringUtils.isEmpty(getters) ? "" : ", ") + attr + ".get" + NameUtils.toClassName(column.getName()) + "()";
    }
    return getters;
  }

  public static Set<String> listImportedForeignKeyTables(Collection<ForeignKey> foreignKeys) {
    SortedSet<String> fks = new TreeSet<>();
    for (ForeignKey foreignKey : foreignKeys) {
      for (ForeignKeyColumnReference columnReference : foreignKey.getColumnReferences()) {
        fks.add(columnReference.getPrimaryKeyColumn().getParent().getName());
      }
    }
    return fks;
  }

  public static Set<String> listExportedForeignKeyTables(Collection<ForeignKey> foreignKeys) {
    SortedSet<String> fks = new TreeSet<>();
    for (ForeignKey foreignKey : foreignKeys) {
      for (ForeignKeyColumnReference columnReference : foreignKey.getColumnReferences()) {
        if (!columnReference.getForeignKeyColumn().getParent().getName().endsWith("_tr")) {
          fks.add(columnReference.getForeignKeyColumn().getParent().getName());
        }
      }
    }
    return fks;
  }

  public static Set<ForeignKey> getExportedForeignKeys(Table table) {
    SortedSet<ForeignKey> fks = new TreeSet<>();
    for (ForeignKey foreignKey : table.getExportedForeignKeys()) {
      boolean add = true;
      for (ForeignKeyColumnReference columnReference : foreignKey.getColumnReferences()) {
        if (columnReference.getForeignKeyColumn().getParent().getName().endsWith("_tr")) {
          add = false;
          break;
        }
      }
      if (add) {
        fks.add(foreignKey);
      }
    }
    return fks;
  }

  public static Set<Table> getExportedForeignKeyTables(Table table) {
    SortedSet<Table> tables = new TreeSet<>();
    for (ForeignKey foreignKey : getExportedForeignKeys(table)) {
      for (ColumnReference reference : foreignKey.getColumnReferences()) {
        if (!tables.contains(reference.getForeignKeyColumn().getParent())) {
          tables.add(reference.getForeignKeyColumn().getParent());
        }
      }
    }
    return tables;
  }

  /**
   * Cheks if table is a relation table (M:N detection) TODO improve relation table checking
   * 
   * @param table
   *          the table to check against
   * @return boolean if it is a relation table
   */
  public static boolean isRelationTable(Table table) {
    return table.getPrimaryKey() != null && table.getPrimaryKey().getColumns().size() == table.getColumns().size() && primaryKeysEqualsForeignKeys(table);
  }

  public static boolean allAreKeyColumns(Table table) {
    return table.getPrimaryKey().getColumns().size() == table.getColumns().size();
  }

  public static Table getRelatedEntity(Table origin, Table relationTable) {
    if (isRelationTable(relationTable)) {
      for (ForeignKey foreignKey : relationTable.getForeignKeys()) {
        for (ColumnReference reference : foreignKey.getColumnReferences()) {
          if (!origin.equals(reference.getPrimaryKeyColumn().getParent())) {
            return reference.getPrimaryKeyColumn().getParent();
          }
        }
      }
    }
    return null;
  }

  public static boolean primaryKeysEqualsForeignKeys(Table table) {
    if (table.getPrimaryKey() == null) {
      return false;
    }
    for (Column pk : table.getPrimaryKey().getColumns()) {
      if (!pk.isPartOfForeignKey()) {
        return false;
      }
    }
    return true;
  }

  // TODO it don't work
  public static boolean foreignKeyIsUnique(Table table) {
    boolean is = false;

    // get foreign key columns
    List<String> fkcolumns = new ArrayList<>();
    for (ForeignKey foreignKey : table.getImportedForeignKeys()) {
      for (ColumnReference reference : foreignKey.getColumnReferences()) {
        fkcolumns.add(reference.getForeignKeyColumn().getName());
      }
    }

    // find if matches primary key fields
    List<IndexColumn> pkcolumns = table.getPrimaryKey().getColumns();
    if (fkcolumns.size() == pkcolumns.size()) {
      is = true;
      for (Column column : pkcolumns) {
        if (!fkcolumns.contains(column.getName())) {
          is = false;
        }
      }
      if (is) {
        return is;
      }
    }

    // find if matches unique key fields
    for (Index index : table.getIndexes()) {
      if (index.isUnique()) {
        List<IndexColumn> idxcolumns = index.getColumns();
        if (fkcolumns.size() == idxcolumns.size()) {
          is = true;
          for (Column column : idxcolumns) {
            if (!fkcolumns.contains(column.getName())) {
              is = false;
            }
          }
          if (is) {
            return is;
          }
        }
      }
    }

    return is;
  }

  public static boolean isHierarchical(Table table) {
    for (Column column : table.getColumns()) {
      if (column.isPartOfForeignKey() && column.getReferencedColumn().getParent().getFullName().equals(table.getFullName())) {
        return true;
      }
    }
    return false;
  }

  public static boolean isHierarchical(Table table, Column column) {
    return column.isPartOfForeignKey() && column.getReferencedColumn().getParent().getFullName().equals(table.getFullName());
  }

  public static List<Table> getImportedTables(Table table) {
    List<Table> tables = new ArrayList<>();
    for (ForeignKey foreignKey : table.getImportedForeignKeys()) {
      for (ColumnReference reference : foreignKey.getColumnReferences()) {
        Table importedTable = reference.getPrimaryKeyColumn().getParent();
        if (!tables.contains(importedTable)) {
          tables.add(importedTable);
        }
      }
    }
    return tables;
  }

  public static List<Table> getExportedTables(Table table) {
    List<Table> tables = new ArrayList<>();
    for (ForeignKey foreignKey : table.getExportedForeignKeys()) {
      for (ColumnReference reference : foreignKey.getColumnReferences()) {
        Table importedTable = reference.getPrimaryKeyColumn().getParent();
        if (!tables.contains(importedTable)) {
          tables.add(importedTable);
        }
      }
    }
    return tables;
  }

  public static String getTitleField(Table referencedTable) throws Exception {
    for (Column col : referencedTable.getColumns()) {
      if ("title_label".equalsIgnoreCase(col.getRemarks())) {
        return col.getName();
      }
    }
    for (Column col : referencedTable.getColumns()) {
      if (TypeUtils.isStringType(col.getColumnDataType())) {
        return col.getName();
      }
    }
    return referencedTable.getColumns().get(0).getName();
  }

  public static List<String> getEnumValues(Column column) throws Exception {
    String table = column.getParent().getName();
    String sql = "show columns from " + table + " where field = ?";
    try (PreparedStatement stmt = DatabaseMetadataParser.getInstance().getConnection().prepareStatement(sql)) {
      stmt.setString(1, column.getName());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String s = rs.getString("type");
          s = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
          s = StringUtils.replace(s, "'", "");
          return Arrays.asList(s.split("\\s*,\\s*"));
        }
      }
    }
    return null; // lleig!
  }

  public static String parseDefaultValue(Column column) {
    if (column.getColumnDataType().getName().equalsIgnoreCase("bit")) {
      return column.getDefaultValue().contains("0") ? "0" : "1";
    } else if (TypeUtils.isStringType(column.getColumnDataType())) {
      return "'" + column.getDefaultValue() + "'";
    } else if (TypeUtils.isDateType(column.getColumnDataType()) && column.getDefaultValue().contains("-")) {
      return "'" + column.getDefaultValue() + "'";
    } else {
      return column.getDefaultValue();
    }
  }

  public static boolean isPKAutoIncremented(Table table) throws Exception {
    for (Column pk : getPKColumns(table)) {
      if (!pk.isAutoIncremented()) {
        return false;
      }
    }
    return true;
  }

  public static Long generateSerialVersionUID() {
    return RandomUtils.nextLong();
  }
}
