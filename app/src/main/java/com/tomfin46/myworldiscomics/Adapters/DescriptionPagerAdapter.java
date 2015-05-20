package com.tomfin46.myworldiscomics.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tomfin46.myworldiscomics.Fragments.DescriptionFragment;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tom on 20/05/2015.
 */
public class DescriptionPagerAdapter  extends FragmentStatePagerAdapter {


    private JSONArray mSections;

    public DescriptionPagerAdapter(FragmentManager fm, JSONArray sections) {
        super(fm);

        mSections = sections;
    }

    @Override
    public Fragment getItem(int position) {
        DescriptionFragment fragment = new DescriptionFragment();

        try {
            JSONObject section = (JSONObject) mSections.get(position);
            Bundle args = new Bundle();
            args.putString(DescriptionFragment.ARG_SECTION, section.toString());
            fragment.setArguments(args);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return mSections.length();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        try {
            JSONObject desc = (JSONObject) mSections.get(position);
            title = desc.getString("Title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return WordUtils.capitalizeFully(title);
    }
}
