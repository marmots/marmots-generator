package org.marmots.generator.model.databasemetdata;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.marmots.generator.exceptions.ValidationException;
import org.marmots.generator.model.GeneratorContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class Attribute extends GeneratorContext implements Comparable<Attribute> {
  /**
   * generated uid
   */
  private static final long serialVersionUID = 7367995672130351966L;

  private static final String[] NUMERIC_TYPES = { "NUMERIC", "DECIMAL", "DOUBLE", "BIT", "TINYINT", "SMALLINT", "INTEGER", "INT", "BIGINT", "REAL", "FLOAT" };
  private static final String[] STRING_TYPES = { "CHARACTER", "CHAR", "VARCHAR", "LONGVARCHAR", "TEXT", "TINYTEXT", "MEDIUMTEXT", "LONGTEXT" };
  private static final String[] DATE_TYPES = { "DATE", "DATETIME", "TIMESTAMP", "TIME" };
  private static final String[] LONGTEXT_TYPES = { "LONGVARCHAR", "TEXT", "TINYTEXT", "MEDIUMTEXT", "LONGTEXT" };
  private static final String[] BINARY_TYPES = { "BLOB", "LONGBLOB", "BINARY", "VARBINARY", "LONGVARBINARY" };
  private static final String[] ENUM_TYPES = { "ENUM", "SET" };

  @JsonIgnore
  private Entity entity;
  private String entityFullName;
  private String column;
  private String columnFullName;
  private String defaultValue;
  private String remarks;

  private String attr;
  private String attrSingular;
  private String attrPlural;
  private String clazz;
  private String clazzSingular;
  private String clazzPlural;
  private String angularTag;
  private String angularTagSingular;
  private String angularTagPlural;
  private String label;
  private String labelSingular;
  private String labelPlural;

  private int size;
  private int decimalDigits;
  private int ordinalPosition;

  private String javaSqlType;
  private String sqlType;
  private String sqlDatabaseType;
  private Class<?> typeMappedClass;
  private String javaType;
  private String javascriptType;

  private boolean nullable;
  private boolean localized;
  private boolean autoIncremented;
  private boolean generated;
  private boolean partOfPrimaryKey;
  private boolean partOfUniqueKey;
  private boolean partOfForeignKey;
  private boolean partOfIndex;

  private List<String> enumValues;

  private Attribute referencedAttribute;

  public boolean isNumericType() {
    return ArrayUtils.contains(NUMERIC_TYPES, getJavaSqlType());
  }

  public boolean isStringType() {
    return ArrayUtils.contains(STRING_TYPES, getJavaSqlType());
  }

  public boolean isDateType() {
    return ArrayUtils.contains(DATE_TYPES, getJavaSqlType());
  }

  public boolean isBinary() {
    return ArrayUtils.contains(BINARY_TYPES, getJavaSqlType());
  }

  public boolean isLongText() {
    return ArrayUtils.contains(LONGTEXT_TYPES, getJavaSqlType());
  }

  public boolean isEnum() {
    return ArrayUtils.contains(ENUM_TYPES, getSqlType());
  }

  public boolean isLongField() {
    return isBinary() || isLongText();
  }

  @JsonIgnore
  public String asNumericType() {
    switch (getJavaSqlType()) {
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
      throw new ValidationException("unsupported numeric type: " + sqlType);
    }
  }

  public String parseDefaultValue() {
    if (getSqlType().equalsIgnoreCase("bit")) {
      return getDefaultValue().contains("0") ? "0" : "1";
    } else if (isStringType()) {
      return "'" + getDefaultValue() + "'";
    } else if (isDateType() && getDefaultValue().contains("-")) {
      return "'" + getDefaultValue() + "'";
    } else {
      return getDefaultValue();
    }
  }

  public Entity getEntity() {
    return entity;
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public String getColumnFullName() {
    return columnFullName;
  }

  public void setColumnFullName(String columnFullName) {
    this.columnFullName = columnFullName;
  }

  public String getAttr() {
    return attr;
  }

  public void setAttr(String attr) {
    this.attr = attr;
  }

  public String getAttrPlural() {
    return attrPlural;
  }

  public void setAttrPlural(String attrPlural) {
    this.attrPlural = attrPlural;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLabelPlural() {
    return labelPlural;
  }

  public void setLabelPlural(String labelPlural) {
    this.labelPlural = labelPlural;
  }

  public boolean isNullable() {
    return nullable;
  }

  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  public boolean isLocalized() {
    return localized;
  }

  public void setLocalized(boolean localized) {
    this.localized = localized;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public boolean isAutoIncremented() {
    return autoIncremented;
  }

  public void setAutoIncremented(boolean autoIncremented) {
    this.autoIncremented = autoIncremented;
  }

  public String getJavaSqlType() {
    return javaSqlType;
  }

  public void setJavaSqlType(String javaSqlType) {
    this.javaSqlType = javaSqlType;
  }

  public String getSqlType() {
    return sqlType;
  }

  public void setSqlType(String sqlType) {
    this.sqlType = sqlType;
  }

  public String getJavaType() {
    return javaType;
  }

  public void setJavaType(String javaType) {
    this.javaType = javaType;
  }

  public String getJavascriptType() {
    return javascriptType;
  }

  public void setJavascriptType(String javascriptType) {
    this.javascriptType = javascriptType;
  }

  public boolean isPartOfPrimaryKey() {
    return partOfPrimaryKey;
  }

  public void setPartOfPrimaryKey(boolean partOfPrimaryKey) {
    this.partOfPrimaryKey = partOfPrimaryKey;
  }

  public boolean isPartOfUniqueKey() {
    return partOfUniqueKey;
  }

  public void setPartOfUniqueKey(boolean partOfUniqueKey) {
    this.partOfUniqueKey = partOfUniqueKey;
  }

  public Attribute getReferencedAttribute() {
    return referencedAttribute;
  }

  public void setReferencedAttribute(Attribute referencedAttribute) {
    this.referencedAttribute = referencedAttribute;
  }

  public boolean isGenerated() {
    return generated;
  }

  public void setGenerated(boolean generated) {
    this.generated = generated;
  }

  public boolean isPartOfForeignKey() {
    return partOfForeignKey;
  }

  public void setPartOfForeignKey(boolean partOfForeignKey) {
    this.partOfForeignKey = partOfForeignKey;
  }

  public boolean isPartOfIndex() {
    return partOfIndex;
  }

  public void setPartOfIndex(boolean partOfIndex) {
    this.partOfIndex = partOfIndex;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public int getDecimalDigits() {
    return decimalDigits;
  }

  public void setDecimalDigits(int decimalDigits) {
    this.decimalDigits = decimalDigits;
  }

  public int getOrdinalPosition() {
    return ordinalPosition;
  }

  public void setOrdinalPosition(int ordinalPosition) {
    this.ordinalPosition = ordinalPosition;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getEntityFullName() {
    return entityFullName;
  }

  public void setEntityFullName(String entityFullName) {
    this.entityFullName = entityFullName;
  }

  public String getSqlDatabaseType() {
    return sqlDatabaseType;
  }

  public void setSqlDatabaseType(String sqlDatabaseType) {
    this.sqlDatabaseType = sqlDatabaseType;
  }

  public Class<?> getTypeMappedClass() {
    return typeMappedClass;
  }

  public void setTypeMappedClass(Class<?> typeMappedClass) {
    this.typeMappedClass = typeMappedClass;
  }

  public List<String> getEnumValues() {
    return enumValues;
  }

  public void setEnumValues(List<String> enumValues) {
    this.enumValues = enumValues;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  public String getClazzPlural() {
    return clazzPlural;
  }

  public void setClazzPlural(String clazzPlural) {
    this.clazzPlural = clazzPlural;
  }

  @Override
  public int compareTo(Attribute o) {
    return new CompareToBuilder().append(getColumnFullName(), o.getColumnFullName()).toComparison();
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  public String getAngularTag() {
    return angularTag;
  }

  public void setAngularTag(String angularTag) {
    this.angularTag = angularTag;
  }

  public String getAngularTagPlural() {
    return angularTagPlural;
  }

  public void setAngularTagPlural(String angularTagPlural) {
    this.angularTagPlural = angularTagPlural;
  }

  public String getAttrSingular() {
    return attrSingular;
  }

  public void setAttrSingular(String attrSingular) {
    this.attrSingular = attrSingular;
  }

  public String getClazzSingular() {
    return clazzSingular;
  }

  public void setClazzSingular(String clazzSingular) {
    this.clazzSingular = clazzSingular;
  }

  public String getLabelSingular() {
    return labelSingular;
  }

  public void setLabelSingular(String labelSingular) {
    this.labelSingular = labelSingular;
  }

  public String getAngularTagSingular() {
    return angularTagSingular;
  }

  public void setAngularTagSingular(String angularTagSingular) {
    this.angularTagSingular = angularTagSingular;
  }

  @Override
  public String getInstanceName() {
    return getAttr();
  }

  @Override
  public String getAttrName() {
    return "attribute";
  }

  @Override
  public String getClassName() {
    return "Attribute";
  }

  @Override
  public String getInstanceClassName() {
    return getClazz();
  }

  @Override
  public String toString() {
    return getInstanceName();
  }

}
