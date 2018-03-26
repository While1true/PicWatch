package com.master.picwatchlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.Serializable;

class MyPhotoView extends PhotoView implements OnScaleChangedListener{

    private Paint paint=null;
    private boolean finish=false;
    private Float textLength=null;
    private int dy=0;

    private CloseListener closeListener;

    public MyPhotoView(Context context) {
        this(context,null);
    }

    public MyPhotoView(Context context, AttributeSet attr) {
        this(context, attr,0);
    }

    public MyPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        initxx();
    }

    private void initxx() {
        setOnScaleChangeListener(this);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16,getResources().getDisplayMetrics()));
        textLength=paint.measureText("您可以单击/缩小返回");
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        dy= (int) ((fontMetrics.ascent - fontMetrics.descent) / 2 - fontMetrics.ascent);
    }

    @Override
    public void onScaleChange(float scaleFactor, float focusX, float focusY) {
        if (getScale() < 0.15f) {
            if (closeListener != null) {
                if (!finish) {
                    closeListener.close();
                    finish = true;
                }
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        paint.setColor(0x60666666);
//        canvas.drawRect(getWidth() / 2 - textLength / 2, getHeight() - textLength / 10, getWidth() / 2 + textLength / 2,getHeight(), paint);
//        paint.setColor(0xffffffff);
//        canvas.drawText("您可以单击/缩小返回", getWidth() / 2 - textLength / 2, (getHeight() - textLength / 20 + dy), paint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (getScale() <= 0.6f) {
                setScale(getMinimumScale(), true);
                return true;
            } else if (getScale() < 1f) {
                setScale(1f, true);
                return true;
            }

        }
        return super.dispatchTouchEvent(event);
    }

    public CloseListener getCloseListener() {
        return closeListener;
    }

    public void setCloseListener(CloseListener closeListener) {
        if(closeListener!=null){
            setMinimumScale(0.1f);
        }
        this.closeListener = closeListener;
    }

    public interface CloseListener {
        void close();
    }
}