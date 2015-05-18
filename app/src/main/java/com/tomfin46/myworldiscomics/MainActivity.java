package com.tomfin46.myworldiscomics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.tomfin46.myworldiscomics.Adapters.RecyclerViewAdapter;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.CharacterResource;
import com.tomfin46.myworldiscomics.Service.BackboneService;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;

import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    final static String TAG = "MainActivity";
    DescriptionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            PlaceholderFragment frag = new PlaceholderFragment();

            Bundle args = new Bundle();
            args.putInt(PlaceholderFragment.ARG_RES_ID, 1475);
            frag.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        final static String ARG_RES_ID = "resource_id";
        int mResId = -1;


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                mResId = savedInstanceState.getInt(ARG_RES_ID);
            }

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            NetworkImageView img = (NetworkImageView) rootView.findViewById(R.id.img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetworkImageView net = (NetworkImageView) v;
                    ViewGroup.LayoutParams params = v.getLayoutParams();
                    params.height = params.height == getResources().getDimensionPixelSize(R.dimen.header_img_height) ? ViewGroup.LayoutParams.WRAP_CONTENT : getResources().getDimensionPixelSize(R.dimen.header_img_height);
                    v.setLayoutParams(params);
                }
            });
            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();

            Bundle args = getArguments();
            if (args != null) {
                // Set article based on argument passed in
                updateResourceDetails(args.getInt(ARG_RES_ID));
            } else if (mResId != -1) {
                // Set article based on saved instance state defined during onCreateView
                updateResourceDetails(mResId);
            }
        }

        public void updateResourceDetails(final int resId) {

            final NetworkImageView img = (NetworkImageView) getActivity().findViewById(R.id.img);
            final TextView name = (TextView) getActivity().findViewById(R.id.name);
            final TextView realName = (TextView) getActivity().findViewById(R.id.realName);
            final TextView aliases = (TextView) getActivity().findViewById(R.id.aliases);
            final TextView birth = (TextView) getActivity().findViewById(R.id.birth);
            final TextView issueCount = (TextView) getActivity().findViewById(R.id.issueCount);
            final TextView deck = (TextView) getActivity().findViewById(R.id.deck);
            final TextView desc = (TextView) getActivity().findViewById(R.id.desc);

            final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.teams);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

            final ProgressBar spinner = (ProgressBar) getActivity().findViewById(R.id.progressBar);
            final ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.scrollView);
            mResId = resId;

            BackboneService.Get(getActivity(), resId, ResourceTypes.ResourcesEnum.Character, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    spinner.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);

                    final Gson gson = new Gson();
                    CharacterResource character = gson.fromJson(response.toString(), CharacterResource.class);

                    ImageLoader imageLoader = RequestQueueSingleton.getInstance(getActivity()).getImageLoader();
                    img.setImageUrl(character.image.super_url, imageLoader);
                    name.setText(character.name);
                    realName.setText(character.RealNameFormattedString);
                    aliases.setText(character.AliasesOneLine);
                    birth.setText(character.BirthFormattedString);
                    issueCount.setText(Integer.toString(character.count_of_issue_appearances));
                    deck.setText(character.deck);

                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), character.teams, R.layout.grid_view_item, ResourceTypes.ResourcesEnum.Team);
                    recyclerView.setAdapter(adapter);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error Fetching Details for " + resId + ": " + error.getMessage());
                }
            });
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            outState.putInt(ARG_RES_ID, mResId);
        }
    }

}
