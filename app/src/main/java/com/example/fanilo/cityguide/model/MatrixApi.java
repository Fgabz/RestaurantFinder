package com.example.fanilo.cityguide.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fanilo on 10/8/15.
 */
public class MatrixApi {

  @SerializedName("rows")
  List<Elements> getElements;

  public List<Elements> getGetElements() {
    return getElements;
  }

  public class Elements {
    @SerializedName("elements")
    List<MatrixResult> matrixResults;

    public List<MatrixResult> getMatrixResults() {
      return matrixResults;
    }
  }

  public class MatrixResult {
    @SerializedName("distance")
    Distance distance;

    public Distance getDistance() {
      return distance;
    }

    public class Distance {
      @SerializedName("text")
      String distanceValue;

      public String getDistanceValue() {
        return distanceValue;
      }
    }
  }
}
