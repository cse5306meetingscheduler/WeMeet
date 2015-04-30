package com.cse5306.wemeet.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.cse5306.wemeet.R;

/**
 * Created by Sathvik on 18/04/15.
 */
/**
 * Custom view to display group id the list
 */
public class CircleText extends View {

    String mText;
    int mColor;


    public CircleText(Context context, AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleText,
                0, 0);
        try {
            mText = a.getString(R.styleable.CircleText_text);
            mColor = a.getColor(R.styleable.CircleText_backgroundColor,getResources().getColor(R.color.deeporange));
        } finally {
            a.recycle();
        }
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
        invalidate();
        requestLayout();
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float diameter = canvas.getHeight();
        float squareLength = (float)Math.sqrt((diameter*diameter)/2);

        Paint paint = new Paint();
        paint.setColor(mColor);

        Paint pw = new Paint();
        pw.setStrokeWidth(10);
        if(mText.length() == 1){
            pw.setTextSize(squareLength*2 /2);
        }else{
            pw.setTextSize(squareLength*2 /(mText.length()));
        }

        pw.setColor(getResources().getColor(R.color.white));
        pw.setTextAlign(Paint.Align.CENTER);

        float textX = (diameter-squareLength)/2;
        float textY = (diameter-squareLength)/2 + squareLength;

        RectF rectF = new RectF();
        rectF.set(textX, textX, textY, textY);
        rectF.centerX();
        rectF.centerY();

        float alteredY = rectF.centerY() + pw.getTextSize()/2-20;

        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2,canvas.getWidth()/2,paint);
        canvas.drawText(mText,0,mText.length(),rectF.centerX(),alteredY,pw);
    }
}