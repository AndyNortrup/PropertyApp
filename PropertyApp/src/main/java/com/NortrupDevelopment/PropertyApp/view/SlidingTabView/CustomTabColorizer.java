package com.NortrupDevelopment.PropertyApp.view.SlidingTabView;

import com.NortrupDevelopment.PropertyApp.R;

/**
 * Created by andy on 6/1/14.
 */
public class CustomTabColorizer implements SlidingTabLayout.TabColorizer {

  /**
   * @return return the color of the indicator used when {@code position} is selected.
   */
  public int getIndicatorColor(int position) {
    return R.color.property_app_dark_green;
  }

  /**
   * @return return the color of the divider drawn to the right of {@code position}.
   */
  public int getDividerColor(int position)
  {
    return R.color.property_app_dark_green;
  }
}
