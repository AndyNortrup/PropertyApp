package com.NortrupDevelopment.PropertyBook.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.bus.DefaultLineNumberDetailEvent;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;

import java.util.AbstractList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Adapter to display a list of LineNumbers for the LIN Browser
 * Created by andy on 2/27/15.
 */
public class LineNumberBrowserAdapter extends
    RecyclerView.Adapter<LineNumberBrowserAdapter.ViewHolder> {

  AbstractList<LineNumber> mLineNumbers;

  public LineNumberBrowserAdapter(AbstractList<LineNumber> lineNumbers) {
    mLineNumbers = lineNumbers;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(
        R.layout.list_item_lin_browser, parent, false);


    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.setData(mLineNumbers.get(position));
  }

  @Override
  public int getItemCount() {
    return mLineNumbers.size();
  }

  /**
   * ViewHolder for all of the View items
   */
  public static class ViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {

    @InjectView(R.id.lin)
    TextView mLineNumberView;
    @InjectView(R.id.nomenclature)
    TextView mNomenclature;
    @InjectView(R.id.sub_lin)
    TextView mSubLin;
    LineNumber mLineNumber;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(itemView);
      itemView.setOnClickListener(this);
    }

    protected void setData(LineNumber lineNumber) {
      mLineNumberView.setText(lineNumber.getLin());
      mNomenclature.setText(lineNumber.getNomenclature());

      if (TextUtils.isEmpty(lineNumber.getSubLin())) {
        mSubLin.setVisibility(View.GONE);
      } else {
        mSubLin.setText(lineNumber.getSubLin());
      }
    }

    @Override
    public void onClick(View v) {
      EventBus.getDefault().post(new DefaultLineNumberDetailEvent(mLineNumber));
    }
  }
}
