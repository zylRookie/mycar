package com.example.zyl.dqcar.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class SquareImageView extends AppCompatImageView {

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //Log.e("SquareImageView", "onMeasure() height: " + height);
        if (height == 0)
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        else if (height == 1) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(width / 2, MeasureSpec.EXACTLY));
        } else
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

}