package com.NortrupDevelopment.PropertyBook.dagger_modules;

import android.content.Context;

import com.NortrupDevelopment.PropertyBook.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andy on 4/25/15.
 */
@Module
public class ApplicationModule {

  private final App application;

  public ApplicationModule(App application) {
    this.application = application;
  }

  @Provides
  @Singleton
  Context provideApplicationContext() {
    return this.application;
  }
}
