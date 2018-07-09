package com.travelbooks3.android.activity.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelbooks3.android.R;

/**
 * Created by system777 on 2017-06-26.
 */

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.ViewHolder> {
    private Context context;

    public FriendsRecyclerAdapter(Context context){
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public FriendsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater.from(context).inflate(R.layout.row_main_fragment_friends, null);
        return null;
    }

    @Override
    public void onBindViewHolder(FriendsRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
