package com.albert.gestureanimation.vateview.items;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import com.albert.gestureanimation.vateview.DensityUtil;
import com.albert.gestureanimation.vateview.VoteView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by feiwh on 2017/2/24.
 */

public class BackPhotoItem {
    private final String TAG = "BackPhotoItem";
    private Context mContext;
    private Handler mHandler;
    private int mBmpWidth;
    private int mBmpHeight;
    private final int PADDING ;
    private Bitmap mBitmap;
    private Bitmap mBlurBitmap;
    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private Point mParentPoint = new Point();
    private Point mPoint = new Point();
    private static ExecutorService mFixedThreadPool;
    private int mThreadStatus = 2;
    private final int THREAD_RUNNER = 1;
    private final int THREAD_FINISH = 2;

    public BackPhotoItem(Context context , Handler handler, int bitmapResources){
        mContext = context;
        mHandler = handler;
        mPaint.setAntiAlias(true);
        PADDING = DensityUtil.dip2px(mContext, 10);
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(),bitmapResources);
        mBlurBitmap = BitmapFactory.decodeResource(mContext.getResources(),bitmapResources);
        initBitmap(mBlurBitmap);
        synchronized (BackPhotoItem.class) {
            if (mFixedThreadPool == null) {
                synchronized (BackPhotoItem.class) {
                    mFixedThreadPool = Executors.newFixedThreadPool(2);
                }
            }
        }
    }

    public void onMeasure(int width, int height,Point point ,int level) {
        mBmpWidth = width - 2*point.x - 2*PADDING;
        mBmpHeight = (height - 2*level*point.y)*mBmpWidth/(width - 2*point.x);
        mParentPoint.x=point.x;
        mParentPoint.y=point.y;
        mPoint.x = mParentPoint.x + PADDING;
        mPoint.y = mParentPoint.y - PADDING;
        if(level==2){
            mPoint.y = mPoint.y + PADDING/3;
            mBmpHeight=mParentPoint.y - mPoint.y;
        }
    }

    public void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        if(mBlurBitmap!=null&&!mBlurBitmap.isRecycled()&&mThreadStatus==THREAD_FINISH) {
            canvas.drawBitmap(mBlurBitmap, new Rect(0, 0, mBitmap.getWidth(), mBitmap.getWidth() * mBmpHeight / mBmpWidth),
                    new Rect(mPoint.x, mPoint.y, mPoint.x + mBmpWidth, mPoint.y + mBmpHeight), mPaint);
        }
    }

    public void initBitmap(Bitmap bitmap,Bitmap blurBitmap){
        mBitmap=bitmap;
        mBlurBitmap=blurBitmap;
    }
    public void initBitmap(final int bitmapResources){
        if(bitmapResources==-1){
            mBitmap = null;
            mBlurBitmap = null;
        }else {
            mThreadStatus = THREAD_RUNNER;
            mFixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    mBitmap = BitmapFactory.decodeResource(mContext.getResources(), bitmapResources);
                    mBlurBitmap = BitmapFactory.decodeResource(mContext.getResources(), bitmapResources);
                    initBitmap(mBlurBitmap);
                    mThreadStatus = THREAD_FINISH;
                    mHandler.sendEmptyMessage(VoteView.PHOTO_UPDATE);
                }
            });
        }
    }

    public void initBitmap(Bitmap bitmap){
        if (Build.VERSION.SDK_INT > 16) {
            RenderScript rs = RenderScript.create(mContext);
            Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(25 /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
        }
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }

    public Bitmap getBlurBitmap(){
        return mBlurBitmap;
    }

    public void recycler(){
        if(mBlurBitmap!=null) {
            mBlurBitmap.recycle();
        }
    }

    public Point getPoint(){
        return mPoint;
    }
}
