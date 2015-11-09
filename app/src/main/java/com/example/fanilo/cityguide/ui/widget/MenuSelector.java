package com.example.fanilo.cityguide.ui.widget;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import com.example.fanilo.cityguide.CityGuideApp;
import com.example.fanilo.cityguide.R;
import com.example.fanilo.cityguide.model.ResultApi;

/**
 * Created by fanilo on 10/7/15.
 */
public class MenuSelector extends FrameLayout {

  @Bind(R.id.slider) View     slider;
  @Bind(R.id.bar)    TextView barTextview;
  @Bind(R.id.cafe)   TextView cafeTextview;
  @Bind(R.id.bistro) TextView bistroTextview;

  private float dx = 0, x = 0;
  private int orangeColor;
  private int whiteColor;
  private Context ctx;

  public MenuSelector(Context context) {
    super(context);
    init(context);
  }

  public MenuSelector(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public MenuSelector(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public MenuSelector(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context);
  }

  private void init(Context context) {

    final View view = LayoutInflater.from(context).inflate(R.layout.slider_selector, this, true);

    orangeColor = getResources().getColor(R.color.lyft_orange);
    whiteColor = getResources().getColor(android.R.color.white);
    ctx = context;
    ButterKnife.bind(this, view);
  }

  @OnTouch(R.id.slider_rails)
  public boolean onTouch(View view, MotionEvent event) {

    float touchLocation = event.getX();
    ResultApi.TYPE currentPlaceType = ResultApi.TYPE.BAR;

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN: {
        x = touchLocation;
        dx = x - slider.getX();
      }
      break;
      case MotionEvent.ACTION_MOVE: {
        slider.setX(event.getX() - dx);
      }
      break;
      case MotionEvent.ACTION_UP: {
        float finalPos = 0;

        barTextview.setTextColor(whiteColor);
        bistroTextview.setTextColor(whiteColor);
        cafeTextview.setTextColor(whiteColor);

        if (touchLocation < (getWidth() * 1/3)){
          finalPos = barTextview.getX();
          barTextview.setTextColor(orangeColor);
          currentPlaceType = ResultApi.TYPE.BAR;
        } else if (touchLocation < (getWidth() * 2/3)) {
          finalPos = bistroTextview.getX();
          bistroTextview.setTextColor(orangeColor);
          currentPlaceType = ResultApi.TYPE.BISTRO;
        } else {
          //in this case we're on the last third of the view
          finalPos = cafeTextview.getX();
          cafeTextview.setTextColor(orangeColor);
          currentPlaceType = ResultApi.TYPE.CAFE;
        }

        if (ctx != null) {
          CityGuideApp.get(ctx).getAppComponent().ottoBus().post(currentPlaceType);;
        }

        ObjectAnimator.ofFloat(slider, "x", finalPos).setDuration(300).start();

      }
    }

    return true;
  }
}


