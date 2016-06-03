package com.example.richieye.mycirclemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by RichieYe on 2016/6/3.
 */

public class CircleMenu extends View {
    /**
     * 如果移动角度达到该值，则屏蔽点击
     */
    private static final int NOCLICK_VALUE = 3;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private static final int FLINGABLE_VALUE = 300;

    private int mBorderThickness=0;     //边框的宽度
    private Context mContext;
    private int defaultColor=0xFFFFFF;      //默认的颜色

    // 如果只有其中一个有值，则只画一个圆形边框
    private int mBorderOutsideColor=0;      //外边框的值
    private int mBorderInsideColor=0;       //内边框的值

    private int defaultWidth=0;         //默认组件的宽度
    private int defaultHeight=0;        //默认组件的高度

    private int mRadius;        //圆形半径

    private float mLastX;
    private float mLastY;

    /**
     * 布局时的开始角度
     */
    private int mStartAngle = 225;

    /**
     * 检测按下到抬起时旋转的角度
     */
    private float mTmpAngle;
    /**
     * 检测按下到抬起时使用的时间
     */
    private long mDownTime;

    /**
     * 判断是否正在自动滚动
     */
    private boolean isFling;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private int mFlingableValue = FLINGABLE_VALUE;


    public CircleMenu(Context context) {
        super(context);
        mContext=context;
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        setCustomAttributesets(attrs);      //设置自定义属性
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        setCustomAttributesets(attrs);      //设置自定义属性
    }

    private void setCustomAttributesets(AttributeSet attrs)         //设置自定义组件的属性方法
    {
        TypedArray tArray=mContext.obtainStyledAttributes(attrs,R.styleable.circlemenu);
        mBorderThickness= tArray.getDimensionPixelSize(R.styleable.circlemenu_border_thickness,0);
        mBorderOutsideColor=tArray.getColor(R.styleable.circlemenu_border_outside_color,defaultColor);
        mBorderInsideColor= tArray.getColor(R.styleable.circlemenu_border_inside_color,defaultColor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("CircleMenu",mStartAngle+"");

        if(getWidth()==0||getHeight()==0)
        {
            return;
        }

        this.measure(0,0);

        if(defaultWidth==0)
        {
            defaultWidth=getWidth();
        }

        if(defaultHeight==0)
        {
            defaultHeight=getHeight();
        }

        //int radius=0;
        mRadius=0;
        if(mBorderInsideColor!=defaultColor&&mBorderOutsideColor!=defaultColor)
        {
            mRadius=(defaultWidth<defaultHeight?defaultWidth:defaultHeight)/2-2*mBorderThickness;

            drawCircleBorder(canvas,mRadius+mBorderThickness/2,mBorderInsideColor);

            drawCircleBorder(canvas,mRadius+mBorderThickness+mBorderThickness/2,mBorderOutsideColor);
        }else if(mBorderInsideColor!=defaultColor&&mBorderOutsideColor==defaultColor)
        {
            mRadius=(defaultWidth<defaultHeight?defaultWidth:defaultHeight)/2-mBorderThickness;

            drawCircleBorder(canvas,mRadius+mBorderThickness/2,mBorderOutsideColor);
        }else
        {
            mRadius=(defaultWidth<defaultHeight?defaultWidth:defaultHeight)/2;
        }

        Paint p=new Paint();
        p.setColor(Color.GRAY);

        RectF oval2 = new RectF(0, 0, defaultWidth, defaultWidth);// 设置个新的长方形，扫描测量

        canvas.drawArc(oval2, mStartAngle, 90, true, p);
        //p.setColor(Color.RED);
        mStartAngle+=90;
        canvas.drawArc(oval2, mStartAngle, 90, true, p);
        //p.setColor(Color.CYAN);
        mStartAngle+=90;
        canvas.drawArc(oval2,mStartAngle,90,true,p);
        //p.setColor(Color.GRAY);
        mStartAngle+=90;
        canvas.drawArc(oval2,mStartAngle,90,true,p);
        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        //p.setColor(Color.RED);
        //canvas.drawCircle(defaultWidth/2,defaultWidth/2,radius,p);
    }

    private  void drawCircleBorder(Canvas canvas,int radius,int color)
    {
        Paint paint=new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);

        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeWidth(mBorderThickness);

        canvas.drawCircle(defaultWidth/2,defaultHeight/2,radius,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
        Log.e("CircleMenu",event.getAction()+"     "+event.getX()+"               "+event.getY());
        requestLayout();
        return super.onTouchEvent(event);
        */
        return true;
    }

    /**
     * 根据触摸的位置，计算角度
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch, float yTouch)
    {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     *
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y)
    {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0)
        {
            return tmpY >= 0 ? 4 : 1;
        } else
        {
            return tmpY >= 0 ? 3 : 2;
        }
    }

    private AutoFlingRunnable mFlingRunnable;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {

        float x = event.getX();
        float y = event.getY();

        Log.e("TAG", "x = " + x + " , y = " + y);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngle = 0;

                // 如果当前已经在快速滚动
                if (isFling)
                {
                    // 移除快速滚动的回调
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }

                break;
            case MotionEvent.ACTION_MOVE:

                /**
                 * 获得开始的角度
                 */
                float start = getAngle(mLastX, mLastY);
                /**
                 * 获得当前的角度
                 */
                float end = getAngle(x, y);

                // Log.e("TAG", "start = " + start + " , end =" + end);
                // 如果是一、四象限，则直接end-start，角度值都是正值
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4)
                {
                    mStartAngle += end - start;
                    mTmpAngle += end - start;
                } else
                // 二、三象限，色角度值是付值
                {
                    mStartAngle += start - end;
                    mTmpAngle += start - end;
                }
                // 重新布局
                //requestLayout();
                invalidate();
                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:

                // 计算，每秒移动的角度
                float anglePerSecond = mTmpAngle * 1000
                        / (System.currentTimeMillis() - mDownTime);

                // Log.e("TAG", anglePrMillionSecond + " , mTmpAngel = " +
                // mTmpAngle);

                // 如果达到该值认为是快速移动
                if (Math.abs(anglePerSecond) > mFlingableValue && !isFling)
                {
                    // post一个任务，去自动滚动
                    post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));

                    return true;
                }

                // 如果当前旋转角度超过NOCLICK_VALUE屏蔽点击
                if (Math.abs(mTmpAngle) > NOCLICK_VALUE)
                {
                    return true;
                }

                break;
        }

        return super.dispatchTouchEvent(event);
    }


    /**
     * 自动滚动的任务
     *
     * @author zhy
     *
     */
    private class AutoFlingRunnable implements Runnable
    {

        private float angelPerSecond;

        public AutoFlingRunnable(float velocity)
        {
            this.angelPerSecond = velocity;
        }

        public void run()
        {
            // 如果小于20,则停止
            if ((int) Math.abs(angelPerSecond) < 20)
            {
                isFling = false;
                return;
            }
            isFling = true;
            // 不断改变mStartAngle，让其滚动，/30为了避免滚动太快
            mStartAngle += (angelPerSecond / 30);
            // 逐渐减小这个值
            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);
            // 重新布局
            requestLayout();
        }
    }

}
