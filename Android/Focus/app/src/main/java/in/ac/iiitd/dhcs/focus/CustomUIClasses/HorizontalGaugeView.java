package in.ac.iiitd.dhcs.focus.CustomUIClasses;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import in.ac.iiitd.dhcs.focus.R;

/**
 * Created by vedantdasswain on 12/03/15.
 */
public class HorizontalGaugeView extends View {
    private Context mContext;
    private int mBackgroundColor,mBackgroundWidth,mPrimaryColor,mPrimaryWidth,
            mRegularTextSize;
    private Paint mRectPaintBackground,mRectPaintPrimary,
            mRegularText,mPercentText;
    private float mPadding,mProgressPercent=20;
    private long mProgressValue=0;
    private RectF mDrawingRect,mBackgroundRect,mProgressRect;

    private static double M_PI_2 = Math.PI/2;

    //MeterColour
    private  int horizontalGaugeColour;

    public HorizontalGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init();
    }

    private void init() {
        Resources res = mContext.getResources();
        float density = res.getDisplayMetrics().density;

        horizontalGaugeColour=res.getColor(R.color.horizontalGauge);

        mBackgroundColor = res.getColor(R.color.horizontalGaugeBackground);
        mBackgroundWidth = (int)(20 * density); // default to 20dp
        mPrimaryColor = horizontalGaugeColour;
        mPrimaryWidth = (int)(20 * density);  // default to 20dp

        mRegularTextSize = (int)(mBackgroundWidth * 0.75); //Double the size of the width;

        mRectPaintBackground = new Paint() {
            {
                setDither(true);
                setStyle(Style.FILL);
                setStrokeCap(Cap.ROUND);
                setAntiAlias(true);
            }
        };
        mRectPaintBackground.setColor(mBackgroundColor);

        mRectPaintPrimary = new Paint() {
            {
                setDither(true);
                setStyle(Style.FILL);
                setStrokeCap(Cap.ROUND);
                setAntiAlias(true);
            }
        };
        mRectPaintPrimary.setColor(mPrimaryColor);


        Typeface tf = Typeface.create("Helvetica",Typeface.NORMAL);
        mRegularText=new Paint();
        mRegularText.setColor(getResources().getColor(R.color.accent));
        mRegularText.setTextSize(mRegularTextSize);
        mRegularText.setTextAlign(Paint.Align.CENTER);
        mRegularText.setTypeface(tf);

        float maxW = mBackgroundWidth;
        mPadding = maxW / 2;
    }

    public void setProgress(float progress) {
        mProgressPercent = progress;

        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);// bound our drawable Rect to stay fully within our canvas

        float left=0,top=0,right=mDrawingRect.right,bottom=mDrawingRect.bottom;
        mBackgroundRect=mDrawingRect;
        mBackgroundRect.set(left,top,right,bottom);
        canvas.drawRect(mDrawingRect, mRectPaintBackground);

        mProgressRect=mBackgroundRect;
        mProgressRect.set(left,top,(mProgressPercent/100)*right,bottom);
        canvas.drawRect(mDrawingRect,mRectPaintPrimary);

        String valueString=((int)mProgressPercent)+"%";
        canvas.drawText(valueString,mProgressRect.centerX(),mProgressRect.centerY(),mRegularText);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Resources res = mContext.getResources();
        float density = res.getDisplayMetrics().density;

        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // set the dimensions
        if (widthWithoutPadding > heigthWithoutPadding) {
            size = heigthWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // bound our drawable Rect to stay fully within our canvas
        mDrawingRect = new RectF(mPadding + getPaddingLeft(),
                mPadding + getPaddingTop(),
                w - mPadding - getPaddingRight(),
                h - mPadding - getPaddingBottom());
    }

}
