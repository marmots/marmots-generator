package org.marmots.generator.model.databasemetdata;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.marmots.generator.model.ChildContext;
import org.marmots.generator.model.GeneratorContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class Entity extends GeneratorContext implements Comparable<Entity> {
  /**
   * generated uid
   */
  private static final long serialVersionUID = 6254907224587138577L;

  private String table;
  private String tableFullName;
  private String remarks;
  private String definition;
  private String type;
  private boolean view;
  private boolean relationEntity;
  private boolean foreignKeyIsUnique;

  private String clazz;
  private String clazzPlural;
  private String attr;
  private String attrPlural;
  private String angularTag;
  private String angularTagPlural;
  private String label;
  private String labelPlural;
  private Attribute titleField;

  private List<Attribute> primaryKey;

  private List<Index> uniqueKeys;
  private List<Index> indexes;

  private List<Attribute> attributes;
  private List<Relation> relations;

  private boolean localized;

  private int queryDepth;
  private String selectList;
  private String selectDetail;
  private String selectCount;
  private String insert;
  private String insertLocalization;
  private String update;
  private String updateLocalization;
  private String save;
  private String delete;
  private String wherePK;

  public boolean isCompositeKey() {
    return primaryKey.size() > 1;
  }

  public boolean hasRelations() {
    return !relations.isEmpty();
  }

  public boolean hasAutoIncrementedKey() {
    for (Attribute key : getPrimaryKey()) {
      if (key.isAutoIncremented()) {
        return true;
      }
    }
    return false;
  }

  public boolean hasExportedForeignKeys() {
    for (Relation relation : getRelations()) {
      if (!relation.isImported()) {
        return true;
      }
    }
    return false;
  }

  public boolean hasImportedForeignKeys() {
    for (Relation relation : getRelations()) {
      if (relation.isImported()) {
        return true;
      }
    }
    return false;
  }

  public boolean hasNotNullableImportedForeignKeys() {
    for (Relation relation : getRelations()) {
      if (relation.isImported()) {
        for (Reference reference : relation.getReferences()) {
          if (!reference.getForeignKey().isNullable()) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean hasLongField() {
    for (Attribute attribute : getAttributes()) {
      if (attribute.isLongField()) {
        return true;
      }
    }
    return false;
  }

  public boolean hasBinaryField() {
    return hasBinaryField(false);
  }

  public boolean hasBinaryField(boolean includeRelated) {
    for (Attribute attribute : getAttributes()) {
      if (attribute.isBinary()) {
        return true;
      }
      if (includeRelated) {
        if (attribute.isPartOfForeignKey()) {
          for (Attribute refAttr : attribute.getReferencedAttribute().getEntity().getAttributes()) {
            if (refAttr.isBinary()) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean hasDateFields() {
    for (Attribute attribute : getAttributes()) {
      if (attribute.isDateType()) {
        return true;
      }
    }
    return false;
  }

  public boolean hasForeignKeys() {
    return !getRelations().isEmpty();
  }

  public boolean allAreKeyAttributes() {
    return getPrimaryKey().size() == getAttributes().size();
  }

  public Set<Attribute> getPKColumns() {
    Set<Attribute> pkattributes = new LinkedHashSet<>();
    for (Attribute pkattribute : getPrimaryKey()) {
      if (pkattribute.isPartOfPrimaryKey()) {
        pkattributes.add(pkattribute);
      }
    }
    return pkattributes;
  }

  @JsonIgnore
  public Set<Entity> getExportedForeignKeyEntities() {
    SortedSet<Entity> entities = new TreeSet<>();
    for (Relation relation : getRelations()) {
      if (!relation.isImported()) {
        for (Reference reference : relation.getReferences()) {
          if (!entities.contains(reference.getForeignKey().getEntity())) {
            entities.add(reference.getForeignKey().getEntity());
          }
        }
      }
    }
    return entities;
  }

  @JsonIgnore
  public Set<Entity> getImportedForeignKeyEntities() {
    SortedSet<Entity> entities = new TreeSet<>();
    for (Relation relation : getRelations()) {
      if (relation.isImported()) {
        for (Reference reference : relation.getReferences()) {
          if (!entities.contains(reference.getForeignKey().getEntity())) {
            entities.add(reference.getForeignKey().getEntity());
          }
        }
      }
    }
    return entities;
  }

  public Entity getRelatedEntity(Entity relationTable) {
    if (relationTable.isRelationEntity()) {
      for (Relation relation : relationTable.getRelations()) {
        for (Reference reference : relation.getReferences()) {
          if (!getTableFullName().equals(reference.getPrimaryKey().getEntity().getTableFullName())) {
            return reference.getPrimaryKey().getEntity();
          }
        }
      }
    }
    return null;
  }

  public String getPKAttributes() {
    return getPKAttributes(false);
  }

  public String getPKAttributes(boolean pathVariable) {
    String attrs = "";
    for (Attribute attribute : getPrimaryKey()) {
      attrs += (StringUtils.isEmpty(attrs) ? "" : ", ") + (pathVariable ? "@PathVariable " : "") + (pathVariable && attribute.isDateType() ? "long" : attribute.getJavaType()) + " "
          + attribute.getAttr();
    }
    return attrs;
  }

  public String getPKAttributesCall() {
    return getPKAttributesCall(false);
  }

  public String getPKAttributesCall(boolean pathVariable) {
    String attrs = "";
    for (Attribute attribute : getPrimaryKey()) {
      attrs += (StringUtils.isEmpty(attrs) ? "" : ", ") + (pathVariable && attribute.isDateType() ? "new java.util.Date(" + attribute.getAttr() + ")" : attribute.getAttr());
    }
    return attrs;
  }

  public boolean isHierarchical() {
    for (Attribute attribute : getAttributes()) {
      if (isHierarchical(attribute)) {
        return true;
      }
    }
    return false;
  }

  public boolean isHierarchical(Attribute attribute) {
    return attribute.isPartOfForeignKey() && attribute.getReferencedAttribute().getEntity().getTableFullName().equals(getTableFullName());
  }

  public static Long generateSerialVersionUID() {
    return RandomUtils.nextLong();
  }

  public String getTable() {
    return table;
  }

  public void setTable(String table) {
    this.table = table;
  }

  public String getTableFullName() {
    return tableFullName;
  }

  public void setTableFullName(String tableFullName) {
    this.tableFullName = tableFullName;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
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

  public List<Attribute> getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(List<Attribute> primaryKey) {
    this.primaryKey = primaryKey;
  }

  public List<Index> getUniqueKeys() {
    return uniqueKeys;
  }

  public void setUniqueKeys(List<Index> uniqueKeys) {
    this.uniqueKeys = uniqueKeys;
  }

  public List<Index> getIndexes() {
    return indexes;
  }

  public void setIndexes(List<Index> indexes) {
    this.indexes = indexes;
  }

  @ChildContext(value = "attribute", context = Attribute.class)
  public List<Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }

  public List<Relation> getRelations() {
    return relations;
  }

  public List<Relation> getImportedRelations() {
    List<Relation> relations = new ArrayList<>();
    for (Relation relation : getRelations()) {
      if (relation.isImported()) {
        relations.add(relation);
      }
    }
    return relations;
  }

  public List<Relation> getExportedRelations() {
    List<Relation> relations = new ArrayList<>();
    for (Relation relation : getRelations()) {
      if (!relation.isImported()) {
        relations.add(relation);
      }
    }
    return relations;
  }

  @JsonIgnore
  public Set<Entity> getImportedRelationEntities() {
    SortedSet<Entity> fks = new TreeSet<>();
    for (Relation relation : getImportedRelations()) {
      for (Reference reference : relation.getReferences()) {
        fks.add(reference.getPrimaryKey().getEntity());
      }
    }
    return fks;
  }

  @JsonIgnore
  public Set<Entity> getExportedRelationEntities() {
    SortedSet<Entity> fks = new TreeSet<>();
    for (Relation relation : getExportedRelations()) {
      for (Reference reference : relation.getReferences()) {
        fks.add(reference.getForeignKey().getEntity());
      }
    }
    return fks;
  }

  public void setRelations(List<Relation> relations) {
    this.relations = relations;
  }

  public Relation getOneToOneRelations() {
    return null;
  }

  public List<Relation> getOneToManyRelations() {
    return null;
  }

  public List<Relation> getManyToOneRelations() {
    return null;
  }

  public List<Relation> getManyToManyRelations() {
    return null;
  }

  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isView() {
    return view;
  }

  public void setView(boolean view) {
    this.view = view;
  }

  public String getSelectList() {
    return selectList;
  }

  public void setSelectList(String selectList) {
    this.selectList = selectList;
  }

  public String getSelectDetail() {
    return selectDetail;
  }

  public void setSelectDetail(String selectDetail) {
    this.selectDetail = selectDetail;
  }

  public String getSelectCount() {
    return selectCount;
  }

  public void setSelectCount(String selectCount) {
    this.selectCount = selectCount;
  }

  public int getQueryDepth() {
    return queryDepth;
  }

  public void setQueryDepth(int queryDepth) {
    this.queryDepth = queryDepth;
  }

  public boolean isLocalized() {
    return localized;
  }

  public void setLocalized(boolean localized) {
    this.localized = localized;
  }

  public String getInsert() {
    return insert;
  }

  public void setInsert(String insert) {
    this.insert = insert;
  }

  public String getInsertLocalization() {
    return insertLocalization;
  }

  public void setInsertLocalization(String insertLocalization) {
    this.insertLocalization = insertLocalization;
  }

  public String getUpdate() {
    return update;
  }

  public void setUpdate(String update) {
    this.update = update;
  }

  public String getUpdateLocalization() {
    return updateLocalization;
  }

  public void setUpdateLocalization(String updateLocalization) {
    this.updateLocalization = updateLocalization;
  }

  public String getDelete() {
    return delete;
  }

  public void setDelete(String delete) {
    this.delete = delete;
  }

  public String getSave() {
    return save;
  }

  public void setSave(String save) {
    this.save = save;
  }

  public Attribute getTitleField() {
    return titleField;
  }

  public void setTitleField(Attribute titleField) {
    this.titleField = titleField;
  }

  @Override
  public int compareTo(Entity o) {
    return new CompareToBuilder().append(getTableFullName(), o.getTableFullName()).toComparison();
  }

  public boolean isRelationEntity() {
    return relationEntity;
  }

  public void setRelationEntity(boolean relationEntity) {
    this.relationEntity = relationEntity;
  }

  public String getWherePK() {
    return wherePK;
  }

  public void setWherePK(String wherePK) {
    this.wherePK = wherePK;
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

  public boolean isForeignKeyIsUnique() {
    return foreignKeyIsUnique;
  }

  public void setForeignKeyIsUnique(boolean foreignKeyIsUnique) {
    this.foreignKeyIsUnique = foreignKeyIsUnique;
  }

  @Override
  public String getAttrName() {
    return "entity";
  }

  @Override
  public String getClassName() {
    return "Entity";
  }

  @Override
  public String getInstanceName() {
    return attr;
  }

  @Override
  public String getInstanceClassName() {
    return clazz;
  }

  @Override
  public String toString() {
    return getInstanceName();
  }
}
