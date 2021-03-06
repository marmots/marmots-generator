package org.marmots.generator.parsers.databasemetadata;

import java.util.ArrayList;
import java.util.List;

import org.marmots.generator.model.databasemetdata.Reference;
import org.marmots.generator.model.databasemetdata.Relation;

import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.ForeignKey;
import schemacrawler.utility.MetaDataUtility;

public class RelationFactory {
  private RelationFactory() {
    // hide default constructor
  }

  public static Relation create(ForeignKey foreignKey) {
    Relation relation = new Relation();
    relation.setName(foreignKey.getName());
    relation.setFullName(foreignKey.getFullName());
    relation.setDefinition(foreignKey.getDefinition());
    relation.setRemarks(foreignKey.getRemarks());
    relation.setConstraintType(foreignKey.getConstraintType().getValue());
    relation.setCardinality(MetaDataUtility.findForeignKeyCardinality(foreignKey).name());

    // references
    List<Reference> references = new ArrayList<>();
    for (ColumnReference reference : foreignKey.getColumnReferences()) {
      references.add(ReferenceFactory.create(reference));
    }
    relation.setReferences(references);

    return relation;
  }
}
