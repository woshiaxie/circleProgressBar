package com.huang.customedview.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.huang.customedview.R;

public class CircleProgressView extends View {
    public static final String TAG = "MyLog";

    public static final int DEFAULT_ARC_COLOR = 0x8bc34a;
    public static final float DEFAULT_ARC_WIDTH = 20;
    public static final float DEFAULT_ARC_START_ANGLE = 0;

    public static final float DEFAULT_CIRCLE_RADIUS = -1;
    public static final int DEFAULT_CIRCLE_BACKGROUND_COLOR = 0x607d8b;

    private static final int DEFAULT_WIDTH = 0;
    private static final int DEFAULT_HEIGHT = 0;

    private int mMeasureHeigth;
    private int mMeasureWidth;

    //中心圆的画笔
    private Paint mCirclePaint;
    //中心圆的背景色
    private int mCircleBackgroundColor = DEFAULT_CIRCLE_BACKGROUND_COLOR;
    //圆心位置X
    private float mCircleX;
    //圆心位置X
    private float mCircleY;
    //园半径
    private float mRadius = DEFAULT_CIRCLE_RADIUS;

    //外弧线的画笔
    private Paint mArcPaint;
    //外弧线的外接矩形
    private RectF mArcRectF;
    //外弧线的开始角度
    private float mStartAngle = DEFAULT_ARC_START_ANGLE;
    //外弧线扫过的角度
    private double mSweepAngle = 0;
    //外弧线颜色
    private int mArcColor = DEFAULT_ARC_COLOR;
    //外弧线宽度
    private float mArcWidth = DEFAULT_ARC_WIDTH;

    //中心圆的文字画笔
    private Paint mTextPaint;
    //中心圆的文字内容
    private String mCenterText;
    //中心圆的文字大小
    private float mCenterTextSize;


    public CircleProgressView(Context context) {
        this(context, null);
    }


    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        bindStyleAttrs(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMeasureHeigth = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(mMeasureWidth, mMeasureHeigth);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制圆
        canvas.drawCircle(mCircleX, mCircleY, mRadius, mCirclePaint);
        // 绘制弧线
        canvas.drawArc(mArcRectF, mStartAngle, (float)mSweepAngle, false, mArcPaint);
        // 绘制文字
        canvas.drawText(mCenterText, 0, mCenterText.length(),mCircleX, mCircleY + (mCenterTextSize / 4), mTextPaint);
    }

    private void initView() {
        Log.d(TAG, "----------------------initView----------- ");
        float baseLength = 0;
        if (mMeasureHeigth >= mMeasureWidth) {
            baseLength = mMeasureWidth;
        } else {
            baseLength = mMeasureHeigth;
        }
        initDrawCircleData(baseLength);
        initDrawArcData(baseLength);
        initCenterTextPaint();
    }

    //---------------------中心圆-----------------------------
    private void initDrawCircleData(float baseLength) {
        float strokeWidth = this.mArcWidth;
        mCircleX = baseLength / 2;
        mCircleY = baseLength / 2;
        if (mRadius == -1) {
            mRadius = (float) (baseLength * 0.5 - mArcWidth);
        }

        initCirclePaint(strokeWidth);
    }

