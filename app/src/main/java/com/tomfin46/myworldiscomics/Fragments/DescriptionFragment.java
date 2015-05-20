package com.tomfin46.myworldiscomics.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tom on 20/05/2015.
 */
public class DescriptionFragment extends Fragment {

    public static final String ARG_SECTION = "section";

    private JSONObject mSection;
    private View mDescView;
    private Context mContext;

    private OnDescriptionFragmentInteractionListener mCallback;

    private ProgressBar mSpinner;
    private ScrollView mScrollView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param section Parameter 1.
     * @return A new instance of fragment DescriptionTabbedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescriptionFragment newInstance(String section) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        fragment.setArguments(args);
        return fragment;
    }

    public DescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                mContext = getActivity();
                mSection = new JSONObject(getArguments().getString(ARG_SECTION));
                mDescView = createContent(mSection);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_description, container, false);

        mSpinner = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);

        if (mSection != null) {
            updateFragment();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnDescriptionFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDescriptionFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private void updateFragment() {
        if (mDescView.getParent() != null) {
            ((ViewGroup) mDescView.getParent()).removeView(mDescView);
        }
        mScrollView.addView(mDescView);

        mSpinner.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    private View createContent(JSONObject descSection) throws JSONException {
        JSONArray queue = descSection.getJSONArray("ContentQueue");

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(createBlock(queue));

        return layout;
    }

    private View createBlock(JSONArray queue) throws JSONException {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (int a = 0; a < queue.length(); ++a) {
            JSONObject obj = (JSONObject) queue.get(a);
            String type = obj.getString("$type");
            if (type.contains("DescriptionParagraphDTO")) {
                layout.addView(createParagraph(obj));
            } else if (type.contains("FigureDTO")) {
                layout.addView(createFigure(obj));
            } else if (type.contains("ListDTO")) {
                layout.addView(createList(obj));
            } else if (type.contains("QuoteDTO")) {
                layout.addView(createQuote(obj));
            } else if (type.contains("SectionDTO")) {
                layout.addView(createSection(obj));
            }
        }

        return layout;
    }

    private View createSection(JSONObject section) throws JSONException {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(createHeader(section));
        JSONArray queue = section.getJSONArray("ContentQueue");
        layout.addView(createBlock(queue));

        return layout;
    }

    private View createHeader(JSONObject section) throws JSONException {
        TextView txtHeader = new TextView(mContext);
        switch (section.getString("Type")) {
            case "h3":
                txtHeader.setTypeface(null, Typeface.BOLD);
                break;
            case "h4":
                txtHeader.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                break;
        }
        txtHeader.setText(section.getString("Title"));
        return txtHeader;
    }

    private View createParagraph(JSONObject para) throws JSONException {
        TextView txtPara = new TextView(mContext);
        txtPara.setText(para.getString("Text"));
        return txtPara;
    }

    private View createFigure(JSONObject image) throws JSONException {
        ImageLoader imageLoader = RequestQueueSingleton.getInstance(mContext).getImageLoader();

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        NetworkImageView i = new NetworkImageView(mContext);
        i.setImageUrl(image.getString("ImageSource"), imageLoader);
        layout.addView(i);

        String caption = image.getString("Text");
        if (caption != null && !caption.equalsIgnoreCase("null")) {
            TextView txtCaption = new TextView(mContext);
            txtCaption.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            txtCaption.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txtCaption.setTypeface(null, Typeface.ITALIC);
            txtCaption.setText(caption);
            layout.addView(txtCaption);
        }

        return layout;
    }

    private View createList(JSONObject list) throws JSONException {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        JSONArray queue = list.getJSONArray("ContentQueue");
        for (int a = 0; a < queue.length(); ++a) {
            JSONObject obj = (JSONObject) queue.get(a);
            TextView bullet = new TextView(mContext);
            bullet.setText(Html.fromHtml("&#8226;&#32;" + obj.getString("Text")));
            layout.addView(bullet);
        }

        return layout;
    }

    private View createQuote(JSONObject quote) throws JSONException {
        TextView txtQuote = new TextView(mContext);
        txtQuote.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtQuote.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtQuote.setTypeface(null, Typeface.BOLD);
        txtQuote.setText(quote.getString("Text"));

        return txtQuote;
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
    public interface OnDescriptionFragmentInteractionListener {
    }
}
