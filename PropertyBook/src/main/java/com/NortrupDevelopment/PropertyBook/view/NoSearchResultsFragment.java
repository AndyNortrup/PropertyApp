package com.NortrupDevelopment.PropertyBook.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;

/**
 * This class is used to display a simple fragment when no search results are
 * found.
 * Created by andy on 4/25/14.
 */

public class NoSearchResultsFragment extends Fragment {

    private static final String ARG = "searchTerm";

    public static NoSearchResultsFragment getInstance(String searchQuery) {
        NoSearchResultsFragment f = new NoSearchResultsFragment();

        Bundle args = new Bundle();
        args.putString(ARG, searchQuery);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        //get the search term from the argument
        String searchQuery = getArguments().getString(ARG);

        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);

        //Create a spannable text field with the search term highlighted green
        Spannable spannable = new SpannableString(
                getString(R.string.no_search_result) + searchQuery);

        int textPrefixLength = getString(R.string.no_search_result).length();

        spannable.setSpan(
                new ForegroundColorSpan(
                  getResources().getColor(R.color.primary_color)),
                textPrefixLength,
                textPrefixLength + searchQuery.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textView.setText(spannable);

        return textView;

    }

}
