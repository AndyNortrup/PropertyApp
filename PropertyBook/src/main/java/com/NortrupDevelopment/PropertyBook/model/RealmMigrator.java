package com.NortrupDevelopment.PropertyBook.model;

import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.internal.ColumnType;
import io.realm.internal.Table;

/**
 * Migrates the realm files with changes in the model.
 * Created by andy on 10/13/14.
 */
public class RealmMigrator implements RealmMigration {
  @Override
  public long execute(Realm realm, long version) {

    if(version == 0) {
      Table stockNumber = realm.getTable(StockNumber.class);
      stockNumber.addColumn(ColumnType.LINK_LIST, "serialNumbers");
      version++;
    }
    return version;
  }
}