    private void initCirclePaint(float strokeWidth) {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleBackgroundColor);
        mCirclePaint.setAlpha(0xff);
        mCirclePaint.setStrokeWidth(strokeWidth);//线宽
        mCirclePaint.setStyle(Paint.Style.FILL);
    }

    //---------------------外弧线-----------------------------
    private void initDrawArcData(float baseLength) {
        initArcPaint(baseLength);
        float arcPadding = mArcWidth/2;
        mArcRectF = new RectF(0 + arcPadding, 0 + arcPadding, mMeasureWidth - arcPadding, mMeasureHeigth - arcPadding);
    }

    private void initArcPaint(float baseLength) {
        float strokeWidth = this.mArcWidth;
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(mArcColor);
        mArcPaint.setAlpha(0xff);
        mArcPaint.setStrokeWidth(strokeWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
    }

    private void initCenterTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mCenterTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * TypedValue.COMPLEX_UNIT_PX : Pixels
     * TypedValue.COMPLEX_UNIT_SP : Scaled Pixels
     * TypedValue.COMPLEX_UNIT_DIP : Device Independent Pixels
     *
     * @param unit 单位，可以是上面列举的几个之一
     * @param size 大小
     * @return
     */
    private void setCenterTextSize(int unit, int size) {
        mCenterTextSize = TypedValue.applyDimension(unit, size,getResources().getDisplayMetrics());
        this.invalidate();
    }

    public void setCenterText(CharSequence str) {
        if (!TextUtils.isEmpty(str)) {
            mCenterText = str.toString();
            this.invalidate();
        }
    }

    public void forceInvalidate() {
        this.invalidate();
    }

    public void setSweepAngle(float sweepAngle) {
        this.setSweepAngle((double) sweepAngle);
    }

    public void setSweepAngle(double sweepAngle) {
        Log.d(TAG, "setSweepAngle: " + sweepAngle);
        if (sweepAngle < 0) {
            Log.d(TAG, "setSweepAngle is less than 0 ");
            return;
        }
        mSweepAngle = sweepAngle % 360;
        this.invalidate();
    }

    /**
     * 设置外弧线的宽度,默认单位dp
     * @param width
     */
    public void setArcWidth(int width) {
        mArcWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width,getResources().getDisplayMetrics());
    }
    public void setArcWidth(int unit, int width) {
        mArcWidth = TypedValue.applyDimension(unit, width,getResources().getDisplayMetrics());
    }

    public float getArcWidth() {
        return this.mArcWidth;
    }


    public void setArcColor(@ColorInt int color) {
        this.mArcColor = color;
        this.invalidate();
    }
    public int getArcColor() {
        return this.mArcColor;
    }

    private void bindStyleAttrs(Context context, AttributeSet attrs) {
//                    Log.d(TAG, "mCircleBackgroundColor: " + mCircleBackgroundColor);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.circleProgressView, 0, 0);
        int n = array.getIndexCount();
        for (int i = 0; i <= n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                //注意获取属性的方式为 styleable的名称_属性名称
                case R.styleable.circleProgressView_centerCircleColor:
                    mCircleBackgroundColor = array.getColor(attr, DEFAULT_CIRCLE_BACKGROUND_COLOR);
                    break;
                case R.styleable.circleProgressView_centerCircleX:
                    mCircleX = array.getFloat(attr, mMeasureWidth / 2);
                    break;
                case R.styleable.circleProgressView_centerCircleY:
                    mCircleY = array.getFloat(attr, mMeasureHeigth / 2);
                    break;
                case R.styleable.circleProgressView_centerCircleRadius:
                    mRadius = array.getDimension(attr, DEFAULT_CIRCLE_RADIUS);
                    break;
                case R.styleable.circleProgressView_arcSweepAngle:
                    mSweepAngle = array.getFloat(attr, 0f);
                    break;
                case R.styleable.circleProgressView_arcStartAngle:
                    mStartAngle = array.getFloat(attr, -1f);
                    break;
                case R.styleable.circleProgressView_arcColor:
                    mArcColor = array.getInteger(attr, DEFAULT_ARC_COLOR);
                    break;
                case R.styleable.circleProgressView_arcWidth:
                    mArcWidth = array.getDimension(attr, 10);
                    break;
                case R.styleable.circleProgressView_centerText:
                    mCenterText = array.getString(attr);
                    break;
                case R.styleable.circleProgressView_centerTextSize:
                    float defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
                    mCenterTextSize = array.getDimension(R.styleable.circleProgressView_centerTextSize, defaultSize);
                    break;
            }
        }
    }
}
