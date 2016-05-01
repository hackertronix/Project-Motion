package com.execube.genesis.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.execube.genesis.R;
import com.execube.genesis.model.Movie;

/**
 * Created by Prateek Phoenix on 4/30/2016.
 */
public class DetailsFragment extends Fragment {
    public Intent intent;
    private TextView mTextView;
    private Movie mMovie;

    public DetailsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_detail,container,false);
        mTextView=(TextView)view.findViewById(R.id.placeholder_textView);

        intent= getActivity().getIntent();
        mMovie=intent.getExtras().getParcelable("PARCEL");

        mTextView.setText(mMovie.getTitle()+"\n"+mMovie.getOverview());

        return view;
    }
}
