package com.albert.gestureanimation.vateview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by feiwanghua on 2017/2/24.
 */

public class BitmapUtil {

    public static Bitmap getBitmap(Context context,int bitmapResources){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResources);
        return getBitmap(bitmap,450,600);
    }

    public static Bitmap getBlurBitmap(Context context,int bitmapResources){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResources);
        bitmap = getBitmap(bitmap, 270, 360);
        if (Build.VERSION.SDK_INT > 16) {
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(25 /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
        }
        return bitmap;
    }

    public static Bitmap getBitmap(Bitmap bitmap,int maxWidth , int maxHeight){
        if(bitmap.getWidth()>maxWidth){
            bitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, bitmap.getHeight()*maxWidth/bitmap.getWidth(), true);
        }
        if(bitmap.getHeight()>maxHeight){
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*maxHeight/bitmap.getHeight(), maxHeight, true);
        }
        return bitmap;
    }
}
