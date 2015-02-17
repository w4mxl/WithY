package com.farbox.wml.withy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.farbox.wml.withy.R;
import com.farbox.wml.withy.view.BackgroundView;
import com.farbox.wml.withy.view.EmotionsView;
import com.farbox.wml.withy.view.MultiTouchListener;

public class AboutActivity extends ActionBarActivity {

    EmotionsView ev;
    BackgroundView bv;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        iv = (ImageView) findViewById(R.id.iv);
        iv.setOnTouchListener(new MultiTouchListener(400) {
            @Override
            public boolean onMultiTouch(View v, MotionEvent event, int touchCount) {
                if(touchCount==5){
                    showEmotionsView();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private RefreshHandler mRedrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (ev == null || ev.isEnd()) {
                return;
            }
            ev.addRandomEmotion();
            ev.invalidate();
            sleep(50);
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    ;

    public void update() {
        ev.setEnd(false);
        ev.clearAllEmotions();
        ev.addRandomEmotion();
        mRedrawHandler.removeMessages(0);
        mRedrawHandler.sleep(200);
    }

    private void showEmotionsView() {
        // 获得表情雨视图,加载icon到内存(在布局文件中置入自定义EmotionsView)
        ev = (EmotionsView) findViewById(R.id.emotion_view); // 此处可实现表情图片的更替，具体判断来自发送的文本内容
        int intDrawable = R.drawable.face_throwing_a_kiss;

        ev.LoadEmotionImage(intDrawable);
        //BV.invalidate();
        ev.setVisibility(View.VISIBLE); // 获取当前屏幕的高和宽
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ev.setView(dm.heightPixels, dm.widthPixels);
        update();
    }
}
