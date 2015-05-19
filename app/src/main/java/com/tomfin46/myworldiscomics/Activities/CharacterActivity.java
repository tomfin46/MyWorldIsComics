package com.tomfin46.myworldiscomics.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.CharacterResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.IssueResource;
import com.tomfin46.myworldiscomics.Fragments.CharacterFragment;
import com.tomfin46.myworldiscomics.Fragments.FirstAppearanceFragment;
import com.tomfin46.myworldiscomics.Fragments.NavigationDrawerFragment;
import com.tomfin46.myworldiscomics.Fragments.ResourceListFragment;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;

import org.json.JSONObject;

public class CharacterActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        CharacterFragment.OnCharacterFragmentInteractionListener,
        ResourceListFragment.OnResourceListFragmentInteractionListener,
        FirstAppearanceFragment.OnFirstAppearanceFragmentInteractionListener{

    public final static String EXTRA_CHARACTER = "com.tomfin46.myworldiscomics.CHARACTER";
    public final static String EXTRA_FRAG_NUM = "com.tomfin46.myworldiscomics.FRAGNUM";

    final static String TAG = "CharacterActivity";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        Intent intent = getIntent();
        mResId = intent.getIntExtra(HubActivity.EXTRA_RES_ID, 1475);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);

        final Context c = this;
        final Gson gson = new Gson();
        BackboneService.Get(c, mResId, ResourceTypes.ResourcesEnum.Character, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                spinner.setVisibility(View.GONE);

                mResource = gson.fromJson(response.toString(), CharacterResource.class);

                Intent bioData = new Intent("fragmentupdater");
                bioData.putExtra(EXTRA_CHARACTER, mResource);
                bioData.putExtra(EXTRA_FRAG_NUM, 0);
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
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Fetching Details for " + mResId + ": " + error.getMessage());
            }
        });

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new PlaceholderFragment();
        switch (position) {
            case 0:
                if (mResource != null) {
                    fragment = CharacterFragment.newInstance(mResource);
                }
                else {
                    fragment = new CharacterFragment();
                }
                mTitle = "";
                break;
            case 1:
                if (mResource != null) {
                    fragment = ResourceListFragment.newInstance(mResource.teams, ResourceTypes.ResourcesEnum.Team);
                    mTitle = mResource.name;
                }
                else {
                    fragment = new ResourceListFragment();
                }
                break;
            case 2:
                if (mResource.first_appeared_in_issue.image != null) {
                    fragment = FirstAppearanceFragment.newInstance(mResource.first_appeared_in_issue);
                    mTitle = mResource.name;
                }
                else {
                    fragment = new FirstAppearanceFragment();
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
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_character, menu);
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
    public void onTeamClick(int teamId) {

    }

    @Override
    public void onIssueClick(int issueId) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance() {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

}
