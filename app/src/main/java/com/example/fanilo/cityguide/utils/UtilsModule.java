package com.example.fanilo.cityguide.utils;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import com.example.fanilo.cityguide.BuildConfig;
import com.example.fanilo.cityguide.annotation.PerApp;
import com.example.fanilo.cityguide.api.IPlaceService;
import com.example.fanilo.cityguide.api.PlaceApi;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by fanilo on 10/7/15.
 */
@Module
public class UtilsModule {

  @Provides
  @PerApp
  LocationManager provideLocationManager(final Context context) {
    return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
  }

  @Provides
  @PerApp
  Bus provideOttoBus() {
    return new Bus();
  }

  @Provides
  @PerApp OkHttpClient provideOkHttpClient(final Application app) {
    return new OkHttpClient();
  }

  @Provides
  @PerApp Client provideRetrofitClient(OkHttpClient okHttpClient) {
    return new OkClient(okHttpClient);
  }

  @Provides
  @PerApp Endpoint provideGigatoolsEndpoint() {
    return Endpoints.newFixedEndpoint(
        "https://maps.googleapis.com/maps/api");
  }

  @Provides
  @PerApp
  RestAdapter provideGooglePlaceRestAdapter(Endpoint endpoint, Client client) {
    return new RestAdapter.Builder()
        .setEndpoint(endpoint)
        .setClient(client)
        .setConverter(new GsonConverter(new GsonBuilder().create()))
        .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
        .build();
  }

  @Provides
  @PerApp
  IPlaceService provideGooglePlaceService(RestAdapter restAdapter) {
    return restAdapter.create(IPlaceService.class);
  }

  @Provides
  @PerApp
  PlaceApi prividePlaceApi(IPlaceService placeService) {
    return new PlaceApi(placeService);
  }
}
