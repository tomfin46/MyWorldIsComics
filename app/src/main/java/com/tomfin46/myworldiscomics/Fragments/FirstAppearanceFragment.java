package com.tomfin46.myworldiscomics.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tomfin46.myworldiscomics.DataModel.Resources.IssueResource;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFirstAppearanceFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstAppearanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstAppearanceFragment extends Fragment {
    private static final String ARG_ISSUE = "issue";

    private IssueResource mIssue;

    private OnFirstAppearanceFragmentInteractionListener mCallback;

    private NetworkImageView mNetImageView;
    private TextView mTxtVolumeName;
    private TextView mTxtIssueNum;
    private TextView mTxtDesc;
    private ProgressBar mSpinner;
    private ScrollView mScrollView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param issue Parameter 1.
     * @return A new instance of fragment FirstAppearanceFragment.
     */
    public static FirstAppearanceFragment newInstance(IssueResource issue) {
        FirstAppearanceFragment fragment = new FirstAppearanceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ISSUE, issue);
        fragment.setArguments(args);
        return fragment;
    }

    public FirstAppearanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIssue = (IssueResource) getArguments().getSerializable(ARG_ISSUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_first_appearance, container, false);

        mNetImageView = (NetworkImageView) rootView.findViewById(R.id.img);
        mNetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onIssueClick(mIssue.id);
            }
        });

        mTxtVolumeName = (TextView) rootView.findViewById(R.id.volumeName);
        mTxtIssueNum = (TextView) rootView.findViewById(R.id.issueNum);
        mTxtDesc = (TextView) rootView.findViewById(R.id.desc);

        mSpinner = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);

        if (mIssue != null) {
            updateFragment();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFirstAppearanceFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFirstAppearanceFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private void updateFragment() {
        ImageLoader imageLoader = RequestQueueSingleton.getInstance(getActivity()).getImageLoader();
        mNetImageView.setImageUrl(mIssue.image.super_url, imageLoader);
        mTxtVolumeName.setText(mIssue.volume.name);
        mTxtIssueNum.setText(mIssue.IssueNumberFormattedString);
        mTxtDesc.setText(Html.fromHtml(mIssue.description));

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
    public interface OnFirstAppearanceFragmentInteractionListener {
        public void onIssueClick(int issueId);
    }

}
