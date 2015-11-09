package com.example.fanilo.cityguide.model;

import com.example.fanilo.cityguide.R;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fanilo on 10/7/15.
 */
public class ResultApi {

  @SerializedName("results")
  List<Place> result;

  public enum TYPE {
    BAR("bar", R.drawable.ic_bar),
    BISTRO("restaurant", R.drawable.ic_bistro),
    CAFE("cafe", R.drawable.ic_cafe);

    TYPE(String type, int icon_value) {
      this.type = type;
      this.iconValue = icon_value;
    }

    private String type;
    private int    iconValue;

    public String getType() {
      return type;
    }

    public int getIconValue() {
      return iconValue;
    }
  }

  public List<Place> getResult() {
    return result;
  }

  public class Place {
    @SerializedName("name")
    String placeName;
    @SerializedName("rating")
    String ratingValue;
    @SerializedName("geometry")
    Geometry geometry;

    public Geometry getGeometry() {
      return geometry;
    }

    public String getPlaceName() {
      return placeName;
    }

    public String getRatingValue() {
      return ratingValue;
    }

    public class Geometry {
      @SerializedName("location")
      GoogleLocation googleLocation;

      public GoogleLocation getGoogleLocation() {
        return googleLocation;
      }

      public class GoogleLocation {
        @SerializedName("lat")
        String latitude;
        @SerializedName("lng")
        String longitude;

        public String getLatitude() {
          return latitude;
        }

        public String getLongitude() {
          return longitude;
        }
      }
    }

  }
}
