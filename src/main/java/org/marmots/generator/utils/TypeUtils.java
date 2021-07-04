package org.marmots.generator.utils;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnDataType;
import schemacrawler.schema.Table;

// TODO move type mappings to configuration using those default values
public class TypeUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(TypeUtils.class);

  private static final String[] NUMERIC_TYPES = { "NUMERIC", "DECIMAL", "DOUBLE", "BIT", "TINYINT", "SMALLINT", "INTEGER", "INT", "BIGINT", "REAL", "FLOAT" };
  private static final String[] STRING_TYPES = { "CHARACTER", "CHAR", "VARCHAR", "LONGVARCHAR", "TEXT", "TINYTEXT", "MEDIUMTEXT", "LONGTEXT" };
  private static final String[] DATE_TYPES = { "DATE", "DATETIME", "TIMESTAMP", "TIME" };
  private static final String[] LONGTEXT_TYPES = { "LONGVARCHAR", "TEXT", "TINYTEXT", "MEDIUMTEXT", "LONGTEXT" };
  private static final String[] BINARY_TYPES = { "BLOB", "LONGBLOB", "BINARY", "VARBINARY", "LONGVARBINARY" };
  private static final String[] ENUM_TYPES = { "ENUM", "SET" };

  private TypeUtils() {
    // hide default constructor
  }

  public static boolean isNumericType(ColumnDataType sqlType) {
    return ArrayUtils.contains(NUMERIC_TYPES, sqlType.getJavaSqlType().getName());
  }

  public static boolean isStringType(ColumnDataType sqlType) {
    return ArrayUtils.contains(STRING_TYPES, sqlType.getJavaSqlType().getName());
  }

  public static boolean isDateType(ColumnDataType sqlType) {
    return ArrayUtils.contains(DATE_TYPES, sqlType.getJavaSqlType().getName());
  }

  public static boolean isBinary(String dataType) {
    return ArrayUtils.contains(BINARY_TYPES, dataType);
  }

  public static boolean isLongText(String dataType) {
    return ArrayUtils.contains(LONGTEXT_TYPES, dataType);
  }

  public static boolean isBinary(ColumnDataType dataType) {
    return isBinary(dataType.getJavaSqlType().getName());
  }

  public static boolean isLongText(ColumnDataType dataType) {
    return isLongText(dataType.getJavaSqlType().getName());
  }

  public static boolean isLongField(ColumnDataType dataType) {
    return isBinary(dataType.getJavaSqlType().getName()) || isLongText(dataType.getJavaSqlType().getName());
  }

  public static boolean isEnum(String dataType) {
    return ArrayUtils.contains(ENUM_TYPES, dataType);
  }

  public static boolean isEnum(ColumnDataType dataType) {
    return isEnum(dataType.getLocalTypeName());
  }

  public static boolean hasLongField(Table table) {
    if (table != null) {
      for (Column column : EntityUtils.getColumns(table)) {
        if (isLongField(column.getColumnDataType())) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean hasBinaryField(Table table) {
    return hasBinaryField(table, false);
  }

  public static boolean hasBinaryField(Table table, boolean includeRelated) {
    if (table != null) {
      for (Column column : EntityUtils.getColumns(table)) {
        if (isBinary(column.getColumnDataType())) {
          return true;
        }
        if (includeRelated) {
          if (column.isPartOfForeignKey()) {
            for (Column c : column.getReferencedColumn().getParent().getColumns()) {
              if (isBinary(c.getColumnDataType())) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  public static boolean hasDateFields(Table table) {
    for (Column column : EntityUtils.getColumns(table)) {
      if (isDateType(column.getColumnDataType())) {
        return true;
      }
    }
    return false;
  }

  public static String getNumericType(ColumnDataType sqlType) throws Exception {
    LOGGER.trace("Getting type for: '{}'", sqlType);
    switch (sqlType.getJavaSqlType().getName()) {
    case "NUMERIC":
    case "DECIMAL":
    case "DOUBLE":
      return "double";
    case "BIT":
    case "TINYINT":
    case "SMALLINT":
      return "short";
    case "INTEGER":
    case "INT":
      return "int";
    case "BIGINT":
      return "long";
    case "REAL":
    case "FLOAT":
      return "float";
    default:
      throw new Exception("unsupported numeric type: " + sqlType);
    }
  }

  public static String toJavaType(ColumnDataType sqlType) {
    LOGGER.trace("Getting type for: '{}'", sqlType);
    switch (sqlType.getJavaSqlType().getName()) {
    case "CHARACTER":
    case "CHAR":
    case "VARCHAR":
    case "NVARCHAR":
    case "LONGVARCHAR":
    case "TEXT":
    case "TINYTEXT":
    case "MEDIUMTEXT":
    case "LONGTEXT":
      return String.class.getName();
    case "NUMERIC":
    case "DECIMAL":
      return BigDecimal.class.getName();
    case "BIT":
      return Boolean.class.getName();
    case "TINYINT":
    case "SMALLINT":
      return Short.class.getName();
    case "INTEGER":
    case "INT":
      return Integer.class.getName();
    case "BIGINT":
      return Long.class.getName();
    case "REAL":
      return Float.class.getName();
    case "FLOAT":
    case "DOUBLE":
      return Double.class.getName();
    case "BLOB":
    case "LONGBLOB":
    case "BINARY":
    case "VARBINARY":
    case "LONGVARBINARY":
      return "byte[]";
    case "DATE":
    case "DATETIME":
    case "TIME":
    case "TIMESTAMP":
      return Date.class.getName();
    default:
      throw new RuntimeException("unsupported database type: " + sqlType);
    }
  }

  public static String toJavascriptType(ColumnDataType sqlType) {
    LOGGER.trace("Getting type for: '{}'", sqlType);
    switch (sqlType.getJavaSqlType().getName()) {
    case "CHARACTER":
    case "CHAR":
    case "VARCHAR":
    case "NVARCHAR":
    case "LONGVARCHAR":
    case "TEXT":
    case "TINYTEXT":
    case "MEDIUMTEXT":
    case "LONGTEXT":
      return "string";
    case "NUMERIC":
    case "DECIMAL":
    case "TINYINT":
    case "SMALLINT":
    case "INTEGER":
    case "INT":
    case "BIGINT":
    case "REAL":
    case "FLOAT":
    case "DOUBLE":
      return "number";
    case "BIT":
      return "boolean";
    case "BLOB":
    case "LONGBLOB":
    case "BINARY":
    case "VARBINARY":
    case "LONGVARBINARY":
      return "any";
    case "DATE":
    case "DATETIME":
    case "TIME":
    case "TIMESTAMP":
      return "Date";
    default:
      return "any";
    }
  }

}
