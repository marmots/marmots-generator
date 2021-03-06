package org.marmots.generator.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.ForeignKeyColumnReference;
import schemacrawler.schema.IndexColumn;
import schemacrawler.schema.Table;

public class SQLUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(SQLUtils.class);

  private static final int DEFAULT_QUERY_DEPTH = 3;

  private interface CommandSQL {
    public String execute(String current, ForeignKeyColumnReference reference, String alias, boolean localization);
  }

  public static String selectList(Table table) {
    return selectList(table, DEFAULT_QUERY_DEPTH);
  }

  public static String selectList(Table table, int queryDepth) {
    return select(table, false, false, queryDepth);
  }

  public static String selectDetail(Table table) {
    return selectDetail(table, DEFAULT_QUERY_DEPTH);
  }

  public static String selectDetail(Table table, int queryDepth) {
    return select(table, false, true, queryDepth);
  }

  public static String selectCount(Table table) {
    return selectCount(table, DEFAULT_QUERY_DEPTH);
  }

  public static String selectCount(Table table, int queryDepth) {
    return select(table, true, false, queryDepth);
  }

  public static String insert(Table table) {
    return "insert into " + table.getName() + " (" + getFieldList(table.getColumns()) + ")\nvalues (" + getParameterList(table.getColumns(), false) + ")";
  }

  public static String insertLocalization(Table table) {
    String insert = null;
    Table localizationTable = LocalizationUtils.getLocalizationTable(table);
    if (localizationTable != null) {
      insert = "insert into " + localizationTable.getName() + " (" + getFieldList(localizationTable.getColumns()) + ") \nvalues ("
          + getParameterList(localizationTable.getColumns(), false) + ")\non duplicate key update " + getUpdateFieldList(localizationTable.getColumns(), true);
    }
    return insert;
  }

  public static String update(Table table) {
    String update;
    if (EntityUtils.allAreKeyColumns(table)) {
      update = "update " + table.getName() + " t set " + getWherePK(table.getColumns(), "") + "\nwhere " + getWherePK(EntityUtils.getPKColumns(table), "t");
    } else {
      update = "update " + table.getName() + " t set " + getUpdateFieldList(table.getColumns()) + "\nwhere " + getWherePK(EntityUtils.getPKColumns(table), "t");
    }
    return update;
  }

  public static String updateLocalization(Table table) {
    String update = null;
    Table localizationTable = LocalizationUtils.getLocalizationTable(table);
    if (localizationTable != null) {
      update = "update " + localizationTable.getName() + " t set " + getUpdateFieldList(localizationTable.getColumns()) + "\nwhere "
          + getWherePK(EntityUtils.getPKColumns(localizationTable), "t");
    }
    return update;
  }

  public static String save(Table table) {
    return "insert into " + table.getName() + " (" + getFieldList(table.getColumns(), "", true) + ")\nvalues (" + getParameterList(table.getColumns(), true) + ")\n"
        + "on duplicate key update " + getUpdateFieldList(table.getColumns(), true);
  }

  public static String delete(Table table) {
    return "delete from " + table.getName() + "\nwhere " + getWherePK(EntityUtils.getPKColumns(table));
  }

  public static String select(Table table, boolean count, boolean includeLongFields, int depth) {
    String alias = "t";

    // select
    String sql = "select \n ";

    if (count) {
      // count
      sql += " count(*) ";

    } else {
      // fields
      String fields = "";
      for (Column column : table.getColumns()) {
        if (!TypeUtils.isLongField(column.getColumnDataType()) || includeLongFields) {
          fields += (fields.isEmpty() ? "" : ",") + alias + ".`" + column.getName() + "` as \"" + NameUtils.toAttributeName(column.getName()) + "\"\n";
        }
      }
      sql += fields;

      // localization fields (if present)
      sql += localizationFields(table, alias, includeLongFields);

      sql += traverse(table, alias, depth, !count, new CommandSQL() {
        @Override
        public String execute(String current, ForeignKeyColumnReference reference, String alias, boolean localization) {
          String fields = "";
          for (Column column : reference.getPrimaryKeyColumn().getParent().getColumns()) {
            if (!TypeUtils.isLongField(column.getColumnDataType())) {
              fields += "," + current + ".`" + column.getName() + "` as \"" + current.substring(2).replaceAll("_", ".") + "." + NameUtils.toAttributeName(column.getName())
                  + "\"\n";
            }
          }

          // localization fields (if present)
          if (!count) {
            fields += localizationFields(reference.getPrimaryKeyColumn().getParent(), current, false);
          }
          return fields;
        }
      });
    }

    // from
    sql += " from " + table.getName() + " " + alias + (count ? " /*! use index(primary) */ " : "") + " \n ";

    // localization joins (if present)
    sql += localizationJoins(table, alias);

    // joins
    sql += traverse(table, alias, depth, !count, new CommandSQL() {
      @Override
      public String execute(String current, ForeignKeyColumnReference reference, String alias, boolean localization) {
        String joins = "";

        joins += "left join " + reference.getPrimaryKeyColumn().getParent().getName() + " " + current;
        joins += " on " + alias + "." + reference.getForeignKeyColumn().getName() + " = " + current + "." + reference.getPrimaryKeyColumn().getName() + "\n";

        // localization joins (if present)
        if (localization) {
          joins += localizationJoins(reference.getPrimaryKeyColumn().getParent(), current);
        }

        return joins;
      }
    });

    return sql;
  }

  public static String where(Table table, boolean includeLongFields, int depth) throws Exception {
    String where = "";

    where += traverse(table, "t", depth, true, new CommandSQL() {
      @Override
      public String execute(String current, ForeignKeyColumnReference reference, String alias, boolean localization) {
        String joins = "";

        joins += "left join " + reference.getPrimaryKeyColumn().getParent().getName() + " " + current;
        joins += " on " + alias + "." + reference.getForeignKeyColumn().getName() + " = " + current + "." + reference.getPrimaryKeyColumn().getName() + "\n";

        // localization joins (if present)
        if (localization) {
          joins += localizationWhere(reference.getPrimaryKeyColumn().getParent(), current);
        }

        return joins;
      }
    });

    return where;
  }

  private static String traverse(Table table, String alias, int num, boolean localization, CommandSQL cmnd) {
    String traverse = "";
    if (num > 0) {
      for (ForeignKey foreignKey : table.getImportedForeignKeys()) {
        for (ForeignKeyColumnReference reference : foreignKey.getColumnReferences()) {
          String current = alias + "_" + NameUtils.toAttributeName(reference.getForeignKeyColumn().getName()) + "Object";
          traverse += cmnd.execute(current, reference, alias, localization);
          traverse += traverse(reference.getPrimaryKeyColumn().getParent(), current, --num, localization, cmnd);
        }
      }
    }
    return traverse;
  }

  private static String localizationFields(Table table, String alias, boolean includeLongFields) {
    String fields = "";
    // Localization table
    Table localization = LocalizationUtils.getLocalizationTable(table);
    if (localization != null) {
      for (Column column : localization.getColumns()) {
        if (!"language".equals(column.getName()) && !column.isPartOfForeignKey() && (!TypeUtils.isLongField(column.getColumnDataType()) || includeLongFields)) {
          String colAlias = "t".equals(alias) ? "" : (alias.substring(2).replaceAll("_", ".") + ".");
          fields += "," + alias + "_localized.`" + column.getName() + "` as \"" + colAlias + NameUtils.toAttributeName(column.getName()) + "\"\n";
        }
      }
    }
    return fields;
  }

  private static String localizationJoins(Table table, String alias) {
    // localization
    String joins = "";
    Table localizationTable = LocalizationUtils.getLocalizationTable(table);
    if (localizationTable != null) {
      joins += "left join " + localizationTable.getName() + " " + alias + "_localized ";
      for (ForeignKey foreignKey : localizationTable.getImportedForeignKeys()) {
        for (ColumnReference reference : foreignKey.getColumnReferences()) {
          joins += " on " + alias + "_localized.`" + reference.getForeignKeyColumn().getName() + "` = " + alias + "." + reference.getPrimaryKeyColumn().getName();
        }
      }
      joins += " and " + alias + "_localized.language = :_language\n";
    }
    return joins;
  }

  private static String localizationWhere(Table table, String alias) {
    String where = "";
    // Localization table
    Table localization = LocalizationUtils.getLocalizationTable(table);
    if (localization != null) {
      for (Column column : localization.getColumns()) {
        if (!TypeUtils.isLongField(column.getColumnDataType())) {
          where += "if (" + NameUtils.toAttributeName(table.getName()) + ".get" + NameUtils.toClassName(column.getName()) + "() != null) {\n      where +=\n"
              + "          (where.length() == 0 ? WHERE : AND) + \"" + alias + "." + NameUtils.toAttributeName(column.getName()) + " like :\" + path + \"title \";\n"
              + "      if (params != null) {\n        params.put(path + \"title\", activity.getTitle());\n      }\n    }";
        }
      }
    }
    return where;
  }

  public static String getUpdateFieldList(List<Column> columns) {
    return getUpdateFieldList(columns, false);
  }

  public static String getUpdateFieldList(List<Column> columns, boolean includeKeys) {
    String list = "";
    if (columns != null) {
      for (Column column : columns) {
        if (!column.isPartOfPrimaryKey() || includeKeys) {
          list += (StringUtils.isEmpty(list) ? "" : ", ") + "`" + column.getName() + "` = :" + NameUtils.toAttributeName(column.getName());
        }
      }
    }
    return list;
  }

  public static String getWherePK(Collection<Column> columns) {
    return getWherePK(columns, "");
  }

  public static String getWherePK(Collection<Column> columns, String tableAlias) {
    String list = "";
    if (columns != null) {
      for (Column column : columns) {
        list += (StringUtils.isEmpty(list) ? "" : " and ") + (StringUtils.isEmpty(tableAlias) ? "" : tableAlias + ".") + "`" + column.getName() + "` = :"
            + NameUtils.toAttributeName(column.getName());
      }
    }
    return list;
  }

  public static String getWherePKIndex(Collection<IndexColumn> columns) {
    return getWherePKIndex(columns, "");
  }

  public static String getWherePKIndex(Collection<IndexColumn> columns, String tableAlias) {
    String list = "";
    if (columns != null) {
      for (Column column : columns) {
        list += (StringUtils.isEmpty(list) ? "" : " and ") + (StringUtils.isEmpty(tableAlias) ? "" : tableAlias + ".") + "`" + column.getName() + "` = :"
            + NameUtils.toAttributeName(column.getName());
      }
    }
    return list;
  }

  public static String getJoinedFieldList(Table table) {
    return getJoinedFieldList(table, new ArrayList<>());
  }

  private static String getJoinedFieldList(Table table, List<String> foreignKeys) {
    String fields = "";
    for (ForeignKey foreignKey : table.getImportedForeignKeys()) {
      if (foreignKeys.contains(foreignKey.getName())) {
        continue;
      } else {
        foreignKeys.add(foreignKey.getName());
      }
      for (ForeignKeyColumnReference reference : foreignKey.getColumnReferences()) {
        for (Column column : reference.getPrimaryKeyColumn().getParent().getColumns()) {
          if (!TypeUtils.isLongField(column.getColumnDataType())) {
            fields += "," + foreignKey.getName() + "." + column.getName() + " as " + foreignKey.getName() + "_" + NameUtils.fixAttributeName(column.getName()) + "\n";
          }
        }

        // Localization table
        Table localizationTable = LocalizationUtils.getLocalizationTable(reference.getPrimaryKeyColumn().getParent());
        if (localizationTable != null) {
          for (Column column : localizationTable.getColumns()) {
            fields += "," + foreignKey.getName() + "_localized." + column.getName() + " as " + foreignKey.getName() + "_" + NameUtils.fixAttributeName(column.getName()) + "\n";
          }
        }

        if (!table.getFullName().equals(reference.getPrimaryKeyColumn().getParent().getFullName())) {
          fields += getJoinedFieldList(reference.getPrimaryKeyColumn().getParent(), foreignKeys);
        }
      }
    }

    return fields;
  }

  public static String getTableJoins(Table table) {
    return getTableJoins(table, "t", new ArrayList<>(), false);
  }

  public static String getTableJoins(Table table, String alias, List<String> foreignKeys, boolean isLocalizationTable) {
    String joins = "";

    // Localization table
    Table localizationTable = LocalizationUtils.getLocalizationTable(table);
    if (localizationTable != null) {
      joins += getTableJoins(localizationTable, alias, foreignKeys, true);
    }

    // Impoted foreign keys
    for (ForeignKey foreignKey : table.getImportedForeignKeys()) {
      LOGGER.debug("processing foreign key: {} for alias: {}", foreignKey.getName(), alias);
      if (foreignKeys.contains(foreignKey.getName())) {
        continue;
      } else {
        foreignKeys.add(foreignKey.getName());
      }
      for (ForeignKeyColumnReference reference : foreignKey.getColumnReferences()) {
        if (isLocalizationTable) {
          String join = "left join " + reference.getForeignKeyColumn().getParent().getName() + " " + alias + "_localized";
          join += " on " + alias + "." + reference.getPrimaryKeyColumn().getName() + " = " + alias + "_localized."
              + NameUtils.fixAttributeName(reference.getForeignKeyColumn().getName());
          join += " and " + alias + "_localized.language = :_language\n";
          if (!foreignKeys.contains(alias + "_localized")) {
            joins += join;
          }

        } else {
          joins += "left join " + reference.getPrimaryKeyColumn().getParent().getName() + " " + foreignKey.getName();
          joins += " on " + alias + "." + reference.getForeignKeyColumn().getName() + " = " + foreignKey.getName() + "."
              + NameUtils.fixAttributeName(reference.getPrimaryKeyColumn().getName()) + "\n";

          // Localization table
          localizationTable = LocalizationUtils.getLocalizationTable(reference.getPrimaryKeyColumn().getParent());
          if (localizationTable != null) {
            joins += "left join " + localizationTable.getName() + " " + foreignKey.getName() + "_localized";
            String attr = localizationTable.getImportedForeignKeys().iterator().next().getColumnReferences().iterator().next().getForeignKeyColumn().getName();
            joins += " on " + foreignKey.getName() + "." + reference.getPrimaryKeyColumn().getName() + " = " + foreignKey.getName() + "_localized."
                + NameUtils.fixAttributeName(attr);
            joins += " and " + foreignKey.getName() + "_localized.language = :_language\n";
            foreignKeys.add(foreignKey.getName() + "_localized");
          }

          LOGGER.debug("getting table joins for {}, alias: {}", reference.getPrimaryKeyColumn().getParent(), foreignKey.getName());
          joins += getTableJoins(reference.getPrimaryKeyColumn().getParent(), foreignKey.getName(), foreignKeys, false);
        }
      }
    }

    return joins;
  }

  public static String getResultSetGetter(Column column) {
    String name = NameUtils.fixAttributeName(column.getName());
    String sqlType = column.getColumnDataType().getJavaSqlType().getName();
    LOGGER.debug("Getting type for: '{}' -> {}", column, sqlType);
    switch (sqlType) {
    case "CHARACTER":
    case "CHAR":
    case "VARCHAR":
    case "NVARCHAR":
    case "LONGVARCHAR":
    case "TEXT":
    case "TINYTEXT":
    case "MEDIUMTEXT":
    case "LONGTEXT":
      return "rs.getString(prefix + \"" + name + "\")";
    case "NUMERIC":
    case "DECIMAL":
      return "rs.getBigDecimal(prefix + \"" + name + "\")";
    case "BIT":
      return "rs.getBoolean(prefix + \"" + name + "\")";
    case "TINYINT":
    case "SMALLINT":
      return "rs.getShort(prefix + \"" + name + "\")";
    case "INTEGER":
    case "INT":
      return "rs.getInt(prefix + \"" + name + "\")";
    case "BIGINT":
      return "rs.getLong(prefix + \"" + name + "\")";
    case "REAL":
      return "rs.getFloat(prefix + \"" + name + "\")";
    case "FLOAT":
    case "DOUBLE":
      return "rs.getDouble(prefix + \"" + name + "\")";
    case "BLOB":
    case "LONGBLOB":
    case "BINARY":
    case "VARBINARY":
    case "LONGVARBINARY":
      return "rs.getBytes(prefix + \"" + name + "\")";
    case "DATE":
      return "rs.getDate(prefix + \"" + name + "\")";
    case "DATETIME":
    case "TIME":
    case "TIMESTAMP":
      return "rs.getTimestamp(prefix + \"" + name + "\")";
    default:
      return "rs.getObject(prefix + \"" + name + "\")";
    }
  }

  public static String getJoinedTableFields(Table table) {
    return getJoinedTableFields(table, new ArrayList<>());
  }

  private static String getJoinedTableFields(Table table, List<String> foreignKeys) {
    String fields = "";
    for (ForeignKey foreignKey : table.getImportedForeignKeys()) {
      if (foreignKeys.contains(foreignKey.getName())) {
        continue;
      } else {
        foreignKeys.add(foreignKey.getName());
      }
      for (ForeignKeyColumnReference reference : foreignKey.getColumnReferences()) {
        fields += ", $" + NameUtils.toClassName(reference.getPrimaryKeyColumn().getParent().getName()).toUpperCase() + "_SQL_FIELD_LIST:" + foreignKey.getName() + " \n";
        if (!table.getFullName().equals(reference.getPrimaryKeyColumn().getParent().getFullName())) {
          fields += getJoinedTableFields(reference.getPrimaryKeyColumn().getParent(), foreignKeys);
        }
      }
    }

    return fields;
  }

  public static String getFieldList(List<Column> columns) {
    return getFieldList(columns, "");
  }

  public static String getFieldList(List<Column> columns, String fieldPrefix) {
    return getFieldList(columns, fieldPrefix, false);
  }

  public static String getFieldList(List<Column> columns, boolean includeGenerated) {
    return getFieldList(columns, "", includeGenerated);
  }

  public static String getFieldList(List<Column> columns, String fieldPrefix, boolean includeGenerated) {
    String list = "";
    if (columns != null) {
      for (Column column : columns) {
        if (!column.isAutoIncremented() || includeGenerated) {
          list += (StringUtils.isEmpty(list) ? "" : ", ") + fieldPrefix + "`" + column.getName() + "`";
        }
      }
    }
    return list;
  }

  public static String getParameterList(List<Column> columns, boolean includeGenerated) {
    String list = "";
    if (columns != null) {
      for (Column column : columns) {
        if (!column.isAutoIncremented() || includeGenerated) {
          list += (StringUtils.isEmpty(list) ? "" : ", ") + ":" + NameUtils.toAttributeName(column.getName());
        }
      }
    }
    return list;
  }
}
