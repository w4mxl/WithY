package com.farbox.wml.withy.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.farbox.wml.withy.R;

import java.util.Random;

/**
 * Created by wml on 2/15/15 11:51.
 */
public class EmotionsView extends View { // 表情图片

    Bitmap bitmap_emotion = null;
    private boolean isEnd = true;

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    private final Paint mPaint = new Paint();
    private static final Random RNG = new Random();
    private Coordinate[] emotions = new Coordinate[40];
    int view_height = 0;
    int view_width = 0;
    private int[] emotionX = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    private int[] emotionY = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    private int[] emotionZ = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    public EmotionsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EmotionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void LoadEmotionImage() {
        Resources r = this.getContext().getResources();
        bitmap_emotion = ((BitmapDrawable) r.getDrawable(R.drawable.face_throwing_a_kiss)).getBitmap();
    }

    public void LoadEmotionImage(int intDrawable) {
        Resources r = this.getContext().getResources();
        bitmap_emotion = ((BitmapDrawable) r.getDrawable(intDrawable)).getBitmap();
    }

    public void setView(int height, int width) {
        view_height = height - 100;
        view_width = width - 50;
    }

    public void clearAllEmotions() {
        emotionX = new int[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        emotionY = new int[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        emotionZ = new int[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
    }

    /**
     * 随机生成位置
     */
    public void addRandomEmotion() {
        calculateNextCoordinate();
        for (int i = 0; i < 40; i++) {
            if (emotions[i] == null) {
                emotions[i] = new Coordinate(emotionX[i], emotionY[i]);
            } else {
                emotions[i].setXY(emotionX[i], emotionY[i]);
            }
            if (emotionY[i] >= view_height) {
                emotions[i] = null;
            }
        }
    }

    private void calculateNextCoordinate() {
        for (int i = 0; i < 40; i++) {
            if (emotionX[i] == 0) {
                emotionY[i] = RNG.nextInt(1000) - 1000;
                emotionX[i] = RNG.nextInt(view_width - 1) + 1;
                emotionZ[i] = RNG.nextInt(20) + 10;
            } else {
                emotionY[i] += emotionZ[i];
            }
        }
    }

    private class Coordinate {
        public int x;
        public int y;

        public Coordinate(int newX, int newY) {
            x = newX;
            y = newY;
        }

        public boolean equals(Coordinate other) {
            if (x == other.x && y == other.y) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Coordinate: [" + x + "," + y + "]";
        }

        public void setXY(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnd) {
            return;
        }
        boolean temp = true;
        for (int x = 0; x < 40; x += 1) {
            if (emotions[x] != null && bitmap_emotion != null && !bitmap_emotion.isRecycled()) {
                canvas.drawBitmap(bitmap_emotion, ((float) emotions[x].x),
                        ((float) emotions[x].y), mPaint);
                temp = false;
            }
        }
        if (temp) {
            isEnd = true;
        }
    }
}

