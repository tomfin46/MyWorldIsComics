package com.tomfin46.myworldiscomics.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 19/05/2015.
 */
public class DescriptionListAdapter extends RecyclerView.Adapter<DescriptionListAdapter.ViewHolder> {

    private Context mContext;
    private JSONArray mSections;
    private Map<String, View> mViewMap;
    private int mLayoutResId;

    public DescriptionListAdapter(Context context, JSONArray sections, int layoutResId) {
        mContext = context;
        mSections = sections;
        mViewMap = new HashMap<>();
        mLayoutResId = layoutResId;

        for (int a = 0; a < mSections.length(); ++a) {
            try {
                JSONObject section = (JSONObject) mSections.get(a);
                View v = createContent(section);
                mViewMap.put(section.getString("Title"), v);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject section = (JSONObject) mSections.get(position);
            String title = section.getString("Title");

            holder.mTitle.setText(title);
            View view = mViewMap.get(title);
            if (view.getParent() == null) {
                holder.mContent.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mSections.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        LinearLayout mContent;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.title);
            mContent = (LinearLayout) itemView.findViewById(R.id.content);

            mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContent.getVisibility() == View.VISIBLE) {
                        mContent.setVisibility(View.GONE);
                    } else {
                        mContent.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        public void setContent(View view) {
            mContent.addView(view);
        }

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

    private View createQuote(JSONObject quote) throws JSONException {
        TextView txtQuote = new TextView(mContext);
        txtQuote.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtQuote.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtQuote.setTypeface(null, Typeface.BOLD);
        txtQuote.setText(quote.getString("Text"));

        return txtQuote;
    }
}
