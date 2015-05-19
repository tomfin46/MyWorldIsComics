package com.tomfin46.myworldiscomics.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tomfin46.myworldiscomics.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class HubFragment extends Fragment {

    OnHubFragmentInteractionListener mCallback;

    public interface OnHubFragmentInteractionListener {
        public void onCharacterClicked(int id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnHubFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHubFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hub, container, false);

        Button btnHawkeye = (Button) rootView.findViewById(R.id.btnHawkeye);
        btnHawkeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onCharacterClicked(1475);
                }
            }
        });

        return rootView;
    }
}
