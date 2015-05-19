package com.tomfin46.myworldiscomics.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tomfin46.myworldiscomics.Adapters.DescriptionListAdapter;
import com.tomfin46.myworldiscomics.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDescriptionListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DescriptionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTIONS = "sections";

    private JSONArray mSections;

    private OnDescriptionListFragmentInteractionListener mCallback;

    private ProgressBar mSpinner;
    private LinearLayout mRootListLayout;
    private RecyclerView mRecyclerView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sections Parameter 1.
     * @return A new instance of fragment DescriptionListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescriptionListFragment newInstance(JSONArray sections) {
        DescriptionListFragment fragment = new DescriptionListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTIONS, sections.toString());
        fragment.setArguments(args);
        return fragment;
    }

    public DescriptionListFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_description_list, container, false);

        mSpinner = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mRootListLayout = (LinearLayout) rootView.findViewById(R.id.root_list_layout);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.sections);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

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
        DescriptionListAdapter adapter = new DescriptionListAdapter(getActivity(), mSections, R.layout.description_item);
        mRecyclerView.setAdapter(adapter);

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
