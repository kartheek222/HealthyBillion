package com.kartheek.healthybillion;

import android.app.Application;
import android.content.Context;

import com.kartheek.healthybillion.volley.RequestManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


/**
 * Created by kartheek on 20/7/15.
 */
public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.init(this);
        initImageLoader(getApplicationContext());
    }

    /**
     * Init image loader with some parameters
     *
     * @param context
     */
    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        @SuppressWarnings("deprecation")
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .discCacheFileCount(300)
                        // default = device screen dimensions
                .threadPoolSize(5).threadPriority(Thread.MAX_PRIORITY).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheSize(80 * 1024 * 1024).discCacheFileCount(200).build();
        ImageLoader.getInstance().init(config);

    }

}
