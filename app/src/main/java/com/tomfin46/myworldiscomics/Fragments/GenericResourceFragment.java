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
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.Helpers.ExtraTags;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;

/**
 * A placeholder fragment containing a simple view.
 */
public class GenericResourceFragment <T extends BaseResource> extends Fragment {
    private static final String ARG_RES = "resource";

    private OnGenericResourceFragmentInteractionListener mCallback;
    private CharacterFragmentReceiver mReceiver;

    private T mResource;

    private NetworkImageView mNetImageView;
    private TextView mTxtName;
    private TextView mTxtIssueCount;
    private TextView mTxtDeck;
    private ProgressBar mSpinner;
    private ScrollView mScrollView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resource Parameter 1.
     * @return A new instance of fragment CharacterFragment.
     */
    public static <T extends BaseResource> GenericResourceFragment newInstance(T resource) {
        GenericResourceFragment fragment = new GenericResourceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RES, resource);
        fragment.setArguments(args);
        return fragment;
    }

    public GenericResourceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResource = (T) getArguments().getSerializable(ARG_RES);
        }

        mReceiver = new CharacterFragmentReceiver();
        getActivity().registerReceiver(mReceiver, new IntentFilter("fragmentupdater"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resource_bio, container, false);

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
        mTxtIssueCount = (TextView) rootView.findViewById(R.id.issueCount);
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
            mCallback = (OnGenericResourceFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnGenericResourceFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        getActivity().unregisterReceiver(mReceiver);
    }

    private void updateFragment() {
        final ImageLoader imageLoader = RequestQueueSingleton.getInstance(getActivity()).getImageLoader();
        mNetImageView.setImageUrl(mResource.image.super_url, imageLoader);
        mTxtName.setText(mResource.name);
        mTxtIssueCount.setText(Integer.toString(mResource.count_of_issue_appearances));
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
    public interface OnGenericResourceFragmentInteractionListener {
    }

    public class CharacterFragmentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int fragNo = intent.getIntExtra(ExtraTags.EXTRA_FRAG_NUM, 0);

            if (fragNo == 5) {
                mResource = (T) intent.getSerializableExtra(ExtraTags.EXTRA_RES);
                updateFragment();
            }
        }
    }
}
