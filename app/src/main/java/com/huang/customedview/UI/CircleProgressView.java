package com.huang.customedview.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.huang.customedview.R;

public class CircleProgressView extends View {
  public static final String TAG = "MyLog";

  private static final int DEFAULT_WIDTH = 0;
  private static final int DEFAULT_HEIGHT = 0;
  private int mMeasureHeigth;
  private int mMeasureWidth;

  //中心圆的画笔
  private Paint mCirclePaint;
  //中心圆的背景色
  private int mCircleBackgroundColor = 0xff4466;
  //圆心位置X
  private float mCircleX;
  //圆心位置X
  private float mCircleY;
  //园半径
  private float mRadius = 10;

  //外弧线的画笔
  private Paint mArcPaint;
  //外弧线的外接矩形
  private RectF mArcRectF;
  //
  private float mStartAngle = 0;
  //外弧线扫过的角度
  private float mSweepAngle = 0;
  //外弧线扫过的角度百分比（1~100）
  private float mSweepValue = 66;
  //外弧线颜色
  private int mArcColor = 0xcccccc;

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
    canvas.drawArc(mArcRectF, mStartAngle, mSweepAngle, false, mArcPaint);
    // 绘制文字
    canvas.drawText(mCenterText, 0, mCenterText.length(),
      mCircleX, mCircleY + (mCenterTextSize / 4), mTextPaint);
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
    float strokeWidth = baseLength * 0.1f;
    mCircleX = baseLength / 2;
    mCircleY = baseLength / 2;
    mRadius = (float) (baseLength * 0.5 / 2);
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
    initArcPaint(baseLength * 0.1f);
    mArcRectF = new RectF(
      (float) (baseLength * 0.1),
      (float) (baseLength * 0.1),
      (float) (baseLength * 0.9),
      (float) (baseLength * 0.9));

    //mSweepAngle = (mSweepValue / 100f) * 360f;
  }
  private void initArcPaint(float strokeWidth) {
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
   * @param unit 单位，可以是上面列举的几个之一
   * @param size 大小
   * @return
   */
  private void setCenterTextSize(int unit, int size) {
    switch (unit) {
      case (TypedValue.COMPLEX_UNIT_PX): {
        mCenterTextSize = size;
        break;
      }
      case (TypedValue.COMPLEX_UNIT_DIP): {
        mCenterTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
        break;
      }
      default: {
        mCenterTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getResources().getDisplayMetrics());
      }
    }
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
    mSweepAngle = sweepAngle;
    this.invalidate();
  }


  private void bindStyleAttrs(Context context, AttributeSet attrs) {
    Log.d(TAG, "----------------------bindStyleAttrs----------- ");
    TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.circleProgressView, 0, 0);
    int n = array.getIndexCount();
    for (int i = 0; i <= n; i++) {
      int attr = array.getIndex(i);
      switch (attr) {
        //注意获取属性的方式为 styleable的名称_属性名称
        case R.styleable.circleProgressView_centerCircleColor:
          mCircleBackgroundColor = array.getColor(attr, 0xfff000);
          Log.d(TAG, "mCircleBackgroundColor == " + mCircleBackgroundColor);
          break;
        case R.styleable.circleProgressView_centerCircleX:
          mCircleX = array.getFloat(attr, mMeasureWidth/2);
          Log.d(TAG, "mCircleX == " + mCircleX);
          break;
        case R.styleable.circleProgressView_centerCircleY:
          mCircleY = array.getFloat(attr, mMeasureHeigth/2);
          Log.d(TAG, "mCircleY == " + mCircleY);
          break;
        case R.styleable.circleProgressView_arcSweepAngle:
          mSweepAngle = array.getFloat(attr, 0f);
          Log.d(TAG, "mSweepAngle == " + mSweepAngle);
          break;
        case R.styleable.circleProgressView_arcStartAngle:
          mStartAngle = array.getFloat(attr, 0f);
          Log.d(TAG, "mStartAngle == " + mStartAngle);
          break;
        case R.styleable.circleProgressView_arcSweepAnglePercent:
          mSweepValue = array.getInteger(attr, 50);
          Log.d(TAG, "mSweepValue == " + mSweepValue);
          break;
        case R.styleable.circleProgressView_arcColor:
          mArcColor = array.getInteger(attr, 0xff4466);
          Log.d(TAG, "mArcColor == " + mArcColor);
          break;
        case R.styleable.circleProgressView_centerText:
          mCenterText = array.getString(attr);
          Log.d(TAG, "mCenterText == " + mCenterText);
          break;
        case R.styleable.circleProgressView_centerTextSize:
          float defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
          mCenterTextSize = array.getDimension(R.styleable.circleProgressView_centerTextSize, defaultSize);
          Log.d(TAG, "mCenterText == " + mCenterText);
          break;
      }
    }
  }
}
