package com.albert.gestureanimation.vateview.items;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import com.albert.gestureanimation.vateview.DensityUtil;

/**
 * Created by feiwh on 2017/2/23.
 */

public class FrontPhotoItem {
    private final String TAG = "FrontPhotoItem";
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private int mBmpWidth;
    private int mBmpHeight;
    private Bitmap mBitmap;
    private Matrix mMatrix = new Matrix();
    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    //private Paint mShaderPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private Point mPoint = new Point();
    private float mRotate = 0;
    private int mRotatePx = 0;
    private final int X_MIN_PADDING ;
    private final int Y_MIN_PADDING ;
    private final int mSpeed ;
    private float mScale = 1;
    private BackPhotoItem mSecBackPhotoItem;
    private BackPhotoItem mThrBackPhotoItem;

    public FrontPhotoItem(Context context, Handler handler, int fstBitmap, int secBitmap, int thrBitmap){
        mContext = context;
        mPaint.setAntiAlias(true);
        //mShaderPaint.setAntiAlias(true);
        //mShaderPaint.setShader(new LinearGradient(0, 0, 0, 50, new int[] {Color.parseColor("#50474744"), Color.parseColor("#30474744"),Color.parseColor("#10474744")}, null, Shader.TileMode.CLAMP));
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(),fstBitmap);
        X_MIN_PADDING = DensityUtil.dip2px(mContext, 16);
        Y_MIN_PADDING = DensityUtil.dip2px(mContext, 29);
        mSpeed = DensityUtil.dip2px(mContext, 30);
        mSecBackPhotoItem = new BackPhotoItem(context ,handler ,secBitmap);
        mThrBackPhotoItem = new BackPhotoItem(context ,handler ,thrBitmap);
    }

    public void onMeasure(int width, int height) {
        Log.d(TAG, "onMeasure:"+width+" "+height);
        mWidth = width;
        mHeight = height;
        mBmpWidth = width - 2 * X_MIN_PADDING;
        mBmpHeight = height - 2 * Y_MIN_PADDING;
        if (mBmpWidth * mBitmap.getHeight() > mBitmap.getWidth()*mBmpHeight ) {
            mBmpWidth = mBmpHeight * mBitmap.getWidth() / mBitmap.getHeight();
        } else {
            mBmpHeight = mBmpWidth * mBitmap.getHeight() / mBitmap.getWidth();
        }
        mPoint.x = (width - mBmpWidth) / 2;
        mPoint.y = (height - mBmpHeight) / 2+Y_MIN_PADDING/2;
        mRotate = 0;
        mRotatePx = mWidth / 100;
        mScale = ((float) mBmpWidth) / mBitmap.getWidth();
        mSecBackPhotoItem.onMeasure(width, height, mPoint, 1);
        mThrBackPhotoItem.onMeasure(width, height, mSecBackPhotoItem.getPoint(), 2);
    }

    public void onDraw(Canvas canvas){
        Log.d(TAG, "onDraw");
        long start = System.currentTimeMillis();
        mThrBackPhotoItem.onDraw(canvas);
        long end = System.currentTimeMillis();
        Log.d(TAG, "onDraw 1:"+(end-start));
        start = System.currentTimeMillis();
        mSecBackPhotoItem.onDraw(canvas);
        end = System.currentTimeMillis();
        Log.d(TAG, "onDraw 2:"+(end-start));
        start = System.currentTimeMillis();
        mMatrix.reset();
        mMatrix.postScale(mScale, mScale);
        mMatrix.postRotate(mRotate, mWidth / 2, mHeight / 2);
        mMatrix.postTranslate(mPoint.x, mPoint.y);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        end = System.currentTimeMillis();
        Log.d(TAG, "onDraw 3:"+(end-start));
        //if(isTouchAble()){
         //   canvas.drawRect(new Rect(mPoint.x,mPoint.y-DensityUtil.dip2px(mContext,2),mPoint.x+mBmpWidth,mPoint.y),mShaderPaint);
        //}
    }

    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
        mPoint.x = mPoint.x - (int) distanceX;
        updateRotate();
    }

    public void onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        if(velocityX>mSpeed){
//            mSpeed = (int) Math.abs(velocityX);
//        }
    }

    public boolean isTouchAble(){
        return Math.abs(mPoint.x*2 + mBmpWidth - mWidth)<2;
    }

    public boolean isFlingOut(){
        return mPoint.x<-mWidth||mPoint.x>mWidth;
    }

    public void updatePosition(){
        if(mPoint.x<=mWidth/4&&mPoint.x>=-mWidth/4){
            mPoint.x = (mWidth - mBmpWidth)/2;
        }else if(mPoint.x<-mWidth/4){
            mPoint.x = mPoint.x - mSpeed;
        }else if(mPoint.x>mWidth/4){
            mPoint.x = mPoint.x + mSpeed;
        }
        updateRotate();
    }

    private void updateRotate(){
        if(mPoint.x<-mRotatePx *19){
            mRotate = -1;
        }else if(mPoint.x<-mRotatePx *17){
            mRotate = -2;
        }else if(mPoint.x<-mRotatePx *15){
            mRotate = -3;
        }else if(mPoint.x<-mRotatePx *13){
            mRotate = -4;
        }else if(mPoint.x<-mRotatePx *11){
            mRotate = -5;
        }else if(mPoint.x<-mRotatePx *5){
            mRotate = -5;
        }else if(mPoint.x<-mRotatePx *4){
            mRotate = -4;
        }else if(mPoint.x<-mRotatePx *3){
            mRotate = -3;
        }else if(mPoint.x<-mRotatePx *2){
            mRotate = -2;
        }else if(mPoint.x<-mRotatePx){
            mRotate = -1;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx *19){
            mRotate = 1;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx *17){
            mRotate = 2;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx *15){
            mRotate = 3;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx *13){
            mRotate = 4;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx *11){
            mRotate = 5;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx *5){
            mRotate = 5;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx *4){
            mRotate = 4;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx *3){
            mRotate = 3;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx *2){
            mRotate = 2;
        }else if(mPoint.x>mWidth - mBmpWidth+ mRotatePx){
            mRotate = 1;
        }else{
            mRotate=0;
        }
    }

    public void initPhotoItems(int bitmapResources,boolean last){
        mBitmap.recycle();
        if(last){
            mBitmap = mSecBackPhotoItem.getBlurBitmap();
        }else{
            mSecBackPhotoItem.recycler();
            mBitmap = mSecBackPhotoItem.getBitmap();
        }
        mSecBackPhotoItem.initBitmap(mThrBackPhotoItem.getBitmap(), mThrBackPhotoItem.getBlurBitmap());
        mThrBackPhotoItem.initBitmap(bitmapResources);
        onMeasure(mWidth, mHeight);
    }
}
