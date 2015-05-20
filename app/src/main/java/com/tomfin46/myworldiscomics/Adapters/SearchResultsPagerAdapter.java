package com.tomfin46.myworldiscomics.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Pair;

import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.Fragments.ResourceListFragment;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 20/05/2015.
 */
public class SearchResultsPagerAdapter <T extends BaseResource> extends FragmentStatePagerAdapter {

    Map<Integer, Pair<ResourceTypes.ResourcesEnum, List<T>>> mResultsMap;

    public SearchResultsPagerAdapter(FragmentManager fm, Map<Integer, Pair<ResourceTypes.ResourcesEnum, List<T>>> results) {
        super(fm);

        mResultsMap = results;
    }

    @Override
    public Fragment getItem(int position) {
        ResourceListFragment fragment = new ResourceListFragment();
        Pair<ResourceTypes.ResourcesEnum, List<T>> pair = mResultsMap.get(position);
        Bundle args = new Bundle();
        args.putSerializable(ResourceListFragment.ARG_RESOURCES, (ArrayList<T>)pair.second);
        args.putSerializable(ResourceListFragment.ARG_RESOURCES_TYPE, pair.first);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return mResultsMap.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return WordUtils.capitalizeFully(ResourceTypes.getResourceTerm(mResultsMap.get(position).first).replace('_', ' '));
    }
}
