package com.albert.gestureanimation.vateview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.albert.gestureanimation.R;
import com.albert.gestureanimation.vateview.items.FrontPhotoItem;
import com.albert.gestureanimation.vateview.items.IndexBarItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by feiwh on 2017/2/23.
 */

public class VoteView extends View{
    private final String TAG = "VoteView";
    private Context mContext;
    public static ExecutorService mFixedThreadPool = Executors.newFixedThreadPool(2);
    private int mWidth;
    private int mHeight;
    private FrontPhotoItem mFrontPhotoItem;
    private IndexBarItem mIndexBarItem;
    private GestureDetector mGestureDetector;
    public final static int PHOTO_UPDATE=100;
    private int mPhotoIndex = 0;
//    private long start;
//    private long end;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PHOTO_UPDATE:
                    postInvalidate();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private List<Integer> mBitmapList = new ArrayList<>();

    public VoteView(Context context, AttributeSet attrs) {
        super(context,attrs);
        mContext = context;
        setClickable(true);
        mGestureDetector = new GestureDetector(context , onGestureListener);
        mBitmapList.add( R.drawable.bbbbb);
        mBitmapList.add( R.drawable.aaaaa);
        mBitmapList.add(R.drawable.c);
        mBitmapList.add( R.drawable.d);
        mBitmapList.add( R.drawable.en1);
        mBitmapList.add( R.drawable.bbbbb);
        mBitmapList.add( R.drawable.aaaaa);
        mBitmapList.add(R.drawable.c);
        mBitmapList.add( R.drawable.d);
        mBitmapList.add(R.drawable.bbbbb);
        mBitmapList.add( R.drawable.aaaaa);
        mBitmapList.add(R.drawable.bbbbb);
        mBitmapList.add( R.drawable.aaaaa);
        mBitmapList.add(R.drawable.bbbbb);
        mFrontPhotoItem = new FrontPhotoItem(context,mHandler,mBitmapList.get(mPhotoIndex),mBitmapList.get(mPhotoIndex+1),mBitmapList.get(mPhotoIndex+2));
        mIndexBarItem = new IndexBarItem(context);
        mPhotoIndex++;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure:"+getMeasuredWidth()+" "+getMeasuredHeight());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth=getMeasuredWidth();
        mHeight=getMeasuredHeight();
        setMeasuredDimension(mWidth, mHeight);
        mFrontPhotoItem.onMeasure(mWidth,mHeight);
        mIndexBarItem.onMeasure(mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        long start = System.currentTimeMillis();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        super.onDraw(canvas);
        mFrontPhotoItem.onDraw(canvas);
        //mIndexBarItem.onDraw(canvas , (float)(mPhotoIndex-1)/(mBitmapList.size()-3));
        long end = System.currentTimeMillis();
        Log.d(TAG, "onDraw:"+(end-start));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent:"+event.toString());
        if(event.getAction() == MotionEvent.ACTION_UP){
            mFixedThreadPool.execute(mRunnable);
        }else{
            if(mPhotoIndex<mBitmapList.size()-2) {
                mGestureDetector.onTouchEvent(event);
            }
        }
        return super.onTouchEvent(event);
    }

    private final GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, "onScroll() distanceX = " + distanceX);
            mFrontPhotoItem.onScroll(e1, e2, distanceX, distanceY);
            postInvalidate();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "onFling() velocityX = " + velocityX);
            mFrontPhotoItem.onFling(e1, e2, velocityX, velocityY);
            postInvalidate();
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (!mFrontPhotoItem.isTouchAble()){
                mFrontPhotoItem.updatePosition();
                postInvalidate();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(mFrontPhotoItem.isFlingOut()){
                    mFrontPhotoItem.initPhotoItems(mBitmapList.get(mPhotoIndex+2),mPhotoIndex+3==mBitmapList.size());
                    mPhotoIndex++;
                    return;
                }
            }
        }
    };
}
