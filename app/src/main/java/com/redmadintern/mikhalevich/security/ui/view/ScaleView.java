package com.redmadintern.mikhalevich.security.ui.view;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.redmadintern.mikhalevich.security.R;

/**
 * Created by Alexander on 18.12.2014.
 */
public class ScaleView extends View {
    private static int ANIM_DURATION = 500;
    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();

    private Bitmap src;
    private Rect rect;
    private Rect rectCpy = new Rect();
    private Rect srcRect;
    private Rect srcRectCpy = new Rect();
    private Rect squareRect;
    private int toolbarHeight;
    Rect newRect = new Rect();

    private ValueAnimator valueAnimator = new ValueAnimator();
    private AnimationListener animatorUpdateListener;
    private Handler handler = new Handler();

    private int startX;
    private int startY;
    private int startWidth;
    private int startHeight;

    private int targetX;
    private int targetY;
    private int targetWidth;
    private int targetHeight;

    private int distanceX;
    private int distanceY;

    private float progress;
    private boolean squarify;
    private float aspectRatio;
    private float deltaRatio;

    private boolean startShowAnimation;
    private boolean startHideAnimation;
    private boolean isLastAnimShow;

    private Paint backgroundPaint = new Paint();
    private Paint whitePaint = new Paint();

    public ScaleView(Context context) {
        super(context);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);

