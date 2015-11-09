package com.example.fanilo.cityguide;

import android.app.Application;
import android.content.Context;
import com.example.fanilo.cityguide.annotation.PerApp;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fanilo on 10/7/15.
 */

@Module
public class CityGuideModule {
  private CityGuideApp appApplication;

  public CityGuideModule(CityGuideApp appApplication) {
    this.appApplication = appApplication;
  }

  @Provides
  @PerApp CityGuideApp provideApp() {
    return this.appApplication;
  }

  @Provides @PerApp
  Application provideApplication(CityGuideApp app) {return app;}

  @Provides @PerApp
  Context provideAppContext() {return this.appApplication;}

}
