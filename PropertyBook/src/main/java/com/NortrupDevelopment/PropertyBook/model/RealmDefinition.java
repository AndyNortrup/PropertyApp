package com.NortrupDevelopment.PropertyBook.model;

import android.content.Context;

import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by andy on 10/13/14.
 */
public class RealmDefinition {
  public static final String PRODUCTION_REALM = "PropertyBook.realm";
  public static String TEASTING_REALM = "Test.realm";
  public static final int REALM_VERSION = 2;

  public static Realm getRealm(Context c, String realmFile) {
    try {
      Realm realm = Realm.getInstance(c, realmFile);
      return realm;
    } catch (RealmMigrationNeededException ex) {

      Realm.deleteRealmFile(c, realmFile);
      //Realm.migrateRealmAtPath(realmFile, new RealmMigrator());
      return Realm.getInstance(c, realmFile);
    }
  }
}
