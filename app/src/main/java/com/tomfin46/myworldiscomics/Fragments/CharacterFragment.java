package com.tomfin46.myworldiscomics.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.CharacterResource;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;

import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCharacterFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CharacterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterFragment extends Fragment {
    final static String TAG = "CharacterFragment";

    private static final String ARG_RES_ID = "resource_id";

    private int mResId;

    private OnCharacterFragmentInteractionListener mCallback;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resId Parameter 1.
     * @return A new instance of fragment CharacterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterFragment newInstance(int resId) {
        CharacterFragment fragment = new CharacterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RES_ID, resId);
        fragment.setArguments(args);
        return fragment;
    }

    public CharacterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResId = getArguments().getInt(ARG_RES_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_character, container, false);

        final NetworkImageView img = (NetworkImageView) rootView.findViewById(R.id.img);
        final TextView name = (TextView) rootView.findViewById(R.id.name);
        final TextView realName = (TextView) rootView.findViewById(R.id.realName);
        final TextView aliases = (TextView) rootView.findViewById(R.id.aliases);
        final TextView birth = (TextView) rootView.findViewById(R.id.birth);
        final TextView issueCount = (TextView) rootView.findViewById(R.id.issueCount);
        final TextView deck = (TextView) rootView.findViewById(R.id.deck);

        final Button btnTeams = (Button) rootView.findViewById(R.id.btnTeams);

        final ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
        final ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);

        BackboneService.Get(getActivity(), mResId, ResourceTypes.ResourcesEnum.Character, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                spinner.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                final Gson gson = new Gson();
                final CharacterResource character = gson.fromJson(response.toString(), CharacterResource.class);

                ImageLoader imageLoader = RequestQueueSingleton.getInstance(getActivity()).getImageLoader();
                img.setImageUrl(character.image.super_url, imageLoader);
                name.setText(character.name);
                realName.setText(character.RealNameFormattedString);
                aliases.setText(character.AliasesOneLine);
                birth.setText(character.BirthFormattedString);
                issueCount.setText(Integer.toString(character.count_of_issue_appearances));
                deck.setText(character.deck);

                btnTeams.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallback != null) {
                            mCallback.onResourceListClicked("Teams", character.teams, ResourceTypes.ResourcesEnum.Team);
                        }
                    }
                });
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Fetching Details for " + mResId + ": " + error.getMessage());
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnCharacterFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCharacterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCharacterFragmentInteractionListener {
        // TODO: Update argument type and name
        public <T extends BaseResource> void onResourceListClicked(String listTitle, List<T> resources, ResourceTypes.ResourcesEnum resourcesType);
    }

}
