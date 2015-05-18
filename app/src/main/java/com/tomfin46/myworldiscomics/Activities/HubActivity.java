package com.tomfin46.myworldiscomics.Activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.Fragments.CharacterFragment;
import com.tomfin46.myworldiscomics.Fragments.HubFragment;
import com.tomfin46.myworldiscomics.Fragments.ResourceListFragment;
import com.tomfin46.myworldiscomics.R;

import java.util.List;

public class HubActivity extends ActionBarActivity implements
        HubFragment.OnHubFragmentInteractionListener,
        CharacterFragment.OnCharacterFragmentInteractionListener,
        ResourceListFragment.OnResourceListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            HubFragment firstFragment = new HubFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment, firstFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hub, menu);
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
    public void onCharacterClicked(int id) {
        CharacterFragment characterFragment = CharacterFragment.newInstance(id);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, characterFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public <T extends BaseResource> void onResourceListClicked(String listTitle, List<T> resources, ResourceTypes.ResourcesEnum resourcesType) {
        ResourceListFragment resourceListFragment = ResourceListFragment.newInstance(listTitle, resources, resourcesType);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, resourceListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onTeamClick(int id) {

    }
}
