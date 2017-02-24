package com.albert.gestureanimation.vateview.items;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.albert.gestureanimation.vateview.DensityUtil;

/**
 * Created by feiwh on 2017/2/24.
 */

public class IndexBarItem {
    private final String TAG = "IndexBarItem";
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int RED = Color.parseColor("#ff3c5e");
    private final int GREY = Color.parseColor("#e6e6e6");
    public IndexBarItem(Context context){
        mContext = context;
    }

    public void onMeasure(int width) {
        mWidth = width;
        mHeight = DensityUtil.dip2px(mContext,4);
    }

    public void onDraw(Canvas canvas,float percent) {
        Log.v(TAG,"onDraw");
        mPaint.setColor(GREY);
        canvas.drawRect(new Rect(0,0,mWidth,mHeight),mPaint);
        mPaint.setColor(RED);
        canvas.drawRect(new Rect(0,0,(int)(mWidth*percent),mHeight),mPaint);
    }
}
