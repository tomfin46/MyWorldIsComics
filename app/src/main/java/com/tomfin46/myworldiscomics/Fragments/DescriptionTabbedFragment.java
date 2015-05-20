package com.tomfin46.myworldiscomics.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tomfin46.myworldiscomics.Adapters.DescriptionPagerAdapter;
import com.tomfin46.myworldiscomics.Helpers.SlidingTab.SlidingTabLayout;
import com.tomfin46.myworldiscomics.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDescriptionListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DescriptionTabbedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionTabbedFragment extends Fragment {
    private static final String ARG_SECTIONS = "sections";

    private JSONArray mSections;

    private OnDescriptionListFragmentInteractionListener mCallback;

    private ProgressBar mSpinner;
    private LinearLayout mRootListLayout;
    private DescriptionPagerAdapter mPagerAdapter;
    private ViewPager mPager;
    private SlidingTabLayout mSlidingTab;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sections Parameter 1.
     * @return A new instance of fragment DescriptionTabbedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescriptionTabbedFragment newInstance(JSONArray sections) {
        DescriptionTabbedFragment fragment = new DescriptionTabbedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTIONS, sections.toString());
        fragment.setArguments(args);
        return fragment;
    }

    public DescriptionTabbedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                mSections = new JSONArray(getArguments().getString(ARG_SECTIONS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_description_tabs, container, false);

        mSpinner = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mRootListLayout = (LinearLayout) rootView.findViewById(R.id.root_list_layout);

        mPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        mSlidingTab = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);

        if (mSections != null) {
            updateFragment();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnDescriptionListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDescriptionListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private void updateFragment() {

        mPagerAdapter = new DescriptionPagerAdapter(getActivity().getSupportFragmentManager(), mSections);
        mPager.setAdapter(mPagerAdapter);
        mSlidingTab.setViewPager(mPager);
        mSlidingTab.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.primary_dark);
            }

            @Override
            public int getDividerColor(int position) {
                return 0;
            }
        });

        mSpinner.setVisibility(View.GONE);
        mRootListLayout.setVisibility(View.VISIBLE);
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
    public interface OnDescriptionListFragmentInteractionListener {
    }

}
