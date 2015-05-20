package com.tomfin46.myworldiscomics.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.CharacterResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.IssueResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.TeamResource;
import com.tomfin46.myworldiscomics.Fragments.CharacterFragment;
import com.tomfin46.myworldiscomics.Fragments.HubFragment;
import com.tomfin46.myworldiscomics.Fragments.NavigationDrawerFragment;
import com.tomfin46.myworldiscomics.Fragments.PlaceholderFragment;
import com.tomfin46.myworldiscomics.Fragments.ResourceListFragment;
import com.tomfin46.myworldiscomics.Helpers.ExtraTags;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HubActivity extends ActionBarActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        HubFragment.OnHubFragmentInteractionListener,
        CharacterFragment.OnCharacterFragmentInteractionListener,
        ResourceListFragment.OnResourceListFragmentInteractionListener {

    final static String TAG = "HubActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private List<IssueResource> mNewComics;
    private List<TeamResource> mTrendingTeams;
    private List<CharacterResource> mTrendingCharacters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        mTitle = getTitle();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                getResources().getStringArray(R.array.nav_hub));

        final Gson gson = new Gson();
        String sort = "date_last_updated:desc";
        String filter = "";
        int limit = 10;
        BackboneService.Get(this, ResourceTypes.ResourcesEnum.Characters, sort, filter, limit, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Type listType = new TypeToken<ArrayList<CharacterResource>>() {
                }.getType();
                mTrendingCharacters = gson.fromJson(response.toString(), listType);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Fetching Details for trending characters: " + error.getMessage());
            }
        });

        BackboneService.Get(this, ResourceTypes.ResourcesEnum.Issues, sort, filter, limit, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Type listType = new TypeToken<ArrayList<IssueResource>>() {
                }.getType();
                mNewComics = gson.fromJson(response.toString(), listType);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Fetching Details for new comics: " + error.getMessage());
            }
        });

        BackboneService.Get(this, ResourceTypes.ResourcesEnum.Teams, sort, filter, limit, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Type listType = new TypeToken<ArrayList<TeamResource>>() {
                }.getType();
                mTrendingTeams = gson.fromJson(response.toString(), listType);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Fetching Details for trending teams: " + error.getMessage());
            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new PlaceholderFragment();
        switch (position) {
            case 0:
                fragment = new HubFragment();
                break;
            case 1:
                if (mTrendingCharacters != null && mTrendingCharacters.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mTrendingCharacters, ResourceTypes.ResourcesEnum.Character);
                    mTitle = getResources().getString(R.string.sec_hub_chars);
                } else {
                    fragment = new ResourceListFragment();
                }
                break;
            case 2:
                if (mNewComics != null && mNewComics.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mNewComics, ResourceTypes.ResourcesEnum.Issue);
                    mTitle = getResources().getString(R.string.sec_hub_new_comics);
                } else {
                    fragment = new ResourceListFragment();
                }
                break;
            case 3:
                if (mTrendingTeams != null && mTrendingTeams.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mTrendingTeams, ResourceTypes.ResourcesEnum.Team);
                    mTitle = getResources().getString(R.string.sec_hub_teams);
                } else {
                    fragment = new ResourceListFragment();
                }
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_hub, menu);
            restoreActionBar();

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            return true;
        }

        return super.onCreateOptionsMenu(menu);
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
    public void onCharacterClicked(int id) {
        Intent intent = new Intent(this, CharacterActivity.class);
        intent.putExtra(ExtraTags.EXTRA_RES_ID, id);
        startActivity(intent);
    }

    @Override
    public void onResourceClick(int resId, ResourceTypes.ResourcesEnum resourceType) {
        Intent intent = new Intent(this, ResourceTypes.GetResourceActivityClass(resourceType));
        intent.putExtra(ExtraTags.EXTRA_RES_ID, resId);
        intent.putExtra(ExtraTags.EXTRA_RES_TYPE, resourceType);
        startActivity(intent);
    }
}
