package com.NortrupDevelopment.PropertyBook.dagger;

import com.NortrupDevelopment.PropertyBook.App;
import com.NortrupDevelopment.PropertyBook.io.FileUtilities;
import com.NortrupDevelopment.PropertyBook.io.PBICNameReader;
import com.NortrupDevelopment.PropertyBook.io.PrimaryHandReceiptReader;
import com.NortrupDevelopment.PropertyBook.io.PrimaryHandReceiptReaderImpl;
import com.NortrupDevelopment.PropertyBook.io.PropertyBookImporter;
import com.NortrupDevelopment.PropertyBook.model.ModelFactory;
import com.NortrupDevelopment.PropertyBook.model.ModelFactoryImpl;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcherImpl;
import com.NortrupDevelopment.PropertyBook.model.ModelUtils;
import com.NortrupDevelopment.PropertyBook.model.ModelUtilsImpl;
import com.NortrupDevelopment.PropertyBook.presenter.DefaultMainActivityPresenter;
import com.NortrupDevelopment.PropertyBook.presenter.DefaultSearchPresenter;
import com.NortrupDevelopment.PropertyBook.presenter.ImportPresenter;
import com.NortrupDevelopment.PropertyBook.presenter.MainActivityPresenter;
import com.NortrupDevelopment.PropertyBook.presenter.SearchPresenter;
import com.NortrupDevelopment.PropertyBook.view.SearchResultViewFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by andy on 4/25/15.
 */
@Module
public class PresenterModule {

  private final App application;

  public PresenterModule(App application) {
    this.application = application;
  }

  @Provides @Singleton
  MainActivityPresenter providesMainActivityPresenter(Realm realm) {
    return new DefaultMainActivityPresenter(realm);
  }

  @Provides Realm provideRealm() {
    try {
      return Realm.getInstance(application);
    } catch (RealmMigrationNeededException ex) {
      Realm.deleteRealmFile(application);
      return Realm.getInstance(application);
    }
  }

  @Provides ModelSearcher provideModelSearcher(Realm realm) {
    return new ModelSearcherImpl(realm);
  }

  @Provides @Singleton ImportPresenter ImportPresenter() {
    return new ImportPresenter();
  }

  @Provides
  PropertyBookImporter propertyBookImporterProvider(ModelUtils modelUtils,
                                                    PrimaryHandReceiptReader reader) {
    return new PropertyBookImporter(application, modelUtils, reader);
  }

  @Provides FileUtilities FileUtilities() {
    return new FileUtilities(application);
  }

  @Provides PBICNameReader pbicNameReaderProvider() {
    return new PBICNameReader(application);
  }

  @Provides ModelFactory providesModelFactory() {
    return new ModelFactoryImpl();
  }

  @Provides PrimaryHandReceiptReader handReceiptReaderProvider(
      ModelFactory modelFactory) {
    return new PrimaryHandReceiptReaderImpl(modelFactory);
  }

  @Provides ModelUtils modelUtilsProvider(Realm realm) {
    return new ModelUtilsImpl(realm);
  }

  @Provides SearchResultViewFactory provideSearchResultsViewFactory() {
    return new SearchResultViewFactory(application);
  }

  @Provides SearchPresenter provideSearchPresenter(ModelSearcher searcher,
                                                   SearchResultViewFactory viewFactory) {
    return new DefaultSearchPresenter(searcher, viewFactory);
  }
}
