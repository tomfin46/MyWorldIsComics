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
import com.tomfin46.myworldiscomics.Activities.CharacterActivity;
import com.tomfin46.myworldiscomics.DataModel.Resources.CharacterResource;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCharacterFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CharacterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterFragment extends Fragment {
    private static final String ARG_RES = "resource";

    private OnCharacterFragmentInteractionListener mCallback;
    private CharacterFragmentReceiver mReceiver;

    private CharacterResource mResource;

    private NetworkImageView mNetImageView;
    private TextView mTxtName;
    private TextView mTxtRealName;
    private TextView mTxtAliases;
    private TextView mTxtBirth;
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
    public static CharacterFragment newInstance(CharacterResource resource) {
        CharacterFragment fragment = new CharacterFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RES, resource);
        fragment.setArguments(args);
        return fragment;
    }

    public CharacterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResource = (CharacterResource) getArguments().getSerializable(ARG_RES);
        }

        mReceiver = new CharacterFragmentReceiver();
        getActivity().registerReceiver(mReceiver, new IntentFilter("fragmentupdater"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_character_bio, container, false);

        mNetImageView = (NetworkImageView) rootView.findViewById(R.id.img);
        mTxtName = (TextView) rootView.findViewById(R.id.name);
        mTxtRealName = (TextView) rootView.findViewById(R.id.realName);
        mTxtAliases = (TextView) rootView.findViewById(R.id.aliases);
        mTxtBirth = (TextView) rootView.findViewById(R.id.birth);
        mTxtIssueCount = (TextView) rootView.findViewById(R.id.issueCount);
        mTxtDeck = (TextView) rootView.findViewById(R.id.deck);

        mSpinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scrollView);

        if (mResource != null) {
            updateFragment();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnCharacterFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCharacterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        getActivity().unregisterReceiver(mReceiver);
    }

    private void updateFragment() {
        ImageLoader imageLoader = RequestQueueSingleton.getInstance(getActivity()).getImageLoader();
        mNetImageView.setImageUrl(mResource.image.super_url, imageLoader);
        mTxtName.setText(mResource.name);
        mTxtRealName.setText(mResource.RealNameFormattedString);
        mTxtAliases.setText(mResource.AliasesOneLine);
        mTxtBirth.setText(mResource.BirthFormattedString);
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
    public interface OnCharacterFragmentInteractionListener {
        //public <T extends BaseResource> void onResourceListClicked(String listTitle, List<T> resources, ResourceTypes.ResourcesEnum resourcesType);
    }

    public class CharacterFragmentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int fragNo = intent.getIntExtra(CharacterActivity.EXTRA_FRAG_NUM, 0);

            if (fragNo == 0) {
                mResource = (CharacterResource) intent.getSerializableExtra(CharacterActivity.EXTRA_CHARACTER);
                updateFragment();
            }
        }
    }

}
