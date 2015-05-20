package com.tomfin46.myworldiscomics.Activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomfin46.myworldiscomics.Adapters.SearchResultsPagerAdapter;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.Fragments.ResourceListFragment;
import com.tomfin46.myworldiscomics.Helpers.ExtraTags;
import com.tomfin46.myworldiscomics.Helpers.SlidingTab.SlidingTabLayout;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultsActivity extends ActionBarActivity
        implements ResourceListFragment.OnResourceListFragmentInteractionListener {

    final static String TAG = "SearchResultsActivity";

    private SearchResultsPagerAdapter mPagerAdapter;
    private ViewPager mPager;
    private ProgressBar mProgressBar;
    private SlidingTabLayout mSlidingTab;
    private LinearLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        mRootLayout = (LinearLayout) findViewById(R.id.root_list_layout);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mSlidingTab = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);

            getSupportActionBar().setTitle(getResources().getString(R.string.search_title) + ": \"" + WordUtils.capitalizeFully(query.trim()) + "\"");

            BackboneService.Search(this, query, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    mPagerAdapter = new SearchResultsPagerAdapter(getSupportFragmentManager(), processResponse(response));
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

                    mProgressBar.setVisibility(View.GONE);
                    mRootLayout.setVisibility(View.VISIBLE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error Searching for " + query + ": " + error.getMessage());
                    error.printStackTrace();
                    //TODO Handle TimeoutError etc
                }
            });
        }
    }

    private Map<Integer, Pair<ResourceTypes.ResourcesEnum, List<BaseResource>>> processResponse(JSONArray response) {
        Type listType = new TypeToken<ArrayList<BaseResource>>() {}.getType();
        List<BaseResource> resultsList = new Gson().fromJson(response.toString(), listType);

        Map<String, List<BaseResource>> resMap = new HashMap<>();

        for (final BaseResource res : resultsList) {
            String resType = res.resource_type;
            List<BaseResource> l = resMap.get(resType);
            if (l != null) {
                l.add(res);
            } else {
                l = new ArrayList<BaseResource>() {{
                    add(res);
                }};
            }
            resMap.put(resType, l);
        }

        Map<Integer, Pair<ResourceTypes.ResourcesEnum, List<BaseResource>>> m = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, List<BaseResource>> e : resMap.entrySet()) {
            ResourceTypes.ResourcesEnum resEnum = ResourceTypes.getResourceEnum(e.getKey());
            Pair p = new Pair<>(resEnum, e.getValue());
            m.put(i, p);
            ++i;
        }

        return m;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResourceClick(int resId, ResourceTypes.ResourcesEnum resourceType) {
        Intent intent = new Intent(this, ResourceTypes.GetResourceActivityClass(resourceType));
        intent.putExtra(ExtraTags.EXTRA_RES_ID, resId);
        intent.putExtra(ExtraTags.EXTRA_RES_TYPE, resourceType);
        startActivity(intent);
    }
}
