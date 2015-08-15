package com.kartheek.healthybillion.task2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kartheek.healthybillion.R;

import java.util.List;

/**
 * Created by kartheek on 21/7/15.
 */
public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> {

    private static final String TAG = ItemsRecyclerAdapter.class.getSimpleName();
    private Context context;
    private List<String> monthsList;

    public ItemsRecyclerAdapter(Context context, List<String> monthsList) {
        this.context = context;
        this.monthsList = monthsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.months_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = monthsList.get(position);
        holder.tvTitle.setText(item);
    }

    @Override
    public int getItemCount() {
        return monthsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }

}