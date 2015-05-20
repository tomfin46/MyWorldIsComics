package com.tomfin46.myworldiscomics.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.IssueResource;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Tom on 18/05/2015.
 */
public class RecyclerViewAdapter<T extends BaseResource> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    final static String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private List<T> mResources;
    private int mLayoutResId;
    private ResourceTypes.ResourcesEnum mResourcesType;
    private final ImageLoader mImageLoader;

    public RecyclerViewAdapter(Context context, final List<T> resources, int layoutResId, ResourceTypes.ResourcesEnum resourcesType) {
        mContext = context;
        mResources = resources;
        mLayoutResId = layoutResId;
        mResourcesType = resourcesType;
        mImageLoader = RequestQueueSingleton.getInstance(mContext).getImageLoader();


        for (int a=0; a<resources.size(); ++a) {
            final T resource = resources.get(a);
            final int finalA = a;
            BackboneService.Get(mContext, resource.id, resourcesType, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    final Gson gson = new Gson();
                    resources.set(finalA, (T) gson.fromJson(response.toString(), ResourceTypes.GetResourceClass(mResourcesType)));
                    notifyItemChanged(finalA);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error Fetching Details for " + mResourcesType + " with id " + resource.id + ": " + error.getMessage());
                }
            });
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, int position) {

        final T resource = mResources.get(position);
        if (resource.image != null) {
            holder.mImage.setImageUrl(resource.image.super_url, mImageLoader);
        }
        if (ResourceTypes.GetResourceClass(mResourcesType).equals(IssueResource.class)) {
            holder.mName.setText(((IssueResource)resource).IssueNumberFormattedString + ": " + ((IssueResource) resource).name);
            if (((IssueResource) resource).description != null) {
                holder.mDeck.setText(Html.fromHtml(((IssueResource) resource).description));
            }
        } else {
            holder.mName.setText(resource.name);
            holder.mDeck.setText(resource.deck);
        }
    }

    @Override
    public int getItemCount() {
        int size = mResources.size();
        return size;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView mImage;
        TextView mName;
        TextView mDeck;

        public ViewHolder(View itemView) {
            super(itemView);

            mImage = (NetworkImageView) itemView.findViewById(R.id.img);
            mName = (TextView) itemView.findViewById(R.id.name);
            mDeck = (TextView) itemView.findViewById(R.id.deck);
        }
    }
}