        /*final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { R.attr.actionBarSize });
        toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (startShowAnimation)
            runShowAnimation();

        if (startHideAnimation)
            runHideAnimation();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (src != null) {
            int centerX;
            int centerY;

            if (targetX > startX)
                centerX = startX + (int)(distanceX*progress);
            else
                centerX = startX - (int)(distanceX*progress);

            if (targetY > startY)
                centerY = startY + (int)(distanceY*progress);
            else
                centerY = startY - (int)(distanceY*progress);

            int halfWidth = (int)((targetWidth * progress) + (startWidth*(1-progress)))/2;
            int halfHeight = (int)((targetHeight * progress) + (startHeight*(1-progress)))/2;

            newRect.left = centerX - halfWidth;
            newRect.top = centerY - halfHeight;
            newRect.right = centerX + halfWidth;
            newRect.bottom = centerY + halfHeight;

            float squarifyProgress;
            if (startShowAnimation) {
                squarifyProgress = (1-progress);
            } else if (startHideAnimation) {
                squarifyProgress = progress;
            } else if (isLastAnimShow) {
                squarifyProgress = 0;
            } else {
                squarifyProgress = 1;
            }

            srcRectCpy.set(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
            
            if (squarify) {
                int dimen;
                if (aspectRatio >= 1) {
                    dimen = newRect.height();
                    newRect.left += (int)(dimen*deltaRatio*squarifyProgress);
                    newRect.right -= (int)(dimen*deltaRatio*squarifyProgress);

                    dimen = srcRect.height();
                    srcRectCpy.left += (int)(dimen*deltaRatio*squarifyProgress);
                    srcRectCpy.right -= (int)(dimen*deltaRatio*squarifyProgress);
                } else {
                    dimen = newRect.width();
                    newRect.top += (int)(dimen*deltaRatio*squarifyProgress);
                    newRect.bottom -= (int)(dimen*deltaRatio*squarifyProgress);
                    
                    dimen = srcRect.width();
                    srcRectCpy.top += (int)(dimen*deltaRatio*squarifyProgress);
                    srcRectCpy.bottom -= (int)(dimen*deltaRatio*squarifyProgress);
                }
            }

            int alpha;
            if (startShowAnimation) {
                alpha = (int) (255 * progress);
            } else if (startHideAnimation) {
                alpha = (int) (255 * (1 - progress));
            } else if (isLastAnimShow) {
                alpha = 255;
            } else {
                alpha = 0;
            }
            backgroundPaint.setAlpha(alpha);


            if (squarify) {
                if (squareRect.top < toolbarHeight)
                    squareRect.top = toolbarHeight;
                canvas.drawRect(squareRect, whitePaint);
            } else {
                rectCpy.set(rect.left, rect.top, rect.right, rect.bottom);
                if (rectCpy.top < toolbarHeight)
                    rectCpy.top = toolbarHeight;
                canvas.drawRect(rectCpy, whitePaint);
            }

            canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), backgroundPaint);

            canvas.drawBitmap(src, srcRectCpy, newRect, null);
        } else {
            backgroundPaint.setAlpha(255);
            canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), backgroundPaint);
        }

    }

    public void setSrc(Bitmap src) {
        this.src = src;
        if (src != null)
            srcRect = new Rect(0, 0, src.getWidth(), src.getHeight());
        else
            srcRect = new Rect(0, 0, 0, 0);

        aspectRatio = (float)srcRect.width() / (float)srcRect.height();
        if (aspectRatio >= 1){
            deltaRatio = (float)(srcRect.width() - srcRect.height())/(float)(2*srcRect.height());
        } else {
            deltaRatio = (float)(srcRect.height() - srcRect.width())/(float)(2*srcRect.width());
        }
    }

    public void setRect(Rect rect) {
        this.rect = rect;

        int width, height, delta, left, top, right, bottom;
        int viewDimen = (rect.width() < rect.height()) ? rect.width() : rect.height();

        aspectRatio = (float)rect.width() / (float)rect.height();
        if (aspectRatio >= 1){
            width = rect.width();
            delta = (width - viewDimen)/2;
            left = rect.left + delta;
            top = rect.top;
            right = left + viewDimen;
            bottom = top + viewDimen;
        } else {
            height = rect.height();
            delta = (height - viewDimen)/2;
            left = rect.left;
            top = rect.top + delta;
            right = left + viewDimen;
            bottom = top + viewDimen;
        }

        squareRect = new Rect(left, top, right, bottom);
    }

    private int[] getDimens(int thWidth, int thHeight, int width, int height) {
        float scaleFactorTh = (float)thWidth / (float)thHeight;
        float scaleFactor = (float)width / (float)height;
        int dimens[] = new int[2];
        if (scaleFactorTh > scaleFactor) {
            dimens[0] = width;
            dimens[1] = (int)(width/scaleFactorTh);
        } else {
            dimens[1] = height;
            dimens[0] = (int)(height*scaleFactorTh);
        }
        return dimens;
    }

    private void runShowAnimation() {
        startWidth = rect.right - rect.left;
        startHeight = rect.bottom - rect.top;
        startX = rect.left + startWidth/2;
        startY = rect.top + startHeight/2;

        int[] targetDimens = getDimens(startWidth, startHeight, getMeasuredWidth(), getMeasuredHeight());
        targetX = getMeasuredWidth()/2;
        targetY = getMeasuredHeight()/2;
        targetWidth = targetDimens[0];
        targetHeight = targetDimens[1];

        distanceX = Math.abs(targetX - startX);
        distanceY = Math.abs(targetY - startY);

        valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setDuration(ANIM_DURATION);
        valueAnimator.setInterpolator(sDecelerator);
        valueAnimator.addUpdateListener(updateListener);
        valueAnimator.addListener(animatorListener);
        valueAnimator.start();
    }

    private void runHideAnimation() {

        targetWidth = rect.right - rect.left;
        targetHeight = rect.bottom - rect.top;
        targetX = rect.left + targetWidth/2;
        targetY = rect.top + targetHeight/2;

        int[] startDimens = getDimens(targetWidth, targetHeight, getMeasuredWidth(), getMeasuredHeight());
        startX = getMeasuredWidth()/2;
        startY = getMeasuredHeight()/2;
        startWidth = startDimens[0];
        startHeight = startDimens[1];

        distanceX = Math.abs(targetX - startX);
        distanceY = Math.abs(targetY - startY);

        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIM_DURATION);
        valueAnimator.setInterpolator(sDecelerator);
        valueAnimator.addUpdateListener(updateListener);
        valueAnimator.addListener(animatorListener);
        valueAnimator.start();
    }


    public void startShowAnimation() {
        startShowAnimation = true;
        if (getMeasuredWidth() != 0)
            runShowAnimation();
    }

    public void startHideAnimation() {
        startHideAnimation = true;
        if (getMeasuredWidth() != 0)
            runHideAnimation();
    }

    public void setAnimatorUpdateListener(AnimationListener animatorUpdateListener) {
        this.animatorUpdateListener = animatorUpdateListener;
    }

    public void setSquarify(boolean squarify) {
        this.squarify = squarify;
    }

    public void setWhiteColor(int color) {
        whitePaint.setColor(color);
    }

    ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float value = (Float)valueAnimator.getAnimatedValue();
            progress = value.floatValue();
            invalidate();
        }
    };

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {

            if (startHideAnimation) {
                startHideAnimation = false;
                isLastAnimShow = false;
                if (animatorUpdateListener != null)
                    animatorUpdateListener.onHideAnimationEnd();
            } else if (startShowAnimation) {
                startShowAnimation = false;
                isLastAnimShow = true;
                if (animatorUpdateListener != null)
                    animatorUpdateListener.onShowAnimationEnd();
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

    public interface AnimationListener {
        public void onShowAnimationEnd();
        public void onHideAnimationEnd();
    }
}
