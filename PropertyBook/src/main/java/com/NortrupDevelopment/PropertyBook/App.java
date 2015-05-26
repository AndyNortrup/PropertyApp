package com.NortrupDevelopment.PropertyBook;

import android.app.Application;
import android.util.Log;

import com.NortrupDevelopment.PropertyBook.dagger.PresenterModule;
import com.NortrupDevelopment.PropertyBook.presenter.ImportPresenter;
import com.NortrupDevelopment.PropertyBook.presenter.SearchResultsView;
import com.NortrupDevelopment.PropertyBook.view.DefaultMainActivity;
import com.NortrupDevelopment.PropertyBook.view.ImportViewImpl;
import com.NortrupDevelopment.PropertyBook.view.LINBrowserView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Provides the Application context to Dagger.
 * Created by andy on 3/16/14.
 */
public class App extends Application {

  private PresenterComponent presenterComponent;

  @Singleton
  @Component(modules = {
      PresenterModule.class
  })
  public interface PresenterComponent {
    void inject(App app);

    void inject(DefaultMainActivity activity);

    void inject(LINBrowserView linBrowserView);

    void inject(ImportViewImpl importFragment);

    void inject(ImportPresenter importPresenter);

    void inject(SearchResultsView searchResultsView);
  }


  @Override public void onCreate() {
    Log.d("App.java", "Starting Application");
    super.onCreate();

    presenterComponent = DaggerApp_PresenterComponent.builder()
        .presenterModule(new PresenterModule(this))
        .build();

    presenterComponent.inject(this);
  }

  public PresenterComponent component() {
    return presenterComponent;
  }

}
