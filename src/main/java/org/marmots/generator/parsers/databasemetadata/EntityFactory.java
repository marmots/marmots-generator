package org.marmots.generator.parsers.databasemetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.marmots.generator.model.databasemetdata.Attribute;
import org.marmots.generator.model.databasemetdata.DatabaseMetadataApplication;
import org.marmots.generator.model.databasemetdata.Entity;
import org.marmots.generator.model.databasemetdata.Index;
import org.marmots.generator.model.databasemetdata.Reference;
import org.marmots.generator.model.databasemetdata.Relation;
import org.marmots.generator.model.databasemetdata.RelationType;
import org.marmots.generator.utils.EntityUtils;
import org.marmots.generator.utils.LocalizationUtils;
import org.marmots.generator.utils.NameUtils;
import org.marmots.generator.utils.SQLUtils;
import org.marmots.generator.utils.TypeUtils;

import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.IndexColumn;
import schemacrawler.schema.Table;

public class EntityFactory {
  private EntityFactory() {
    // hide default constructor
  }

  public static Entity create(DatabaseMetadataApplication application, Table table) {
    Entity entity = new Entity();

    // direct attributes
    entity.setTable(table.getName());
    entity.setTableFullName(table.getFullName());
    entity.setRemarks(table.getRemarks());
    entity.setDefinition(table.getDefinition());
    entity.setType(table.getTableType().getTableType());
    entity.setView(table.getTableType().isView());
    entity.setRelationEntity(isRelationTable(table));
    entity.setForeignKeyIsUnique(foreignKeyIsUnique(table));

    // derived attributes
    entity.setClazz(NameUtils.toClassName(NameUtils.toEnglishSingular(table.getName())));
    entity.setClazzPlural(NameUtils.toClassName(NameUtils.toEnglishPlural(table.getName())));
    entity.setAttr(NameUtils.toAttributeName(NameUtils.toEnglishSingular(table.getName())));
    entity.setAttrPlural(NameUtils.toAttributeName(NameUtils.toEnglishPlural(table.getName())));
    entity.setAngularTag(NameUtils.toAngularTag(NameUtils.toAttributeName(NameUtils.toEnglishSingular(table.getName()))));
    entity.setAngularTagPlural(NameUtils.toAngularTag(NameUtils.toAttributeName(NameUtils.toEnglishPlural(table.getName()))));
    entity.setLabel(NameUtils.toLabel(NameUtils.toEnglishSingular(table.getName()), true));
    entity.setLabelPlural(NameUtils.toLabel(NameUtils.toEnglishPlural(table.getName()), true));

    // title field
    Column titleField = getTitleField(table);
    entity.setTitleField(AttributeFactory.create(DatabaseMetadataParser.FUTURE_ENTITIES.get(titleField.getParent().getFullName()), titleField));

    // primary key
    List<Attribute> primaryKey = new ArrayList<>();
    for (final Column column : table.getPrimaryKey()) {
      primaryKey.add(AttributeFactory.create(DatabaseMetadataParser.FUTURE_ENTITIES.get(table.getFullName()), column));
    }
    entity.setPrimaryKey(primaryKey);

    // unique keys
    List<Index> uniqueKeys = new ArrayList<>();
    for (final schemacrawler.schema.Index dbindex : table.getIndexes()) {
      if (dbindex.isUnique()) {
        Index index = IndexFactory.create(dbindex);
        uniqueKeys.add(index);
      }
    }
    entity.setUniqueKeys(uniqueKeys);

    // indexes
    List<Index> indexes = new ArrayList<>();
    for (final schemacrawler.schema.Index dbindex : table.getIndexes()) {
      if (dbindex.isUnique()) {
        Index index = IndexFactory.create(dbindex);
        indexes.add(index);
      }
    }
    entity.setIndexes(indexes);

    // attributes
    List<Attribute> attributes = new ArrayList<>();
    for (final Column column : table.getColumns()) {
      attributes.add(AttributeFactory.create(DatabaseMetadataParser.FUTURE_ENTITIES.get(table.getFullName()), column));
    }

    // localized attributes
    Set<Column> columns = LocalizationUtils.getLocalizedColumns(table);
    for (Column column : columns) {
      Attribute attribute = AttributeFactory.create(DatabaseMetadataParser.FUTURE_ENTITIES.get(table.getFullName()), column);
      attribute.setLocalized(true);
      attributes.add(attribute);
    }

    // set to entity
    entity.setAttributes(attributes);

    // references
    List<Relation> relations = new ArrayList<>();
    for (ForeignKey foreignKey : table.getImportedForeignKeys()) {
      Relation relation = RelationFactory.create(foreignKey);
      relation.setImported(true);
      relations.add(relation);
    }
    for (ForeignKey foreignKey : EntityUtils.getExportedForeignKeys(table)) {
      Relation relation = RelationFactory.create(foreignKey);
      relation.setImported(false);
      relations.add(relation);
    }
    entity.setRelations(relations);

    // TODO: detect relation types
    for (Relation relation : relations) {
      // detect SELF_REFERENCE
      boolean selfReference = true;
      for (Reference reference : relation.getReferences()) {
        if (!reference.getPrimaryKey().getEntityFullName().equals(reference.getForeignKey().getEntityFullName())) {
          selfReference = false;
        }
      }
      if (selfReference) {
        relation.setType(RelationType.SELF_REFERENCE);
        continue;
      }

      /*
       * TODO: detect ONE_TO_ONE boolean oneToOne = true; for(Reference reference: relation.getReferences()) {
       * if(!reference.getForeignKey().getReferencedAttribute().isPartOfPrimaryKey()) { oneToOne = false; } } if(oneToOne) { relation.setType(RelationType.ONE_TO_ONE); continue; }
       */

      // detect MANY_TO_MANY -- de moment no en tenim perque afegim les M:N
      // Nom??s t?? imported foreign keys: les seves columnes son foreignKey -> PK-column cap a diferents taules
      // Les foreign key -> PK-column formen PK
      // NO pot tenir m??s camps

      // detect ONE_TO_MANY -> List<VO>
      if (relation.isImported()) {
        relation.setType(RelationType.ONE_TO_MANY);
        continue;
      }

      // detect MANY_TO_ONE -> VO
      if (!relation.isImported()) {
        relation.setType(RelationType.MANY_TO_ONE);
        continue;
      }
    }

    // localization
    entity.setLocalized(LocalizationUtils.hasLocalizationTable(table));

    // SQL Queries
    if (entity.getQueryDepth() == 0) {
      entity.setQueryDepth(application.getDefaultQueryDepth());
    }
    entity.setSelectList(SQLUtils.selectList(table, entity.getQueryDepth()));
    entity.setSelectDetail(SQLUtils.selectDetail(table, entity.getQueryDepth()));
    entity.setSelectCount(SQLUtils.selectCount(table, entity.getQueryDepth()));
    entity.setInsert(SQLUtils.insert(table));
    entity.setInsertLocalization(SQLUtils.insertLocalization(table));
    entity.setUpdate(SQLUtils.update(table));
    entity.setUpdateLocalization(SQLUtils.updateLocalization(table));
    entity.setSave(SQLUtils.save(table));
    entity.setDelete(SQLUtils.delete(table));
    entity.setWherePK(SQLUtils.getWherePKIndex(table.getPrimaryKey().getColumns(), "t"));

    return entity;
  }

  private static Column getTitleField(Table table) {
    for (Column col : table.getColumns()) {
      if ("title_label".equalsIgnoreCase(col.getRemarks())) {
        return col;
      }
    }
    for (Column col : table.getColumns()) {
      if (TypeUtils.isStringType(col.getColumnDataType())) {
        return col;
      }
    }
    return table.getColumns().get(0);
  }

  public static boolean isRelationTable(Table table) {
    return table.getPrimaryKey() != null && table.getPrimaryKey().getColumns().size() == table.getColumns().size() && primaryKeysEqualsForeignKeys(table);
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
    for (schemacrawler.schema.Index index : table.getIndexes()) {
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
}
