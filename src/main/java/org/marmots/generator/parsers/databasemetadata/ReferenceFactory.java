package org.marmots.generator.parsers.databasemetadata;

import org.marmots.generator.model.databasemetdata.Reference;

import schemacrawler.schema.ColumnReference;

public class ReferenceFactory {
  private ReferenceFactory() {
    // hide default constructor
  }

  public static Reference create(ColumnReference columnReference) {
    Reference reference = new Reference();
    reference.setForeignKey(AttributeFactory.create(DatabaseMetadataParser.FUTURE_ENTITIES.get(columnReference.getForeignKeyColumn().getParent().getFullName()),
        columnReference.getForeignKeyColumn()));
    reference.setPrimaryKey(AttributeFactory.create(DatabaseMetadataParser.FUTURE_ENTITIES.get(columnReference.getPrimaryKeyColumn().getParent().getFullName()),
        columnReference.getPrimaryKeyColumn()));
    return reference;
  }
}
