package com.kartheek.healthybillion.task2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Krishna on 15/08/2015.
 */
public class FixedRecyclerview extends RecyclerView {
    public FixedRecyclerview(Context context) {
        super(context);
    }

    public FixedRecyclerview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedRecyclerview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        // check if scrolling up
        if (direction < 1) {
            boolean original = super.canScrollVertically(direction);
            return !original && getChildAt(0) != null && getChildAt(0).getTop() < 0 || original;
        }
        return super.canScrollVertically(direction);

    }
}