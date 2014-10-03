package com.NortrupDevelopment.PropertyBook.view;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;
import com.NortrupDevelopment.PropertyBook.presenter.LINDetailPresenter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Fragment presents one or many LIN Cards.
 * Created by andy on 7/17/14.
 */
public class LINDetailFragment extends Fragment
    implements LINDetail {

  LINDetailPresenter presenter;

  private SparseArray<LINCardWithNSNs> mLINCards;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    presenter = new LINDetailPresenter(this);

    presenter.linSearchRequested(
        getArguments().getInt(LINDetailActivity.LIN_ID_KEY));

  }

  /**
   * Inflate the fragment view.
   * @param inflater Inflater service to do the inflating
   * @param container Container that the fragment will exist in.
   * @param savedInstanceState Previous state information.
   * @return An inflated view for display
   */
  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState)
  {
    return inflater.inflate(R.layout.fragment_lin_detail, container, false);
  }

  /**
   * Return a copy of the Loader Manager to the Presenter.
   * @return A LoaderManager object for the presenter to use.
   */
  public LoaderManager getViewLoaderManager() {
    return getLoaderManager();
  }

  /**
   * Return a copy of the hosting Activity's context.
   *
   * @return A context object.
   */
  public Context getContext() {
    return getActivity().getBaseContext();
  }

  /**
   * Directs the view to take the provided LIN object and add it to the view
   * @param lineNumber LIN to be added to the view.
   */
  public void addLIN(LineNumber lineNumber) {
    if(mLINCards == null) {
      mLINCards = new SparseArray<LINCardWithNSNs>();
    }

    //only add the card if it doesn't exist.
    if(mLINCards.indexOfKey(lineNumber.getLinId()) < 0) {
      LINCardWithNSNs cardWithNSNs = createCard(lineNumber);
      mLINCards.put(lineNumber.getLinId(), cardWithNSNs);

      //Create the CardView and add it to the Layout
      CardView cardView = createCardView(cardWithNSNs);
      LinearLayout linearLayout =
          (LinearLayout) getView().findViewById(R.id.lin_detail_layout);
      linearLayout.addView(cardView);
    }
  }

  /**
   * Directs the view to add the give NSN to the given LIN.
   *
   * @param stockNumber NSN to add.
   * @param lineNumber LIN which it belongs to,
   */
  @Override
  public void addNSNtoLIN(StockNumber stockNumber, LineNumber lineNumber) {
    LINCardWithNSNs linCard = mLINCards.get(lineNumber.getLinId());
    linCard.getLIN().addNSN(stockNumber);
    linCard.setNSN();
  }

  /**
   * Creates a card model based on the provided LIN
   * @param lineNumber Information to be presented by the model.
   * @return A LINCardWithNSN object to be added to a card.
   */
  public LINCardWithNSNs createCard(LineNumber lineNumber) {
    //Create our card model
    LINCardWithNSNs cardWithNSNs = new LINCardWithNSNs(getActivity(), lineNumber);
    cardWithNSNs.updateProgressBar(false, false);
    return cardWithNSNs;
  }

  /**
   * Creates a CardView to be used in the layout.
   * @param card CardModel with information to be displayed in the card.
   * @return A CardView to be added to the layout.
   */
  private CardView createCardView(Card card) {
    //Create the card view
    CardView cardView = new CardView(getActivity());
    cardView.setCard(card);

    //Set the margins
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);


    //convert 12 dp to pixels
    int px12dp = (int) TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
    layoutParams.setMargins(px12dp, px12dp / 2, px12dp, px12dp / 2);

    cardView.setLayoutParams(layoutParams);
    return cardView;
  }
}
