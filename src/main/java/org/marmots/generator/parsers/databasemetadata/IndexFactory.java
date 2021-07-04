package org.marmots.generator.parsers.databasemetadata;

import java.util.ArrayList;
import java.util.List;

import org.marmots.generator.model.databasemetdata.Attribute;
import org.marmots.generator.model.databasemetdata.Index;
import org.marmots.generator.utils.NameUtils;

import schemacrawler.schema.Column;

public class IndexFactory {
  private IndexFactory() {
    // hide default constructor
  }

  public static Index create(schemacrawler.schema.Index dbIndex) {
    Index index = new Index();
    index.setName(dbIndex.getName());
    index.setFullName(dbIndex.getFullName());
    index.setCardinality(dbIndex.getCardinality());
    index.setDefinition(dbIndex.getDefinition());
    index.setRemarks(dbIndex.getRemarks());
    index.setUnique(dbIndex.isUnique());

    index.setClazz(NameUtils.toClassName(NameUtils.toEnglishSingular(dbIndex.getName())));
    index.setClazzPlural(NameUtils.toClassName(NameUtils.toEnglishPlural(dbIndex.getName())));
    index.setAttr(NameUtils.toAttributeName(NameUtils.toEnglishSingular(dbIndex.getName())));
    index.setAttrPlural(NameUtils.toAttributeName(NameUtils.toEnglishPlural(dbIndex.getName())));

    List<Attribute> attributes = new ArrayList<>();
    for (final Column column : dbIndex.getColumns()) {
      Attribute attribute = AttributeFactory.create(DatabaseMetadataParser.FUTURE_ENTITIES.get(column.getParent().getFullName()), column);
      attributes.add(attribute);
    }
    index.setAttributes(attributes);
    return index;
  }
}
