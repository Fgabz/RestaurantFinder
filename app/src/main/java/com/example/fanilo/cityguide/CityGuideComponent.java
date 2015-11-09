package com.example.fanilo.cityguide;

import com.example.fanilo.cityguide.annotation.PerApp;
import com.example.fanilo.cityguide.ui.home.MainActivity;
import com.example.fanilo.cityguide.utils.UtilsModule;
import com.squareup.otto.Bus;
import dagger.Component;

/**
 * Created by fanilo on 10/7/15.
 */

@PerApp
@Component(
    modules = {
        CityGuideModule.class,
        UtilsModule.class
    }
)
public interface CityGuideComponent {
  void inject(MainActivity mainActivity);

  Bus ottoBus();
}
