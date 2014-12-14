package com.NortrupDevelopment.PropertyBook.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.adapters.SeachAdapters.SerialNumberAdapter;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by andy on 12/9/14.
 */
public class NSNDetailFragment extends Fragment {

  private RecyclerView mRecyclerView;
  private RecyclerView.LayoutManager mLayoutManager;

  public static final String PARENT_NSN_ARG = "parentNSN";
  public static final String PARENT_LIN_ARG = "parentLIN";
  public static final String PARENT_SUB_LIN_ARG = "parentSubLIN";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent,
    Bundle savedInstanceState)
  {
    View result = inflater.inflate(R.layout.fragment_nsn_detail, parent, false);

    mRecyclerView =
        (RecyclerView) mRecyclerView.findViewById(R.id.serial_number_list);
    mRecyclerView.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);

    String nsn_key = getArguments().getString(PARENT_NSN_ARG);
    String lin_key = getArguments().getString(PARENT_LIN_ARG);
    String sublin_key = getArguments().getString(PARENT_SUB_LIN_ARG);

    Realm realm = Realm.getInstance(getActivity(),
        RealmDefinition.PRODUCTION_REALM);

    RealmResults<SerialNumber> serialNumbers =
        realm.where(SerialNumber.class).equalTo("stockNumber.nsn", nsn_key)
        .equalTo("stockNumber.parentLineNumber.lin", lin_key)
        .equalTo("stockNumber.parentLineNumber.sublin", sublin_key)
        .findAll();

    mRecyclerView.setAdapter(new SerialNumberAdapter(serialNumbers));

    return result;
  }
}
