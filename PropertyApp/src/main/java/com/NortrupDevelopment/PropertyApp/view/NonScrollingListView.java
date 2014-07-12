package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.NortrupDevelopment.PropertyApp.R;

/**
 * The non-scrolling ListView overrides the default ListView so that when it is
 * rendered it's hight is large enough that it shows the entire list at once and
 * doesn't scroll.
 *
 * Created by andy on 6/28/14.
 */
public class NonScrollingListView extends ListView {

  public NonScrollingListView(Context c) {
    super(c);
  }

  public NonScrollingListView(Context c, AttributeSet attrs) {
    super(c, attrs);
    init(attrs);
  }

  public NonScrollingListView(Context c, AttributeSet attrs, int defStyle) {
    super(c, attrs, defStyle);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    TypedArray a=getContext().obtainStyledAttributes(
        attrs,
        R.styleable.NonScrollingListView);

    //Don't forget this
    a.recycle();
  }

  /*@Override
  public int getColumnWidth() {
    // This method will be called from onMeasure() too.
    // It's better to use getMeasuredWidth(), as it is safe in this case.
    final int totalHorizontalSpacing = getNumColumns() > 0 ? ( getNumColumns() - 1) * getHorizontalSpacing(): 0;
    return (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - totalHorizontalSpacing) / getNumColumns();
  }*/

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // Sets the padding for this view.
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    final int measuredWidth = getMeasuredWidth();
    final int childWidth = getWidth();
    int childHeight = 0;

    // If there's an adapter, use it to calculate the height of this view.
    final ListAdapter adapter = getAdapter();
    final int count;

    // There shouldn't be any inherent size (due to padding) if there are no child views.
    if (adapter == null || (count = adapter.getCount()) == 0) {
      setMeasuredDimension(0, 0);
      return;
    }

    // Get the first child from the adapter.
    final View child = adapter.getView(0, null, this);
    if (child != null) {
      // Set a default LayoutParams on the child, if it doesn't have one on its own.
      AbsListView.LayoutParams params = (AbsListView.LayoutParams) child.getLayoutParams();
      if (params == null) {
        params = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
            AbsListView.LayoutParams.WRAP_CONTENT);
        child.setLayoutParams(params);
      }

      // Measure the exact width of the child, and the height based on the width.
      // Note: the child takes care of calculating its height.
      int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
      int childHeightSpec = MeasureSpec.makeMeasureSpec(0,  MeasureSpec.UNSPECIFIED);
      child.measure(childWidthSpec, childHeightSpec);
      childHeight = child.getMeasuredHeight();
    }

    // Number of rows required to 'mTotal' items.
    final int rows = (int) Math.ceil((double) getAdapter().getCount());
    final int childrenHeight = childHeight * rows;
    //final int totalVerticalSpacing = rows > 0 ? (rows - 1) * getVerticalSpacing() : 0;

    // Total height of this view.
    final int measuredHeight = childrenHeight + getPaddingTop() + getPaddingBottom();// + totalVerticalSpacing;
    setMeasuredDimension(measuredWidth, measuredHeight);
  }
}
