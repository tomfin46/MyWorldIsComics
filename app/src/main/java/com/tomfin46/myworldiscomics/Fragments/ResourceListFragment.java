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

import com.tomfin46.myworldiscomics.Adapters.RecyclerViewAdapter;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.Helpers.RecyclerItemClickListener;
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
    public static final String ARG_RESOURCES = "resources";
    public static final String ARG_RESOURCES_TYPE = "resources_type";
    public static final String ARG_ITEM_RES_ID = "item_resource_id";

    private OnResourceListFragmentInteractionListener mCallback;

    private ProgressBar mSpinner;
    private LinearLayout mRootListLayout;
    private RecyclerView mRecyclerView;

    private List<T> mResources;
    private ResourceTypes.ResourcesEnum mResourcesType;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResourceListFragment.
     */
    public static <T extends BaseResource> ResourceListFragment newInstance(List<T> resources, ResourceTypes.ResourcesEnum resourcesType) {
        ResourceListFragment fragment = new ResourceListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESOURCES, (ArrayList<T>) resources);
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
            mResources = (List<T>) getArguments().getSerializable(ARG_RESOURCES);
            mResourcesType = (ResourceTypes.ResourcesEnum) getArguments().getSerializable(ARG_RESOURCES_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resource_list, container, false);

        mSpinner = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mRootListLayout = (LinearLayout) rootView.findViewById(R.id.root_list_layout);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.resources);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCallback.onResourceClick(mResources.get(position).id, mResourcesType);
            }
        }));

        if (mResources != null) {
            updateFragment();
        }

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

    private void updateFragment() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mResources, R.layout.new_comics_item, mResourcesType);
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
    public interface OnResourceListFragmentInteractionListener {
        public void onResourceClick(int resId, ResourceTypes.ResourcesEnum resourceType);
    }
}
