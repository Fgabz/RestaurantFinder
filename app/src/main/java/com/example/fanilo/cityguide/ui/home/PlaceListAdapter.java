package com.example.fanilo.cityguide.ui.home;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.fanilo.cityguide.R;
import com.example.fanilo.cityguide.api.PlaceApi;
import com.example.fanilo.cityguide.model.MatrixApi;
import com.example.fanilo.cityguide.model.ResultApi;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by fanilo on 10/7/15.
 */
public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

  private List<ResultApi.Place> placeList = new ArrayList<>();
  private LayoutInflater layoutInflater;
  private int ressourceId;
  private PlaceApi placeApi;
  private String myLocation;

  public PlaceListAdapter(LayoutInflater layoutInflater, int ressourceId, String myLocation,
      PlaceApi placeApi) {
    this.ressourceId = ressourceId;
    this.layoutInflater = layoutInflater;
    this.placeApi = placeApi;
    this.myLocation = myLocation;
  }

  @Override
  public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        layoutInflater.inflate(R.layout.layout_item_place, parent, false);

    return new PlaceViewHolder(view);
  }

  @Override
  public void onBindViewHolder(PlaceViewHolder holder, int position) {
    ResultApi.Place currentPlace = placeList.get(position);
    holder.placeName.setText(currentPlace.getPlaceName());
    Drawable img = layoutInflater.getContext().getResources().getDrawable(ressourceId);
    holder.placeName.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
    setRating(currentPlace.getRatingValue(), holder);
    ResultApi.Place.Geometry.GoogleLocation loc = currentPlace.getGeometry().getGoogleLocation();
    setDistance(holder, loc.getLatitude(), loc.getLongitude());
  }


  private void setDistance(final PlaceViewHolder holder, String latitude, String longitude) {
    String dest = String.valueOf(latitude) + "," +
        String.valueOf(longitude);

    placeApi.getDistance(myLocation, dest).subscribe(
        new Action1<MatrixApi>() {
          @Override
          public void call(MatrixApi matrixDistance) {
            holder.distanceText.setText(matrixDistance.getGetElements()
                .get(0).getMatrixResults().get(0).getDistance().getDistanceValue());
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "error getting ditance from Google API");
          }
        });
  }


  private void setRating(String value, PlaceViewHolder holder) {
    holder.starLayout.removeAllViews();
    float rating = 0;
    if (value != null) {
      rating = Float.valueOf(value);
    }

    int incr = 0;
    int starNumber = Math.round(rating);

    while (incr < starNumber) {
      ImageView pinkStar = new ImageView(layoutInflater.getContext());
      pinkStar.setImageResource(R.drawable.star_pink);
      holder.starLayout.addView(pinkStar);
      incr++;
    }

    while (incr < 5) {
      ImageView pinkStar = new ImageView(layoutInflater.getContext());
      pinkStar.setImageResource(R.drawable.star_grey);
      holder.starLayout.addView(pinkStar);
      incr++;
    }
  }

  @Override
  public int getItemCount() {
    return placeList.size();
  }

  public void addData(List<ResultApi.Place> data) {
    placeList.clear();
    if (data != null) {
      placeList.addAll(data);
    }
    notifyDataSetChanged();
  }

  public class PlaceViewHolder extends RecyclerView.ViewHolder {

    public PlaceViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @Bind(R.id.place_name) TextView placeName;
    @Bind(R.id.distance) TextView distanceText;
    @Bind(R.id.star_layout) LinearLayout starLayout;
  }

}
