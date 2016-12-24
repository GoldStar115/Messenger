//==============================================================================================================================
package com.app.gaganimage;


//==============================================================================================================================
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;


//==============================================================================================================================
public class RoundedCornersFB extends ImageView
{

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void requestLayout()
    {
        if (!blockLayout_)
            super.requestLayout();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void setImageResource(int resId)
    {
        blockLayout_ = true;

        super.setImageResource(resId);

        blockLayout_ = false;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void setImageURI(Uri uri)
    {
        blockLayout_ = true;

        super.setImageURI(uri);

        blockLayout_ = false;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void setImageDrawable(Drawable drawable)
    {
        blockLayout_ = true;

        super.setImageDrawable(drawable);

        blockLayout_ = false;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void setImageBitmap(Bitmap bm)
    {
        blockLayout_ = true;

        super.setImageBitmap(bm);

        blockLayout_ = false;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public RoundedCornersFB(Context context)
    {
        super(context);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public RoundedCornersFB(Context context , AttributeSet attrs)
    {
        super(context, attrs);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public RoundedCornersFB(Context context , AttributeSet attrs , int defStyle)
    {
        super(context, attrs, defStyle);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onDraw(Canvas canvas)
    {
        Drawable maiDrawable = getDrawable();

        if (maiDrawable != null && maiDrawable instanceof BitmapDrawable && radius_ > 0)
        {
                  Paint paint        = ((BitmapDrawable) maiDrawable).getPaint();
            final int   color        = 0xff000000;
                  Rect  bitmapBounds = maiDrawable.getBounds();
            final RectF rectF        = new RectF(bitmapBounds);
                  int saveCount      = canvas.saveLayer(rectF, null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG |
                                                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                                                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

            getImageMatrix().mapRect(rectF);

            paint.setAntiAlias(true);

            canvas.drawARGB(0, 0, 0, 0);

            paint.setColor(color);

            canvas.drawRoundRect(rectF, radius_, radius_, paint);

            Xfermode oldMode = paint.getXfermode();

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            super.onDraw(canvas);

            paint.setXfermode(oldMode);

            canvas.restoreToCount(saveCount);
        }
        else
        {
            super.onDraw(canvas);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void setRadius(int radius)
    {
        this.radius_ = radius;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private boolean blockLayout_;
    private int     radius_      = 0;
}
