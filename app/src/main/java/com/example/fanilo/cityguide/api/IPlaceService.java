package com.example.fanilo.cityguide.api;

import com.example.fanilo.cityguide.model.MatrixApi;
import com.example.fanilo.cityguide.model.ResultApi;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by fanilo on 10/7/15.
 */

public interface IPlaceService {
  @GET("/place/search/json?radius=1000&sensor=true&key=AIzaSyBZ2q2Ts-BGLU3IzPLJo-HQA1iMTlrpgos")
  Observable<ResultApi> getGooglePlace(
      @Query("location") final String location,
      @Query("types") final String type);

  @GET("/distancematrix/json?key=AIzaSyBZ2q2Ts-BGLU3IzPLJo-HQA1iMTlrpgos")
  Observable<MatrixApi> getDistance(
      @Query("origins") final String origins,
      @Query("destinations") final String destinations);

}
