package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.alcidae.smarthome.R;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/3 17:00 1.0
 * @time 2018/4/3 17:00
 * @project ir_demo com.alcidae.smarthome.ir.widget
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/3 17:00
 */

public class IrPadView extends IrVibrateImageView {
    private static final int IDLE = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int UP = 3;
    private static final int DOWN = 4;
    private static final int CENTER = 5;


    private GestureDetector mGestureDetector;
    private int mDefaultRes = R.drawable.ir_default;
    private int mLeftRes = R.drawable.ir_click_left;
    private int mRightRes = R.drawable.ir_click_right;
    private int mUpRes = R.drawable.ir_click_up;
    private int mDownRes = R.drawable.ir_click_down;
    private int mCenterRes = R.drawable.ir_click_ok;


    private int mClickDirection;

    private Rect mCenterBounds;
    private Rect mLeftBounds;
    private Rect mRightBounds;
    private Rect mUpBounds;
    private Rect mDownBounds;

    private OnPadClickListener onPadClickListener;

    public IrPadView(Context context) {
        super(context);
        init();
    }


    public IrPadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IrPadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setEveryBounds();
    }

    private void setEveryBounds() {
        int oneThirdWidth = getWidth() / 3;
        int oneThirdHeight = getHeight() / 3;
        mCenterBounds = new Rect(oneThirdWidth, oneThirdHeight, oneThirdWidth * 2, oneThirdHeight * 2);
        mLeftBounds = new Rect(0, oneThirdHeight, oneThirdWidth, oneThirdHeight * 2);
        mRightBounds = new Rect(oneThirdWidth * 2, oneThirdHeight, getWidth(), oneThirdHeight * 2);
        mUpBounds = new Rect(oneThirdWidth, 0, oneThirdWidth * 2, oneThirdHeight);
        mDownBounds = new Rect(oneThirdWidth, oneThirdHeight * 2, oneThirdWidth * 2, getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mClickDirection = jugdeDirection(event);
            showImage();
        } else if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            notifyPadClickListener();
            mClickDirection = IDLE;
            showImage();
            super.performClick();
        }
        return true;
    }


    private void init() {
        setImageResource(mDefaultRes);
    }

    private void notifyPadClickListener() {
        if (mClickDirection != IDLE && onPadClickListener != null) {
            switch (mClickDirection) {
                case CENTER:
                    onPadClickListener.clickCenter();
                    break;
                case LEFT:
                    onPadClickListener.clickLeft();
                    break;
                case RIGHT:
                    onPadClickListener.clickRight();
                    break;
                case UP:
                    onPadClickListener.clickUp();
                    break;
                case DOWN:
                    onPadClickListener.clickDown();
                    break;
                default:
                    break;
            }
        }
    }

    private void showImage() {
        switch (mClickDirection) {
            case CENTER:
                setImageResource(mCenterRes);
                break;
            case LEFT:
                setImageResource(mLeftRes);
                break;
            case RIGHT:
                setImageResource(mRightRes);
                break;
            case UP:
                setImageResource(mUpRes);
                break;
            case DOWN:
                setImageResource(mDownRes);
                break;
            default:
                setImageResource(mDefaultRes);
                break;
        }
    }

    private int jugdeDirection(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        if (mCenterBounds.contains(x, y)) {
            return CENTER;
        }
        if (mLeftBounds.contains(x, y)) {
            return LEFT;
        }
        if (mUpBounds.contains(x, y)) {
            return UP;
        }
        if (mRightBounds.contains(x, y)) {
            return RIGHT;
        }
        if (mDownBounds.contains(x, y)) {
            return DOWN;
        }
        return IDLE;
    }

    public OnPadClickListener getOnPadClickListener() {
        return onPadClickListener;
    }

    public void setOnPadClickListener(OnPadClickListener onPadClickListener) {
        this.onPadClickListener = onPadClickListener;
    }

    public interface OnPadClickListener {
        void clickUp();

        void clickDown();

        void clickLeft();

        void clickRight();

        void clickCenter();
    }
}
