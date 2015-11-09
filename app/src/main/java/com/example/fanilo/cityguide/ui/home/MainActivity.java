package com.example.fanilo.cityguide.ui.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.fanilo.cityguide.CityGuideApp;
import com.example.fanilo.cityguide.R;
import com.example.fanilo.cityguide.api.PlaceApi;
import com.example.fanilo.cityguide.model.ResultApi;
import com.example.fanilo.cityguide.ui.base.BaseActivity;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;
import rx.functions.Action1;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements LocationListener {

  private static final int            MY_PERMISSIONS_REQUEST_LOCATION = 1;
  // The minimum distance to change Updates in meters
  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
  // The minimum time between updates in milliseconds
  private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

  @Inject PlaceApi        placeApi;
  @Inject LocationManager locationManager;

  @Bind(R.id.toolbar)       Toolbar      toolbar;
  @Bind(R.id.recycler_view) RecyclerView recyclerView;
  @Bind(R.id.pull_refresh_list) SwipeRefreshLayout refreshLayout;

  private PlaceListAdapter placeListAdapter;
  private ResultApi.TYPE PLACE_TYPE = ResultApi.TYPE.BAR;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    CityGuideApp.get(this).getAppComponent().inject(this);
    CityGuideApp.get(this).getAppComponent().ottoBus().register(this);
    setUpToolBar();
    fetchData();

    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        fetchData();
        refreshLayout.setRefreshing(false);
      }
    });
  }

  private void fetchData() {
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
          != PackageManager.PERMISSION_GRANTED
          && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
          != PackageManager.PERMISSION_GRANTED) {

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        apiCall(location, PLACE_TYPE);
      } else {
        ActivityCompat.requestPermissions(this,
            new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
            MY_PERMISSIONS_REQUEST_LOCATION);
      }
    } else {
      Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

      if (location == null) {
        new AlertDialog.Builder(MainActivity.this)
            .setMessage("You need to allow access to the location")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                fetchData();
              }
            })
            .create()
            .show();
      } else {
        apiCall(location, PLACE_TYPE);
      }
    }
  }

  private void apiCall(Location location, final ResultApi.TYPE type) {
    final String myLocation = String.valueOf(location.getLatitude()) + "," +
        String.valueOf(location.getLongitude());

    bind(placeApi.getPlaces(myLocation, type.name().toLowerCase()).subscribe(
        new Action1<ResultApi>() {
          @Override
          public void call(ResultApi resultApi) {
            placeListAdapter = new PlaceListAdapter(getLayoutInflater(), type.getIconValue(),
                myLocation, placeApi);
            placeListAdapter.addData(resultApi.getResult());
            recyclerView.setAdapter(placeListAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "error getting places from Google API");
          }
        }));
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_LOCATION: {
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // permission was granted
          fetchData();
        } else {
          // permission denied
        }
      }
    }
  }

  private void setUpToolBar() {
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
  }

  @Subscribe
  public void onChangedPlaceType(ResultApi.TYPE type) {
    if (refreshLayout != null) {
      refreshLayout.setRefreshing(false);
    }
    PLACE_TYPE = type;
    fetchData();
  }

  @Override
  public void onLocationChanged(Location location) {

  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }
}
