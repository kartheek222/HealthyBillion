package com.kartheek.healthybillion.task4;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kartheek.healthybillion.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.File;
import java.util.List;

/**
 * Created by kartheek on 21/7/15.
 */
public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder> {

    private static final String TAG = GalleryRecyclerAdapter.class.getSimpleName();
    private final DisplayImageOptions displayOptions;
    private Context context;
    private List<File> filesList;
    private final ImageLoader imageLoader;
    private View.OnClickListener listener;

    public GalleryRecyclerAdapter(Context context, List<File> monthsList, View.OnClickListener listener1) {
        this.context = context;
        this.filesList = monthsList;
        imageLoader = ImageLoader.getInstance();
        displayOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.weather).showImageForEmptyUri(R.drawable.weather)
                .showImageOnFail(R.drawable.weather).resetViewBeforeLoading(false).delayBeforeLoading(0).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer())
                .build();
        this.listener = listener1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_grid_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivImage.setOnClickListener(listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File item = filesList.get(position);
        holder.tvTitle.setText(item.getName());
        imageLoader.displayImage("file:/" + item.getAbsolutePath(), holder.ivImage, displayOptions);
        holder.ivImage.setTag(item.getAbsolutePath());
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

}