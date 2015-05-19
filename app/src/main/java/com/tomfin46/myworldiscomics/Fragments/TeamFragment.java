package com.tomfin46.myworldiscomics.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tomfin46.myworldiscomics.DataModel.Resources.TeamResource;
import com.tomfin46.myworldiscomics.Helpers.ExtraTags;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RES = "resource";

    private OnFragmentInteractionListener mCallback;
    private TeamFragmentReceiver mReceiver;

    private TeamResource mResource;

    private NetworkImageView mNetImageView;
    private TextView mTxtName;
    private TextView mTxtAliases;
    private TextView mTxtDeck;
    private ProgressBar mSpinner;
    private ScrollView mScrollView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resource Parameter 1.
     * @return A new instance of fragment TeamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamFragment newInstance(TeamResource resource) {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RES, resource);
        fragment.setArguments(args);
        return fragment;
    }

    public TeamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResource = (TeamResource) getArguments().getSerializable(ARG_RES);
        }

        mReceiver = new TeamFragmentReceiver();
        getActivity().registerReceiver(mReceiver, new IntentFilter("fragmentupdater"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_team_bio, container, false);

        mNetImageView = (NetworkImageView) rootView.findViewById(R.id.img);
        mNetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.height =
                        params.height == getResources().getDimensionPixelSize(R.dimen.header_img_height)
                                ? ViewGroup.LayoutParams.WRAP_CONTENT
                                : getResources().getDimensionPixelSize(R.dimen.header_img_height);
                v.setLayoutParams(params);
            }
        });
        mTxtName = (TextView) rootView.findViewById(R.id.name);
        mTxtAliases = (TextView) rootView.findViewById(R.id.aliases);
        mTxtDeck = (TextView) rootView.findViewById(R.id.deck);

        mSpinner = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);


        if (mResource != null) {
            updateFragment();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        getActivity().unregisterReceiver(mReceiver);
    }

    private void updateFragment(){
        final ImageLoader imageLoader = RequestQueueSingleton.getInstance(getActivity()).getImageLoader();
        mNetImageView.setImageUrl(mResource.image.super_url, imageLoader);
        mTxtName.setText(mResource.name);
        mTxtAliases.setText(mResource.AliasesOneLine);
        mTxtDeck.setText(mResource.deck);

        mSpinner.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }

    public class TeamFragmentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int fragNo = intent.getIntExtra(ExtraTags.EXTRA_FRAG_NUM, 0);

            if (fragNo == 0) {
                mResource = (TeamResource) intent.getSerializableExtra(ExtraTags.EXTRA_TEAM);
                updateFragment();
            }
        }
    }
}
