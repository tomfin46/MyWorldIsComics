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
import com.google.gson.reflect.TypeToken;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.CharacterResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.IssueResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.TeamResource;
import com.tomfin46.myworldiscomics.Fragments.DescriptionListFragment;
import com.tomfin46.myworldiscomics.Fragments.FirstAppearanceFragment;
import com.tomfin46.myworldiscomics.Fragments.NavigationDrawerFragment;
import com.tomfin46.myworldiscomics.Fragments.PlaceholderFragment;
import com.tomfin46.myworldiscomics.Fragments.ResourceListFragment;
import com.tomfin46.myworldiscomics.Fragments.TeamFragment;
import com.tomfin46.myworldiscomics.Helpers.ExtraTags;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TeamActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        TeamFragment.OnTeamFragmentInteractionListener,
        DescriptionListFragment.OnDescriptionListFragmentInteractionListener,
        FirstAppearanceFragment.OnFirstAppearanceFragmentInteractionListener,
        ResourceListFragment.OnResourceListFragmentInteractionListener {

    final static String TAG = "TeamActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavDrawerTeamFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int mResId;
    private TeamResource mResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        mResId = getIntent().getIntExtra(ExtraTags.EXTRA_RES_ID, 0);

        mNavDrawerTeamFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavDrawerTeamFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                getResources().getStringArray(R.array.nav_team));


        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);

        final Context c = this;
        final Gson gson = new Gson();
        BackboneService.Get(c, mResId, ResourceTypes.ResourcesEnum.Team, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                spinner.setVisibility(View.GONE);

                mResource = gson.fromJson(response.toString(), TeamResource.class);

                Intent bioData = new Intent("fragmentupdater");
                bioData.putExtra(ExtraTags.EXTRA_TEAM, mResource);
                bioData.putExtra(ExtraTags.EXTRA_FRAG_NUM, 1);
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

                final Type listType = new TypeToken<ArrayList<CharacterResource>>(){}.getType();
                String filter = "Characters,Character_Enemies,Character_Friends";
                BackboneService.Get(c, mResource.id, ResourceTypes.ResourcesEnum.Team, filter, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TeamResource team = gson.fromJson(response.toString(), TeamResource.class);
                        mResource.characters = team.characters;
                        mResource.character_enemies = team.character_enemies;
                        mResource.characters_friends = team.characters_friends;

                        List<String> labels = new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.nav_team)));
                        if (mResource.characters.size() > 0) {
                            labels.add(getResources().getString(R.string.sec_team_members));
                        }
                        if (mResource.character_enemies.size() > 0) {
                            labels.add(getResources().getString(R.string.sec_team_enemies));
                        }
                        if (mResource.characters_friends.size() > 0) {
                            labels.add(getResources().getString(R.string.sec_team_allies));
                        }

                        String[] l = labels.toArray(new String[labels.size()]);
                        // Set up the drawer.
                        mNavDrawerTeamFragment.setUp(
                                R.id.navigation_drawer,
                                (DrawerLayout) findViewById(R.id.drawer_layout),
                                l);

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
                    fragment = TeamFragment.newInstance(mResource);
                } else {
                    fragment = new TeamFragment();
                }
                mTitle = "";
                break;
            case 1:
                if (mResource != null && mResource.descriptionSections != null) {
                    fragment = DescriptionListFragment.newInstance(mResource.descriptionSections);
                    mTitle = mResource.name;
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
            case 3:
                if (mResource != null && mResource.characters.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.characters, ResourceTypes.ResourcesEnum.Character);
                    mTitle = mResource.name;
                } else {
                    fragment = new ResourceListFragment();
                }
                break;
            case 4:
                if (mResource != null && mResource.character_enemies.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.character_enemies, ResourceTypes.ResourcesEnum.Character);
                    mTitle = mResource.name;
                } else {
                    fragment = new ResourceListFragment();
                }
                break;
            case 5:
                if (mResource != null && mResource.characters_friends.size() > 0) {
                    fragment = ResourceListFragment.newInstance(mResource.characters_friends, ResourceTypes.ResourcesEnum.Character);
                    mTitle = mResource.name;
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
        if (!mNavDrawerTeamFragment.isDrawerOpen()) {
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

    @Override
    public void onResourceClick(int resId, ResourceTypes.ResourcesEnum resourceType) {
        Intent intent = new Intent(this, CharacterActivity.class);
        intent.putExtra(ExtraTags.EXTRA_RES_ID, resId);
        startActivity(intent);
    }
}
