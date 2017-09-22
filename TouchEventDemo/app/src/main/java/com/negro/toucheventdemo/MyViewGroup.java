package com.negro.toucheventdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.ContentFrameLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;

import java.util.Locale;

/**
 * Created by Negro
 * Date 2017/9/22
 * Email niulinguo@163.com
 */

public class MyViewGroup extends ContentFrameLayout {

    private static final String TAG = MyViewGroup.class.getSimpleName();

    private int mNumber;
    private TextPaint mPaint;

    {
        mNumber = Utils.createViewCount();
        mPaint = new TextPaint(Paint.DITHER_FLAG);
        mPaint.setColor(0xffff0000);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        mPaint.setTypeface(Typeface.DEFAULT);
    }

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, String.format(Locale.getDefault(), "number:%d, method:%s, event:%d", mNumber, "dispatchTouchEvent", ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, String.format(Locale.getDefault(), "number:%d, method:%s, event:%d", mNumber, "onInterceptTouchEvent", ev.getAction()));
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, String.format(Locale.getDefault(), "number:%d, method:%s, event:%d", mNumber, "onTouchEvent", event.getAction()));
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(String.valueOf(mNumber), 0, 50, mPaint);
    }
}
