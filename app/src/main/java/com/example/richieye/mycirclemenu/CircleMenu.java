package com.example.richieye.mycirclemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by RichieYe on 2016/6/3.
 */

public class CircleMenu extends View {
    private int mBorderThickness=0;     //边框的宽度
    private Context mContext;
    private int defaultColor=0xFFFFFF;      //默认的颜色

    // 如果只有其中一个有值，则只画一个圆形边框
    private int mBorderOutsideColor=0;      //外边框的值
    private int mBorderInsideColor=0;       //内边框的值

    private int defaultWidth=0;         //默认组件的宽度
    private int defaultHeight=0;        //默认组件的高度

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
    protected void onDraw(Canvas canvas) {

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

        int radius=0;
        if(mBorderInsideColor!=defaultColor&&mBorderOutsideColor!=defaultColor)
        {
            radius=(defaultWidth<defaultHeight?defaultWidth:defaultHeight)/2-2*mBorderThickness;

            drawCircleBorder(canvas,radius+mBorderThickness/2,mBorderInsideColor);

            drawCircleBorder(canvas,radius+mBorderThickness+mBorderThickness/2,mBorderOutsideColor);
        }else if(mBorderInsideColor!=defaultColor&&mBorderOutsideColor==defaultColor)
        {
            radius=(defaultWidth<defaultHeight?defaultWidth:defaultHeight)/2-mBorderThickness;

            drawCircleBorder(canvas,radius+mBorderThickness/2,mBorderOutsideColor);
        }else
        {
            radius=(defaultWidth<defaultHeight?defaultWidth:defaultHeight)/2;
        }

        Paint p=new Paint();
        p.setColor(Color.BLUE);

        RectF oval2 = new RectF(0, 0, defaultWidth, defaultWidth);// 设置个新的长方形，扫描测量
        canvas.drawArc(oval2, 0, 90, true, p);
        p.setColor(Color.RED);
        canvas.drawArc(oval2, 90, 90, true, p);
        p.setColor(Color.CYAN);
        canvas.drawArc(oval2,180,90,true,p);
        p.setColor(Color.GRAY);
        canvas.drawArc(oval2,270,90,true,p);
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
}
