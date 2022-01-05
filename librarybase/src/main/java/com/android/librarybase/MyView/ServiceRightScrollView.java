package com.android.librarybase.MyView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

import androidx.core.widget.NestedScrollView;

public class ServiceRightScrollView extends NestedScrollView {
    private static final int size = 4;
    private View inner;
    private float y;
    //    private int yDown, yMove;
//    private boolean isIntercept;
    private Rect normal = new Rect();
    private OnSlideListenerInterface onSlideListener;

    public void setOnSlideListener(OnSlideListenerInterface onSlideListener){
        this.onSlideListener = onSlideListener;
    }

    public ServiceRightScrollView(Context context) {
        super(context);
    }

    public ServiceRightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        super.onFinishInflate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
//                    Log.i("mlguitar", y+"");
                    animation();
                    if(onSlideListener != null){
                        onSlideListener.OnSlideListener(y);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                int deltaY = (int) (preY - nowY) / size;
                // scrollBy(0, deltaY);
                y = nowY;
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());
                        return;
                    }
                    int yy = inner.getTop() - deltaY;

                    inner.layout(inner.getLeft(), yy, inner.getRight(),
                            inner.getBottom() - deltaY);
                }
                break;
            default:
                break;
        }
    }


    public void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return scrollY == 0 || scrollY == offset;
    }

    public interface OnSlideListenerInterface{
        void OnSlideListener(float coordinate);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//
//        int y = (int) event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                yDown = y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                yMove = y;
//                //上滑
//                if (yMove - yDown < 0) {
//                    isIntercept = true;
//                    //下滑
//                } else if (yMove - yDown > 0) {
//                    isIntercept = true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//
//        }
//        return isIntercept;
//    }
}