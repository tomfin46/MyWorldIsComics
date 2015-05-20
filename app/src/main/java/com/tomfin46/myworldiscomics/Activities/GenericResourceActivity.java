package com.tomfin46.myworldiscomics.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.CharacterResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.IssueResource;
import com.tomfin46.myworldiscomics.Fragments.DescriptionListFragment;
import com.tomfin46.myworldiscomics.Fragments.FirstAppearanceFragment;
import com.tomfin46.myworldiscomics.Fragments.GenericResourceFragment;
import com.tomfin46.myworldiscomics.Fragments.NavigationDrawerFragment;
import com.tomfin46.myworldiscomics.Fragments.PlaceholderFragment;
import com.tomfin46.myworldiscomics.Helpers.ExtraTags;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GenericResourceActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        GenericResourceFragment.OnGenericResourceFragmentInteractionListener,
        FirstAppearanceFragment.OnFirstAppearanceFragmentInteractionListener,
        DescriptionListFragment.OnDescriptionListFragmentInteractionListener {

    final static String TAG = "GenericResourceActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int mResId;
    private CharacterResource mResource;
    private ResourceTypes.ResourcesEnum mResourceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_resource);

        mResId = getIntent().getIntExtra(ExtraTags.EXTRA_RES_ID, 0);
        mResourceType = (ResourceTypes.ResourcesEnum) getIntent().getSerializableExtra(ExtraTags.EXTRA_RES_TYPE);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                getResources().getStringArray(R.array.nav_generic));

        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);

        final Context c = this;
        final Gson gson = new Gson();
        BackboneService.Get(c, mResId, mResourceType, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                spinner.setVisibility(View.GONE);

                mResource = gson.fromJson(response.toString(), CharacterResource.class);

                Intent bioData = new Intent("fragmentupdater");
                bioData.putExtra(ExtraTags.EXTRA_RES, mResource);
                bioData.putExtra(ExtraTags.EXTRA_FRAG_NUM, 5);
                sendBroadcast(bioData);

                mTitle = mResource.name;

                BackboneService.Get(c, mResource.first_appeared_in_issue.id, ResourceTypes.ResourcesEnum.Issue, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mResource.first_appeared_in_issue = gson.fromJson(response.toString(), IssueResource.class);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error Fetching Details for " + mResource.first_appeared_in_issue.id + ": " + error.getMessage());
                    }
                });

                if (mResource.description != null) {
                    BackboneService.Post(c, mResource.description, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray sections = null;
                            try {
                                sections = response.getJSONArray("Sections");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (sections != null) {
                                mResource.descriptionSections = sections;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error Mapping Description: " + error.getMessage());
                        }
                    });
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Fetching Details for " + mResId + ": " + error.getMessage());
                error.printStackTrace();
                //TODO Handle TimeoutError etc
            }
        });

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = new PlaceholderFragment();
        switch (position) {
            case 0:
                if (mResource != null) {
                    fragment = GenericResourceFragment.newInstance(mResource);
                } else {
                    fragment = new GenericResourceFragment();
                }
                mTitle = "";
                break;
            case 1:
                if (mResource != null && mResource.descriptionSections != null) {
                    fragment = DescriptionListFragment.newInstance(mResource.descriptionSections);
                } else if (mResource.description == null) {
                    fragment = DescriptionListFragment.newInstance(new JSONArray());
                } else {
                    fragment = new DescriptionListFragment();
                }
                break;
            case 2:
                if (mResource != null && mResource.first_appeared_in_issue.image != null) {
                    fragment = FirstAppearanceFragment.newInstance(mResource.first_appeared_in_issue);
                    mTitle = mResource.name;
                } else {
                    fragment = new FirstAppearanceFragment();
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
            getMenuInflater().inflate(R.menu.resource, menu);
            restoreActionBar();
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
    public void onIssueClick(int issueId) {
        Intent intent = new Intent(this, IssueActivity.class);
        intent.putExtra(ExtraTags.EXTRA_RES_ID, issueId);
        startActivity(intent);
    }
}
