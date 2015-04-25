package com.NortrupDevelopment.PropertyBook.dagger_modules;

import android.content.Context;

import com.NortrupDevelopment.PropertyBook.presenter.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by andy on 4/25/15.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

  void inject(MainActivity baseActivity);

  Context context();
}
