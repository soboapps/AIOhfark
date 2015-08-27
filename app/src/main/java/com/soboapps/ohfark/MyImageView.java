package com.soboapps.ohfark;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {

        private AnimationEndListener mAnimListener = null;

        public MyImageView(Context context) {
                super(context);
        }

        public MyImageView(Context context, AttributeSet attrs) {
                super(context, attrs);
        }

        public MyImageView(Context context, AttributeSet attrs, int defStyle) {
                super(context, attrs, defStyle);
        }

        @Override
        protected void onAnimationEnd() {
                super.onAnimationEnd();
                if(mAnimListener != null)mAnimListener.onAnimationEnd(MyImageView.this);
        }

        public void setAnimationEndListerner(AnimationEndListener l) {
                mAnimListener = l;
        }

}