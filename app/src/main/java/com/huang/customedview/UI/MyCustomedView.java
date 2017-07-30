package com.huang.customedview.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/5/1.
 */

public class MyCustomedView extends android.support.v7.widget.AppCompatTextView {

    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 0;
    private Paint mPaint;
    private Paint mPaint2;

    private int mViewWidth = 0;
    private int mViewHeight = 0;

    private int mTranslate = 0;

    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;

    public MyCustomedView(Context context) {
        super(context);

        initPaint();
    }

    public MyCustomedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //height = attrs.getAttributeIntValue()
        initPaint();
    }

    public MyCustomedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }



    private void initPaint(){
      mPaint = new Paint();
      mPaint.setColor(getResources().getColor(android.R.color.holo_green_light));
      mPaint.setStyle(Paint.Style.FILL);

    }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (mViewWidth == 0) {
      mViewWidth = getMeasuredWidth();
      if (mViewWidth != 0) {
        mPaint = getPaint();
        int[] linearArr = new int[]{Color.BLUE,0xffffffff,Color.BLUE};
        mLinearGradient = new LinearGradient(0,0,mViewWidth,0,linearArr,null, Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
        mGradientMatrix = new Matrix();
      }

    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = customedMeasureWidth(widthMeasureSpec);
    int height = customedMeasureHeight(heightMeasureSpec);
    //super.onMeasure(width, heightMeasureSpec);
    setMeasuredDimension(width,height);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mGradientMatrix != null) {
      mTranslate += mViewWidth/5;
      if (mTranslate > 2*mViewWidth) {
        mTranslate = -mViewWidth;
      }
      mGradientMatrix.setTranslate(mTranslate, 0);
      mLinearGradient.setLocalMatrix(mGradientMatrix);
      postInvalidateDelayed(100);
    }
  }

  private int customedMeasureWidth(int widthMeasureSpce) {
    int result = 0;
    int specMode = MeasureSpec.getMode(widthMeasureSpce);
    int specSize = MeasureSpec.getSize(widthMeasureSpce);

    switch (specMode) {
      case (MeasureSpec.EXACTLY): {
        result = specSize;
        break;
      }
      case (MeasureSpec.UNSPECIFIED): {
//        result = specSize;
//        break;
      }
      case (MeasureSpec.AT_MOST): {
        result = Math.min(DEFAULT_WIDTH, specSize);
        break;
      }
      default:
        break;
    }

    return result;
  }

  private int customedMeasureHeight(int heightMeasureSpce) {
    int result = 0;
    int specMode = MeasureSpec.getMode(heightMeasureSpce);
    int specSize = MeasureSpec.getSize(heightMeasureSpce);

    switch (specMode) {
      case (MeasureSpec.EXACTLY): {
        result = specSize;
        break;
      }
      case (MeasureSpec.UNSPECIFIED): {
//        result = specSize;
//        break;
      }
      case (MeasureSpec.AT_MOST): {
        result = Math.min(DEFAULT_WIDTH, specSize);
        break;
      }
      default:
        break;
    }

    return result;
  }

  public Paint getCustomedPaint() {
    if (mPaint == null) {
      mPaint = new Paint();
      mPaint.setColor(getResources().getColor(android.R.color.holo_green_light));
      mPaint.setStyle(Paint.Style.FILL);
    }
    return mPaint;
  }
}
