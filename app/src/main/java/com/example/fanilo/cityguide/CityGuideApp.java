package com.example.fanilo.cityguide;

import android.app.Application;
import android.content.Context;
import com.example.fanilo.cityguide.utils.UtilsModule;
import timber.log.Timber;

/**
 * Created by fanilo on 10/7/15.
 */
public class CityGuideApp extends Application {
  private CityGuideComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    initiliazeInjector();
  }

  private void initiliazeInjector() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }

    this.appComponent = DaggerCityGuideComponent.builder()
        .cityGuideModule(new CityGuideModule(this))
        .utilsModule(new UtilsModule())
        .build();
  }

  public CityGuideComponent getAppComponent() {
    return this.appComponent;
  }

  public static CityGuideApp get(Context context) {
    return (CityGuideApp) context.getApplicationContext();
  }
}
