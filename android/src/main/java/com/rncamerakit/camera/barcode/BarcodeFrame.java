package com.rncamerakit.camera.barcode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import androidx.annotation.ColorInt;

import android.view.View;


import com.rncamerakit.R;

public class BarcodeFrame extends View {

    private static final int STROKE_WIDTH = 5;
    private static final int ANIMATION_SPEED = 8;
    // private static final int WIDTH_SCALE = 7;
    // private static final double HEIGHT_SCALE = 2.75;

    private double OFFSET_FRAME = 1;
    private double FRAME_HEIGHT = 1;

    private Paint dimPaint;
    private Paint framePaint;
    private Paint borderPaint;
    private Paint laserPaint;
    private Rect frameRect;
    private int width;
    private int height;
    private int borderMargin;

    private long previousFrameTime = System.currentTimeMillis();
    private int laserY;

    public BarcodeFrame(Context context, double offsetFrame, double frameHeight) {
        super(context);
        init(context);
        this.OFFSET_FRAME = offsetFrame;
        this.FRAME_HEIGHT = frameHeight;
    }

    private void init(Context context) {
        framePaint = new Paint();
        framePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        dimPaint = new Paint();
        dimPaint.setStyle(Paint.Style.FILL);
        dimPaint.setColor(context.getResources().getColor(R.color.bg_dark));
        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(STROKE_WIDTH);
        laserPaint = new Paint();
        laserPaint.setStyle(Paint.Style.STROKE);
        laserPaint.setStrokeWidth(STROKE_WIDTH);

        frameRect = new Rect();
        borderMargin = context.getResources().getDimensionPixelSize(R.dimen.border_length);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        int marginWidth = OFFSET_FRAME != 1 ? (int) (width * (1-OFFSET_FRAME)/2) : 0;
        int marginHeight = FRAME_HEIGHT != 1 ? (int) (height * (1-FRAME_HEIGHT)/2) : 0;

        frameRect.left = marginWidth;
        frameRect.right = width - marginWidth;
        frameRect.top = marginHeight;
        frameRect.bottom = height - marginHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long timeElapsed = (System.currentTimeMillis() - previousFrameTime);
        super.onDraw(canvas);
        canvas.drawRect(0, 0, width, height, dimPaint);
        canvas.drawRect(frameRect, framePaint);
        drawBorder(canvas);
        // drawLaser(canvas, timeElapsed);
        previousFrameTime = System.currentTimeMillis();
        this.invalidate(frameRect);
    }

    private void drawBorder(Canvas canvas) {
        canvas.drawLine(frameRect.left, frameRect.top, frameRect.left, frameRect.top + borderMargin, borderPaint);
        canvas.drawLine(frameRect.left, frameRect.top, frameRect.left + borderMargin, frameRect.top, borderPaint);
        canvas.drawLine(frameRect.left, frameRect.bottom, frameRect.left, frameRect.bottom - borderMargin, borderPaint);
        canvas.drawLine(frameRect.left, frameRect.bottom, frameRect.left + borderMargin, frameRect.bottom, borderPaint);
        canvas.drawLine(frameRect.right, frameRect.top, frameRect.right - borderMargin, frameRect.top, borderPaint);
        canvas.drawLine(frameRect.right, frameRect.top, frameRect.right, frameRect.top + borderMargin, borderPaint);
        canvas.drawLine(frameRect.right, frameRect.bottom, frameRect.right, frameRect.bottom - borderMargin, borderPaint);
        canvas.drawLine(frameRect.right, frameRect.bottom, frameRect.right - borderMargin, frameRect.bottom, borderPaint);
    }

    private void drawLaser(Canvas canvas, long timeElapsed) {
        if (laserY > frameRect.bottom || laserY < frameRect.top) laserY = frameRect.top;
        canvas.drawLine(frameRect.left + STROKE_WIDTH, laserY, frameRect.right - STROKE_WIDTH, laserY, laserPaint);
        laserY += (timeElapsed) / ANIMATION_SPEED;
    }

    public Rect getFrameRect() {
        return frameRect;
    }

    public void setFrameColor(@ColorInt int borderColor) {
        borderPaint.setColor(borderColor);
    }

    public void setLaserColor(@ColorInt int laserColor) {
        laserPaint.setColor(laserColor);
    }

    public void setOffsetFrame(double offsetFrame) {
        this.OFFSET_FRAME = offsetFrame;
    }

    public void setFrameHeight(double frameHeight) {
        this.FRAME_HEIGHT = frameHeight;
    }    
}
