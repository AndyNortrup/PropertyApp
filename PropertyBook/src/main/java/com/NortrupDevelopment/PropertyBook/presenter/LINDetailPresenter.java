package com.NortrupDevelopment.PropertyBook.presenter;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.view.LINDetail;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * LINDetailPresenter handles the logic for the lookup and display of the
 * details of LINs for display.
 * Created by andy on 7/17/14.
 */
public class LINDetailPresenter {

  LINDetail mDetailView;

  public LINDetailPresenter(LINDetail detailView) {
    mDetailView = detailView;
  }

  public void linSearchRequested(String uuid) {
    Realm realm = RealmDefinition.getRealm(mDetailView.getContext(),
        RealmDefinition.PRODUCTION_REALM);
    RealmResults<LineNumber> lineNumbers = realm.where(LineNumber.class)
        .equalTo("uuid", uuid)
        .findAll();

    for(LineNumber lin : lineNumbers) {
      mDetailView.addLineNumber(lin);
    }
  }
}
