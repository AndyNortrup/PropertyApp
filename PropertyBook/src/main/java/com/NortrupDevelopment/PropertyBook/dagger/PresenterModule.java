package com.NortrupDevelopment.PropertyBook.dagger;

import com.NortrupDevelopment.PropertyBook.App;
import com.NortrupDevelopment.PropertyBook.io.PropertyBookImporter;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcherImpl;
import com.NortrupDevelopment.PropertyBook.presenter.DefaultMainActivityPresenter;
import com.NortrupDevelopment.PropertyBook.presenter.MainActivityPresenter;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by andy on 4/25/15.
 */
@Module
public class PresenterModule {

  private final App application;

  public PresenterModule(App application) {
    this.application = application;
  }

  @Provides MainActivityPresenter providesMainActivityPresenter(Realm realm) {
    return new DefaultMainActivityPresenter(realm);
  }

  @Provides Realm provideRealm() {
    return Realm.getInstance(application);
  }

  @Provides ModelSearcher provideModelSearcher(Realm realm) {
    return new ModelSearcherImpl(realm);
  }

  @Provides PropertyBookImporter PropertyBookImporter() {
    return new PropertyBookImporter();
  }
}
