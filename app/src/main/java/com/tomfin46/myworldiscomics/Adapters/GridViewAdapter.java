package com.tomfin46.myworldiscomics.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.TeamResource;
import com.tomfin46.myworldiscomics.R;
import com.tomfin46.myworldiscomics.Service.BackboneService;
import com.tomfin46.myworldiscomics.Service.RequestQueueSingleton;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Tom on 18/05/2015.
 */
public class GridViewAdapter<T extends BaseResource> extends ArrayAdapter {
    final static String TAG = "GridViewAdapter";

    private Context mContext;
    private List<T> mResources;
    private int mLayoutResId;
    private ResourceTypes.ResourcesEnum mResourcesType;

    public GridViewAdapter(Context c, int layoutResId, List<T> resources, ResourceTypes.ResourcesEnum resourcesType) {
        super(c, layoutResId, resources);
        mContext = c;
        mLayoutResId = layoutResId;
        mResources = resources;
        mResourcesType = resourcesType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ResourceHolder holder;
        final ImageLoader imageLoader = RequestQueueSingleton.getInstance(mContext).getImageLoader();

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResId, parent, false);

            holder = new ResourceHolder();
            holder.image = (NetworkImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.deck = (TextView) convertView.findViewById(R.id.deck);
            convertView.setTag(holder);
        } else {
            holder = (ResourceHolder) convertView.getTag();
        }

        final BaseResource resource = (BaseResource)mResources.get(position);
        BackboneService.Get(mContext, resource.id, mResourcesType, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                final Gson gson = new Gson();
                TeamResource resp = gson.fromJson(response.toString(), TeamResource.class);
                holder.image.setImageUrl(resp.image.super_url, imageLoader);
                holder.name.setText(resp.name);
                holder.deck.setText(resp.deck);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error Fetching Details for " + mResourcesType + " with id " + resource.id + ": " + error.getMessage());
            }
        });

        return convertView;
    }

    static class ResourceHolder {
        NetworkImageView image;
        TextView name;
        TextView deck;
    }
}
