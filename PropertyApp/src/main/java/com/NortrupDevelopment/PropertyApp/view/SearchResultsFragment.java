package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.ViewContractItemData;

/**
 * Created by andy on 3/8/14.
 */
public class SearchResultsFragment extends android.support.v4.app.ListFragment

{
    private int mFragmentType;
    private Cursor mCursor;
    private SimpleCursorAdapter mCursorAdapter;

    public static final int TYPE_LIN = 0;
    public static final int TYPE_NSN = 1;
    public static final int TYPE_SN = 2;
    public static final int TYPE_NOMENCLATURE = 3;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);


        return inflater.inflate(R.layout.fragment_search_results,
                container, false);

    }


    public void onStart() {
        super.onStart();

        if(mCursorAdapter != null) {
            getListView().setAdapter(mCursorAdapter);
        }


        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id)
            {
                mCursor.moveToPosition(position);

                Intent startNSNBrowser = new Intent(getActivity(),
                        LINDetailActivity.class);
                startNSNBrowser.putExtra(LINDetailActivity.LIN_ID_KEY,
                        mCursor.getString(mCursor.getColumnIndex(
                                ViewContractItemData.ALIAS_LIN_ID)));

                getActivity().startActivity(startNSNBrowser);
            }
        });

    }

    public void setFragmentType(int mFragmentType) {
        this.mFragmentType = mFragmentType;
    }

    public void setCursor(Cursor mCursor) {
        this.mCursor = mCursor;
    }


    public void init(int fragmentType, Cursor data, Context context) {

        setFragmentType(fragmentType);
        setCursor(data);
        mCursorAdapter = SearchCursorAdapterFactory.getCursorAdapter(
                data,
                fragmentType,
                context);

    }
}
