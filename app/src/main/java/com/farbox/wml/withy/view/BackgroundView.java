package com.farbox.wml.withy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.farbox.wml.withy.R;

/**
 * Created by wml on 2/15/15 12:06.
 */
public class BackgroundView extends View {
    private static final String TAG = "BackgroundView";
    // 屏幕的高度和宽度
    int view_height = 0;
    int view_width = 0;
    Bitmap bmp = null;

    /**
     * 构造器
     */
    public BackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置当前窗体的实际高度和宽度
     */
    public void SetView(int height, int width) {
        view_height = height;
        view_width = width;
    }

    public void SetBitmap(Bitmap bitmap) {
        bmp = bitmap;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        MyLog.d(TAG, view_width + " " + view_height);
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.android);
        }
        Rect src = new Rect();// 图片
        Rect dst = new Rect();// 屏幕位置及尺寸
        // src 这个是表示绘画图片的大小
        src.left = 0; // 0,0
        src.top = 0;
        src.right = bmp.getWidth();// mBitDestTop.getWidth();,这个是桌面图的宽度，
        src.bottom = bmp.getHeight();// mBitDestTop.getHeight()/2;//
        // 这个是桌面图的高度的一半
        // 下面的 dst 是表示 绘画这个图片的位置
        dst.left = 0; // miDTX,//这个是可以改变的，也就是绘图的起点X位置
        dst.top = 0; // mBitQQ.getHeight();//这个是QQ图片的高度。 也就相当于 桌面图片绘画起点的Y坐标
        dst.right = view_width; // miDTX + mBitDestTop.getWidth();//
        // 表示需绘画的图片的右上角
        dst.bottom = view_height; // mBitQQ.getHeight() +
        // mBitDestTop.getHeight();//表示需绘画的图片的右下角
        canvas.drawBitmap(bmp, src, dst, null);// 这个方法 第一个参数是图片原来的大小，第二个参数是
        // 绘画该图片需显示多少。也就是说你想绘画该图片的某一些地方，而不是全部图片，第三个参数表示该图片绘画的位置        
        // src = null;
        dst = null;
    }
}