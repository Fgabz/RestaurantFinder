package com.example.fanilo.cityguide.api;

import com.example.fanilo.cityguide.model.MatrixApi;
import com.example.fanilo.cityguide.model.ResultApi;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by fanilo on 10/7/15.
 */
public class PlaceApi {

  private IPlaceService iPlaceService;

  @Inject
  public PlaceApi(IPlaceService iPlaceService) {
    this.iPlaceService = iPlaceService;
  }

  public Observable<ResultApi> getPlaces(String location, String types) {
    Timber.d("getPlace() THREAD " + Thread.currentThread().getName());

    return iPlaceService.getGooglePlace(location, types)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io());
  }

  public Observable<MatrixApi> getDistance(String origins, String destinations) {
    Timber.d("getPlace() THREAD " + Thread.currentThread().getName());

    return iPlaceService.getDistance(origins, destinations)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io());
  }
}
