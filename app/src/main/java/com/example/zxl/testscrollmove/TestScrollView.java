package com.example.zxl.testscrollmove;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/2/8 11:02
 */

public class TestScrollView extends ScrollView {
    private TextView headView;
    private Context mContext;
    private float mLastY = -1;
    private LinearLayout layout_content;
    private boolean isAdded = false;
    private Scroller mScroller;
    private float deltay = 0;
    private int headerViewDefalutHeight = 48;

    public TestScrollView(Context context) {
        this(context, null, 0);
    }

    public TestScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public void init() {
        mScroller = new Scroller(mContext, new LinearInterpolator());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isAdded) {
            isAdded = true;
            layout_content = (LinearLayout) getChildAt(0);
            if (layout_content != null) {
                headView = new TextView(mContext);
                headView.setText("头布局");
                headView.setGravity(Gravity.CENTER);
                layout_content.addView(headView, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, headerViewDefalutHeight));
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();//相对屏幕的Y坐标
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指落下
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动
                deltay = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                updateHeaderHeight(deltay);
                break;
            case MotionEvent.ACTION_UP:
                resetHeaderHeight();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void updateHeaderHeight(float deltay) {
        if (headView == null) {
            return;
        }
        int height = headView.getLayoutParams().height;
        if (height + deltay < 0) {
            headView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        } else {
            headView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height + deltay)));
        }
    }

    public void resetHeaderHeight() {
        if (headView == null) {
            return;
        }
        int height = headView.getLayoutParams().height;
        mScroller.startScroll(0, height, 0, headerViewDefalutHeight-height, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int curry = mScroller.getCurrY();
            headView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, curry));
            postInvalidate();
        }
        super.computeScroll();
    }
}
