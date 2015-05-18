package com.tomfin46.myworldiscomics.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tomfin46.myworldiscomics.Adapters.RecyclerViewAdapter;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnResourceListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResourceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResourceListFragment<T extends BaseResource> extends Fragment {
    private static final String ARG_LIST_TITLE = "list_title";
    private static final String ARG_RESOURCES = "resources";
    private static final String ARG_RESOURCES_TYPE = "resources_type";

    private String mListTitle;
    private List<T> mResources;
    private ResourceTypes.ResourcesEnum mResourcesType;

    private OnResourceListFragmentInteractionListener mCallback;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param listTitle Parameter 1.
     * @param resources Parameter 2.
     * @return A new instance of fragment ResourceListFragment.
     */
    public static <T extends BaseResource> ResourceListFragment newInstance(String listTitle, List<T> resources, ResourceTypes.ResourcesEnum resourcesType) {
        ResourceListFragment fragment = new ResourceListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LIST_TITLE, listTitle);
        args.putSerializable(ARG_RESOURCES, (ArrayList<T>)resources);
        args.putSerializable(ARG_RESOURCES_TYPE, resourcesType);
        fragment.setArguments(args);
        return fragment;
    }

    public ResourceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListTitle = getArguments().getString(ARG_LIST_TITLE);
            mResources = (List<T>) getArguments().getSerializable(ARG_RESOURCES);
            mResourcesType = (ResourceTypes.ResourcesEnum) getArguments().getSerializable(ARG_RESOURCES_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_resource_list, container, false);
        final ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        final LinearLayout rootListLayout = (LinearLayout) rootView.findViewById(R.id.root_list_layout);

        final TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(mListTitle);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.resources);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mResources, R.layout.grid_view_item, ResourceTypes.ResourcesEnum.Team);
        recyclerView.setAdapter(adapter);

        spinner.setVisibility(View.GONE);
        rootListLayout.setVisibility(View.VISIBLE);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnResourceListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnResourceListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
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
    public interface OnResourceListFragmentInteractionListener {
        public void onTeamClick(int id);
    }

}
