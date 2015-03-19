package com.tomfin46.myworldiscomics.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CharacterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CharacterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CHAR_ID = "character_id";

    // TODO: Rename and change types of parameters
    private int charId;

    private OnFragmentInteractionListener mListener;

    public CharacterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param character_id Parameter 1.
     * @return A new instance of fragment CharacterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterFragment newInstance(int character_id) {
        CharacterFragment fragment = new CharacterFragment();
        Bundle args = new Bundle();
        args.putInt(CHAR_ID, character_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            charId = getArguments().getInt(CHAR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_character, container, false);

        BackboneService.Get(getActivity(), charId, ResourceTypes.ResourcesEnum.Character, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                initialiseView(rootView, response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    private void initialiseView(View rootView, JSONObject character) {

        String deck = "";
        String imgUrl = "";
        try {
            deck = character.getString("deck");
            imgUrl = character.getJSONObject("image").getString("super_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*ImageLoader imageLoader = RequestQueueSingleton.getInstance(getActivity()).getImageLoader();
        NetworkImageView networkImageView = (NetworkImageView) rootView.findViewById(R.id.char_img);
        networkImageView.setImageUrl(imgUrl, imageLoader);

        TextView textView = (TextView) rootView.findViewById(R.id.char_deck);
        textView.setText(deck);*/
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
