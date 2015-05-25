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
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.IssueResource;
import com.tomfin46.myworldiscomics.Fragments.IssueFragment;
import com.tomfin46.myworldiscomics.Fragments.NavigationDrawerFragment;
import com.tomfin46.myworldiscomics.Fragments.PlaceholderFragment;
import com.tomfin46.myworldiscomics.Fragments.ResourceListFragment;
import com.tomfin46.myworldiscomics.Helpers.ExtraTags;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;

public class IssueActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        IssueFragment.OnIssueFragmentInteractionListener,
        ResourceListFragment.OnResourceListFragmentInteractionListener {

        final static String TAG = "IssueActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavDrawerTeamFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int mResId;
    private IssueResource mResource;
    private LinkedList<String> mLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        mResId = getIntent().getIntExtra(ExtraTags.EXTRA_RES_ID, 0);

        mNavDrawerTeamFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mLabels = new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.nav_issue)));

        // Set up the drawer.
        mNavDrawerTeamFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                getResources().getStringArray(R.array.nav_issue));


        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);

        final Context c = this;
        final Gson gson = new Gson();
        BackboneService.Get(c, mResId, ResourceTypes.ResourcesEnum.Issue, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                spinner.setVisibility(View.GONE);

                mResource = gson.fromJson(response.toString(), IssueResource.class);

                Intent bioData = new Intent("fragmentupdater");
                bioData.putExtra(ExtraTags.EXTRA_ISSUE, mResource);
                bioData.putExtra(ExtraTags.EXTRA_FRAG_NUM, 3);
                sendBroadcast(bioData);

                mTitle = mResource.volume.name + " " + mResource.IssueNumberFormattedString;

                String filter = "Person_Credits,Character_Credits,Team_Credits,Location_Credits,Concept_Credits,Object_Credits,Story_Arc_Credits";
                BackboneService.Get(c, mResource.id, ResourceTypes.ResourcesEnum.Issue, filter, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        IssueResource issue = gson.fromJson(response.toString(), IssueResource.class);
                        mResource.person_credits = issue.person_credits;
                        mResource.character_credits = issue.character_credits;
                        mResource.team_credits = issue.team_credits;
                        mResource.location_credits = issue.location_credits;
                        mResource.concept_credits = issue.concept_credits;
                        mResource.object_credits = issue.object_credits;
                        mResource.story_arc_credits = issue.story_arc_credits;

                        if (mResource.person_credits.size() > 0) {
                            mLabels.add(getResources().getString(R.string.sec_issue_creators));
                        }
                        if (mResource.character_credits.size() > 0) {
                            mLabels.add(getResources().getString(R.string.sec_issue_characters));
                        }
                        if (mResource.team_credits.size() > 0) {
                            mLabels.add(getResources().getString(R.string.sec_issue_teams));
                        }
                        if (mResource.location_credits.size() > 0) {
                            mLabels.add(getResources().getString(R.string.sec_issue_locations));
                        }
                        if (mResource.concept_credits.size() > 0) {
                            mLabels.add(getResources().getString(R.string.sec_issue_concepts));
                        }
                        if (mResource.object_credits.size() > 0) {
                            mLabels.add(getResources().getString(R.string.sec_issue_objects));
                        }
                        if (mResource.story_arc_credits.size() > 0) {
                            mLabels.add(getResources().getString(R.string.sec_issue_story_arcs));
                        }

                        String[] l = mLabels.toArray(new String[mLabels.size()]);
                        // Set up the drawer.
                        mNavDrawerTeamFragment.setUp(
                                R.id.navigation_drawer,
                                (DrawerLayout) findViewById(R.id.drawer_layout),
                                l);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error Fetching Details for " + mResource.id + ": " + error.getMessage());
                    }
                });
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Fetching Details for " + mResId + ": " + error.getMessage());
                error.printStackTrace();
            }
        });

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = new PlaceholderFragment();

        if (mLabels == null) {
            if (mResource != null) {
                fragment = IssueFragment.newInstance(mResource);
            } else {
                fragment = new IssueFragment();
            }
            mTitle = "";
        }
        else {
            String label = mLabels.get(position);
            if (label.equalsIgnoreCase(getResources().getString(R.string.sec_res_bio))) {
                if (mResource != null) {
                    fragment = IssueFragment.newInstance(mResource);
                } else {
                    fragment = new IssueFragment();
                }
                mTitle = "";
            } else if (label.equalsIgnoreCase(getResources().getString(R.string.sec_issue_creators))) {
                if (mResource != null && mResource.person_credits.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.person_credits, ResourceTypes.ResourcesEnum.Person);
                    mTitle = mResource.volume.name + " " + mResource.IssueNumberFormattedString;
                } else {
                    fragment = new ResourceListFragment();
                }
            } else if (label.equalsIgnoreCase(getResources().getString(R.string.sec_issue_characters))) {
                if (mResource != null && mResource.character_credits.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.character_credits, ResourceTypes.ResourcesEnum.Character);
                    mTitle = mResource.volume.name + " " + mResource.IssueNumberFormattedString;
                } else {
                    fragment = new ResourceListFragment();
                }
            } else if (label.equalsIgnoreCase(getResources().getString(R.string.sec_issue_teams))) {
                if (mResource != null && mResource.team_credits.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.team_credits, ResourceTypes.ResourcesEnum.Team);
                    mTitle = mResource.volume.name + " " + mResource.IssueNumberFormattedString;
                } else {
                    fragment = new ResourceListFragment();
                }
            } else if (label.equalsIgnoreCase(getResources().getString(R.string.sec_issue_locations))) {
                if (mResource != null && mResource.location_credits.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.location_credits, ResourceTypes.ResourcesEnum.Location);
                    mTitle = mResource.volume.name + " " + mResource.IssueNumberFormattedString;
                } else {
                    fragment = new ResourceListFragment();
                }
            } else if (label.equalsIgnoreCase(getResources().getString(R.string.sec_issue_concepts))) {
                if (mResource != null && mResource.concept_credits.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.concept_credits, ResourceTypes.ResourcesEnum.Concept);
                    mTitle = mResource.volume.name + " " + mResource.IssueNumberFormattedString;
                } else {
                    fragment = new ResourceListFragment();
                }
            } else if (label.equalsIgnoreCase(getResources().getString(R.string.sec_issue_objects))) {
                if (mResource != null && mResource.object_credits.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.object_credits, ResourceTypes.ResourcesEnum.Object);
                    mTitle = mResource.volume.name + " " + mResource.IssueNumberFormattedString;
                } else {
                    fragment = new ResourceListFragment();
                }
            } else if (label.equalsIgnoreCase(getResources().getString(R.string.sec_issue_story_arcs))) {
                if (mResource != null && mResource.story_arc_credits.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.story_arc_credits, ResourceTypes.ResourcesEnum.StoryArc);
                    mTitle = mResource.volume.name + " " + mResource.IssueNumberFormattedString;
                } else {
                    fragment = new ResourceListFragment();
                }
            }
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
        if (!mNavDrawerTeamFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.issue, menu);
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
        if (id == R.id.itm_volume) {
            Intent intent = new Intent(this, VolumeActivity.class);
            intent.putExtra(ExtraTags.EXTRA_RES_ID, mResource.volume.id);
            startActivity(intent);
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