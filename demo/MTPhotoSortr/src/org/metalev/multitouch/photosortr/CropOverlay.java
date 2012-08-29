package org.metalev.multitouch.photosortr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * A custom View to draw a transparent box with a border, and a semi transparent color outside the border.
 */
public class CropOverlay extends View
{
    private float zoneWidth, zoneHeight;
    private int strokeWidth;
    private Paint borderPaint, exteriorPaint;
    private Rect zoneRect;
    private Rect[] exterior;

    public CropOverlay(Context context)
    {
        super(context);
        init();
    }

    public CropOverlay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public CropOverlay(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        Resources resources = getContext().getResources();
        zoneWidth = resources.getDimension(R.dimen.crop_width);
        zoneHeight = resources.getDimension(R.dimen.crop_height);
        strokeWidth = Math.round(resources.getDimension(R.dimen.crop_border_thickness));

        borderPaint = new Paint();
        borderPaint.setColor(resources.getColor(R.color.crop_border));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(strokeWidth);

        exteriorPaint = new Paint();
        exteriorPaint.setColor(resources.getColor(R.color.crop_exterior));
        exteriorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (zoneRect == null)
        {
            //center the rectangle on the screen
            zoneRect = new Rect();
            zoneRect.left = Math.round((float)this.getWidth() / 2.0f - zoneWidth / 2.0f);
            zoneRect.top = Math.round((float)this.getHeight() / 2.0f - zoneHeight / 2.0f);
            zoneRect.right = zoneRect.left + Math.round(zoneWidth);
            zoneRect.bottom = zoneRect.top + Math.round(zoneHeight);

            //Fill the outer bounds of the crop zone. Maybe there's a better way, but we'll brute force it for now.
            Rect first = new Rect();
            first.left = 0;
            first.top = 0;
            first.right = this.getWidth();
            first.bottom = zoneRect.top;

            Rect second = new Rect();
            second.left = 0;
            second.top = first.bottom;
            second.right = zoneRect.left;
            second.bottom = zoneRect.bottom;

            Rect third = new Rect();
            third.left = zoneRect.right;
            third.top = first.bottom;
            third.right = this.getWidth();
            third.bottom = zoneRect.bottom;

            Rect last = new Rect();
            last.top = zoneRect.bottom;
            last.left = 0;
            last.right = this.getWidth();
            last.bottom = this.getHeight();

            exterior = new Rect[] { first, second, third, last };
        }

        canvas.drawRect(zoneRect, borderPaint);
        for (Rect r : exterior)
            canvas.drawRect(r, exteriorPaint);
    }
}
