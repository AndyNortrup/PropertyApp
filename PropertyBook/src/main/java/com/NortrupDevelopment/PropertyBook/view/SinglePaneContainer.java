package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.CurrentView;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.presenter.LINDetail;

/**
 * Serves as the container for the main activity.  Manages the back stack as
 * well as adding and removing views from the container.
 *
 * Created by andy on 1/8/15.
 */
public class SinglePaneContainer extends FrameLayout implements Container {

  LINBrowserView linBrowser;
  LINDetail linDetail;

  public SinglePaneContainer(Context context, AttributeSet attr) {
    super(context, attr);
  }

  /**
   * Runs after the view inflates
   */
  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    linBrowser = (LINBrowserView)getChildAt(0);
  }

  @Override
  public void showLineNumber(LineNumber lineNumber) {
    if(linBrowserAttached()) {
      removeViewAt(0);
      View.inflate(getContext(), R.layout.tabbed_lin_detail, this);
    }
    linDetail = (TabbedLINDetail)getChildAt(0);
    linDetail.addLineNumber(lineNumber);
    CurrentView.getInstance().setCurrentScreen(CurrentView.SCREEN_DETAIL);
  }

  public void showBrowser() {
    if(!linBrowserAttached()) {
      removeViewAt(0);
      addView(linBrowser);
      CurrentView.getInstance().setCurrentScreen(CurrentView.SCREEN_BROWSE);
    }
  }

  @Override
  public boolean onBackPressed() {
    if(linBrowserAttached()) {
      return false;
    }
    showBrowser();
    return true;
  }

  public boolean linBrowserAttached() {
    return linBrowser.getParent() != null;
  }
}
