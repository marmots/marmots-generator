package org.marmots.generator.parsers.databasemetadata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.marmots.generator.exceptions.ValidationException;
import org.marmots.generator.model.databasemetdata.Attribute;
import org.marmots.generator.model.databasemetdata.Entity;
import org.marmots.generator.utils.LocalizationUtils;
import org.marmots.generator.utils.NameUtils;
import org.marmots.generator.utils.TypeUtils;

import schemacrawler.schema.Column;

public class AttributeFactory {

  private AttributeFactory() {
    // hide default constructor
  }

  public static Attribute create(CompletableFuture<Entity> entity, Column column) {
    Attribute attribute = new Attribute();
    entity.thenAccept(e -> attribute.setEntity(e));
    attribute.setEntityFullName(column.getParent().getFullName());
    attribute.setColumn(column.getName());
    attribute.setColumnFullName(column.getFullName());

    attribute.setDecimalDigits(column.getDecimalDigits());
    attribute.setDefaultValue(column.getDefaultValue());
    attribute.setOrdinalPosition(column.getOrdinalPosition());
    attribute.setRemarks(column.getRemarks());

    attribute.setAttr(NameUtils.toAttributeName(column.getName()));
    attribute.setAttrSingular(NameUtils.toAttributeName(NameUtils.toEnglishSingular(column.getName())));
    attribute.setAttrPlural(NameUtils.toAttributeName(NameUtils.toEnglishPlural(column.getName())));
    attribute.setClazz(NameUtils.toClassName(column.getName()));
    attribute.setClazzSingular(NameUtils.toClassName(NameUtils.toEnglishSingular(column.getName())));
    attribute.setClazzPlural(NameUtils.toClassName(NameUtils.toEnglishPlural(column.getName())));
    attribute.setAngularTag(NameUtils.toAngularTag(NameUtils.toAttributeName(column.getName())));
    attribute.setAngularTagSingular(NameUtils.toAngularTag(NameUtils.toAttributeName(NameUtils.toEnglishSingular(column.getName()))));
    attribute.setAngularTagPlural(NameUtils.toAngularTag(NameUtils.toAttributeName(NameUtils.toEnglishPlural(column.getName()))));
    attribute.setLabel(NameUtils.toLabel(column.getName(), true));
    attribute.setLabelSingular(NameUtils.toLabel(NameUtils.toEnglishSingular(column.getName()), true));
    attribute.setLabelPlural(NameUtils.toLabel(NameUtils.toEnglishPlural(column.getName()), true));

    attribute.setNullable(column.isNullable());
    attribute.setLocalized(LocalizationUtils.isLocalized(column));

    attribute.setSize(column.getSize());
    attribute.setAutoIncremented(column.isAutoIncremented());
    attribute.setJavaSqlType(column.getColumnDataType().getJavaSqlType().getName());
    attribute.setSqlType(column.getColumnDataType().getFullName());
    attribute.setSqlDatabaseType(column.getColumnDataType().getDatabaseSpecificTypeName());
    attribute.setTypeMappedClass(column.getColumnDataType().getTypeMappedClass());
    attribute.setJavaType(TypeUtils.toJavaType(column.getColumnDataType()));
    attribute.setJavascriptType(TypeUtils.toJavascriptType(column.getColumnDataType()));

    attribute.setPartOfPrimaryKey(column.isPartOfPrimaryKey());
    attribute.setPartOfUniqueKey(column.isPartOfUniqueIndex());
    attribute.setGenerated(column.isGenerated());
    attribute.setPartOfForeignKey(column.isPartOfForeignKey());

    if (attribute.isEnum()) {
      try {
        attribute.setEnumValues(getEnumValues(column));
      } catch (Exception e) {
        throw new ValidationException(String.format("cannot get enum values for %s", attribute.getColumnFullName()), e);
      }
    }

    if (column.getReferencedColumn() != null) {
      attribute.setReferencedAttribute(create(DatabaseMetadataParser.FUTURE_ENTITIES.get(column.getReferencedColumn().getParent().getFullName()), column.getReferencedColumn()));
    }
    return attribute;
  }

  // TODO MySQL only
  private static List<String> getEnumValues(Column column) throws Exception {
    List<String> enumValues = new ArrayList<>();
    if (DatabaseMetadataParser.getInstance().getConnection() != null) {
      String table = column.getParent().getName();
      String sql = "show columns from " + table + " where field = ?";
      try (PreparedStatement stmt = DatabaseMetadataParser.getInstance().getConnection().prepareStatement(sql)) {
        stmt.setString(1, column.getName());
        try (ResultSet rs = stmt.executeQuery()) {
          if (rs.next()) {
            String s = rs.getString("type");
            s = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
            s = StringUtils.replace(s, "'", "");
            enumValues = Arrays.asList(s.split("\\s*,\\s*"));
          }
        }
      }
    }
    return enumValues;
  }
}
