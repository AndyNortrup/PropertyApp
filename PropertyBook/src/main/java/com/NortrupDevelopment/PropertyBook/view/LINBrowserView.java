package com.NortrupDevelopment.PropertyBook.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.adapters.LineNumberArrayAdapter;
import com.NortrupDevelopment.PropertyBook.bus.DefaultImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.DefaultLineNumberDetailEvent;
import com.NortrupDevelopment.PropertyBook.bus.DefaultSearchRequestedEvent;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.presenter.LINBrowser;
import com.NortrupDevelopment.PropertyBook.presenter.LINBrowserPresenter;

import java.util.AbstractList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.realm.RealmBaseAdapter;

public class LINBrowserView extends LinearLayout implements LINBrowser
{
  private LINBrowserPresenter mPresenter;

  @InjectView(R.id.lin_list) ListView mListView;
  @InjectView(R.id.lin_loading_progress) LinearLayout mLoadingLayout;

  public LINBrowserView(Context context, AttributeSet attr) {
    super(context, attr);

    LayoutInflater inflater;

    if(context instanceof Activity) {
      inflater = (LayoutInflater)((Activity) context).getLayoutInflater();
    } else {
      inflater = LayoutInflater.from(context);
    }

    inflater.inflate(R.layout.lin_browser, this, true);

    mPresenter = new LINBrowserPresenter(this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();

    ButterKnife.inject(this, getRootView());

    mPresenter.loadListContents();
  }

  /**getRootView()
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

      RealmBaseAdapter<LineNumber> adapter = new LineNumberArrayAdapter(getContext(),
          lineNumbers); //AutoUpdate

      mListView.setAdapter(adapter);
      mListView.setFastScrollEnabled(true);

      mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

            LineNumber lin =  ((LineNumberArrayAdapter)mListView.getAdapter())
                .getItem(position);
            EventBus.getDefault().post(new DefaultLineNumberDetailEvent(lin));
        }
      });
    } else {
      mPresenter.importRequested();
    }

  }

  @OnClick(R.id.fab_import)
  public void requestImport() {
    Log.i("BrowserView", "Import Requested");
    EventBus.getDefault().post(new DefaultImportRequestedEvent());
  }

  @OnClick(R.id.fab_search)
  public void requestSearch() {
    Log.i("BrowserView", "Search Requested");
    EventBus.getDefault().post(new DefaultSearchRequestedEvent());
  }
}
