package com.soboapps.ohfark;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class Flip180 extends LinearLayout {
	
    private Matrix mForward = new Matrix();
    private Matrix mReverse = new Matrix();
	
    private float degree = 0;

	//The below two constructors appear to be required
    public Flip180(Context context) {
        super(context);
    }

    public Flip180(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    
    @Override
    public void onDraw(Canvas canvas) {
        //This saves off the matrix that the canvas applies to draws, so it can be restored later. 
        canvas.save();
        try {
            if (degree == 0) {
                super.dispatchDraw(canvas);
                return;
            }

        //now we change the matrix
        //We need to rotate around the center
        //Otherwise it rotates around the origin, and that's bad. 
        float py = this.getHeight()/2.0f;
        float px = this.getWidth()/2.0f;
        canvas.rotate(degree, getWidth() / 2, getHeight() / 2);
        //canvas.rotate(180, px, py); 
        
        mForward = canvas.getMatrix();
        mForward.invert(mReverse);
        canvas.save();
        canvas.setMatrix(mForward); // This is the matrix we need to use for
                                    // proper positioning of touch events

        //draw the text with the matrix applied. 
        super.onDraw(canvas); 

        //restore the old matrix. 
        canvas.restore();
        invalidate();
    } catch (Exception e) {

    }
    }
    

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (degree == 0) {
            return super.dispatchTouchEvent(event);
        }

        event.setLocation(getWidth() - event.getX(), getHeight() - event.getY());
        return super.dispatchTouchEvent(event);
    }

    public void rotate() {
        if (degree == 0) {
            degree = 180;
        } else {
            degree = 0;
        }
    }

}