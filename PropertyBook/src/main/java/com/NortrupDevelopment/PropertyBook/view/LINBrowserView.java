package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.NortrupDevelopment.PropertyBook.App;
import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.adapters.LineNumberBrowserAdapter;
import com.NortrupDevelopment.PropertyBook.bus.DefaultImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.DefaultSearchRequestedEvent;
import com.NortrupDevelopment.PropertyBook.dao.LineNumber;
import com.NortrupDevelopment.PropertyBook.presenter.LINBrowser;
import com.NortrupDevelopment.PropertyBook.presenter.LINBrowserPresenter;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.AbstractList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class LINBrowserView extends LinearLayout implements LINBrowser {
  @Inject LINBrowserPresenter mPresenter;
  @InjectView(R.id.lin_list) RecyclerView mListView;
  @InjectView(R.id.lin_loading_progress) LinearLayout mLoadingLayout;
  @InjectView(R.id.fab_menu) FloatingActionsMenu mFloatingActionsMenu;

  public LINBrowserView(Context context, AttributeSet attr) {
    super(context, attr);
    ((App) context.getApplicationContext()).component().inject(this);

    LayoutInflater inflater;
    inflater = LayoutInflater.from(context);
    inflater.inflate(R.layout.lin_browser, this, true);

    //Attach the view to the presenter
    mPresenter.attach(this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    ButterKnife.inject(this, getRootView());

    //Set the recycler view here so that we can avoid an NPE
    //https://github.com/Malinskiy/SuperRecyclerView/issues/25
    mListView.setLayoutManager(new LinearLayoutManager(getContext()));
    mPresenter.loadListContents();
  }

  /**
   * getRootView()
   * Shows the Loading progress bar and hides mCardList and mEmptyLayout.
   */
  @Override
  public void showLoadingProgressBar() {
    mListView.setVisibility(View.GONE);
    mLoadingLayout.setVisibility(View.VISIBLE);
  }

  /**
   * Shows the mCardList and hides the progress bar and empty list view.
   */
  @Override
  public void showList() {
    mLoadingLayout.setVisibility(View.GONE);
    mListView.setVisibility(View.VISIBLE);
  }

  /**
   * Takes a list of LINs, converts them into UI cards then creates and array
   * adapter for display in the CardList.
   *
   * @param lineNumbers List of LINs to be displayed.
   */
  public void setList(AbstractList<LineNumber> lineNumbers) {

    if (lineNumbers != null) {
      mListView.setAdapter(new LineNumberBrowserAdapter(lineNumbers));
    } else {
      mPresenter.importRequested();
    }

  }

  @OnClick(R.id.fab_import)
  public void requestImport() {
    Log.i("BrowserView", "Import Requested");
    mFloatingActionsMenu.collapse();
    EventBus.getDefault().post(new DefaultImportRequestedEvent());
  }

  @OnClick(R.id.fab_search)
  public void requestSearch() {
    Log.i("BrowserView", "Search Requested");
    mFloatingActionsMenu.collapse();
    EventBus.getDefault().post(new DefaultSearchRequestedEvent());
  }
}
