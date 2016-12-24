// TODO: this file uses \n for line end instead of \r\n
//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TabWidget;
import android.widget.TextView;


//==============================================================================================================================
public class BadgeView extends TextView
{

    //--------------------------------------------------------------------------------------------------------------------------
    public BadgeView(Context context)
    {
        this(context, (AttributeSet) null, android.R.attr.textViewStyle);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public BadgeView(Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public BadgeView(Context context, View target)
    {
        this(context, null, android.R.attr.textViewStyle, target, 0);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public BadgeView(Context context, TabWidget target, int index)
    {
        this(context, null, android.R.attr.textViewStyle, target, index);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public BadgeView(Context context, AttributeSet attrs, int defStyle)
    {
        this(context, attrs, defStyle, null, 0);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public BadgeView(Context context, AttributeSet attrs, int defStyle, View target, int tabIndex)
    {
        super(context, attrs, defStyle);
        init(context, target, tabIndex);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean isShown()
    {
        return isShown_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void show()
    {
        show(false, null);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void show(boolean animate)
    {
        show(animate, fadeIn_);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void show(Animation anim)
    {
        show(true, anim);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void hide()
    {
        hide(false, null);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void hide(boolean animate)
    {
        hide(animate, fadeOut_);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void hide(Animation anim)
    {
        hide(true, anim);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void toggle()
    {
        toggle(false, null, null);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void toggle(boolean animate)
    {
        toggle(animate, fadeIn_, fadeOut_);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void toggle(Animation animIn, Animation animOut)
    {
        toggle(true, animIn, animOut);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public int increment(int offset)
    {
        CharSequence text  = getText();
        int          index = 0;

        if (text != null)
        {
            try
            {
                index = Integer.parseInt(text.toString());
            }
            catch (NumberFormatException error)
            {
            }
        }

        index = index + offset;

        setText(String.valueOf(index));

        return index;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public int decrement(int offset)
    {
        return increment(-offset);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void setBadgePosition(int layoutPosition)
    {
        this.badgePosition_ = layoutPosition;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void setBadgeMargin(int badgeMargin)
    {
        this.badgeMarginH_ = badgeMargin;
        this.badgeMarginV_ = badgeMargin;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void setBadgeMargin(int horizontal, int vertical)
    {
        this.badgeMarginH_ = horizontal;
        this.badgeMarginV_ = vertical;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void setBadgeBackgroundColor(int badgeColor)
    {
        this.badgeColor_ = badgeColor;
        badgeBg_         = getDefaultBackground();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public View getTarget()
    {
        return target_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public int getBadgePosition()
    {
        return badgePosition_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public int getHorizontalBadgeMargin()
    {
        return badgeMarginH_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public int getVerticalBadgeMargin()
    {
        return badgeMarginV_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public int getBadgeBackgroundColor()
    {
        return badgeColor_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void init(Context context, View target, int tabIndex)
    {
        this.context_        = context;
        this.target_         = target;
        this.targetTabIndex_ = tabIndex;

        // apply defaults
        badgePosition_ = DEFAULT_POSITION;
        badgeMarginH_  = dipToPixels(DEFAULT_MARGIN_DIP);
        badgeMarginV_  = badgeMarginH_;
        badgeColor_    = DEFAULT_BADGE_COLOR;

        setTypeface(Typeface.DEFAULT_BOLD);

        int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);

        setPadding(paddingPixels, 0, paddingPixels, 0);

        setTextColor(DEFAULT_TEXT_COLOR);

        fadeIn_ = new AlphaAnimation(0, 1);

        fadeIn_.setInterpolator(new DecelerateInterpolator());
        fadeIn_.setDuration    (200);  // TODO: mag num.

        fadeOut_ = new AlphaAnimation(1, 0);

        fadeOut_.setInterpolator(new AccelerateInterpolator());
        fadeOut_.setDuration(200);

        isShown_ = false;

        if (this.target_ != null)
            applyTo(this.target_);

        else
            show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void applyTo(View target)
    {
        LayoutParams layoutParams = target.getLayoutParams();
        ViewParent   parent       = target.getParent();
        FrameLayout  container    = new FrameLayout(context_);

        if (target instanceof TabWidget)
        {
            target = ((TabWidget) target).getChildTabViewAt(targetTabIndex_);

            this.target_ = target;

            // TODO: deprecated
            ((ViewGroup) target).addView(container, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

            this.setVisibility(View.GONE);

            container.addView(this);
        }
        else
        {
            // TODO verify that parent is indeed a ViewGroup (old)
            ViewGroup group = (ViewGroup) parent;
            int       index = group.indexOfChild(target);

            group.removeView(target);
            group.addView   (container, index, layoutParams);

            container.addView(target);

            this.setVisibility(View.GONE);

            container.addView(this);

            group.invalidate();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void show(boolean animate, Animation anim)
    {
        if (getBackground() == null)
        {
            if (badgeBg_ == null)
                badgeBg_ = getDefaultBackground();

            setBackgroundDrawable(badgeBg_);  // TODO: deprecated
        }

        applyLayoutParams();

        if (animate)
            this.startAnimation(anim);

        this.setVisibility(View.VISIBLE);

        isShown_ = true;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void hide(boolean animate, Animation anim)
    {
        this.setVisibility(View.GONE);

        if (animate)
            this.startAnimation(anim);

        isShown_ = false;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void toggle(boolean animate, Animation animIn, Animation animOut)
    {
        if (isShown_)
            hide(animate && (animOut != null), animOut);

        else
            show(animate && (animIn != null), animIn);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void applyLayoutParams()
    {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                                                                             LayoutParams.WRAP_CONTENT);

        switch (badgePosition_)
        {
        case POSITION_TOP_LEFT:
            layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            layoutParams.setMargins(badgeMarginH_, badgeMarginV_, 0, 0);

            break;

        case POSITION_TOP_RIGHT:
            layoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
            layoutParams.setMargins(0, badgeMarginV_, badgeMarginH_, 0);

            break;

        case POSITION_BOTTOM_LEFT:
            layoutParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
            layoutParams.setMargins(badgeMarginH_, 0, 0, badgeMarginV_);

            break;

        case POSITION_BOTTOM_RIGHT:
            layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            layoutParams.setMargins(0, 0, badgeMarginH_, badgeMarginV_);

            break;

        case POSITION_CENTER:
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(0, 0, 0, 0);

            break;
        }

        setLayoutParams(layoutParams);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private ShapeDrawable getDefaultBackground()
    {
        int pixel     = dipToPixels(DEFAULT_CORNER_RADIUS_DIP);
        float[] pixels = new float[] {pixel, pixel, pixel, pixel, pixel, pixel, pixel, pixel};

        RoundRectShape roundRect = new RoundRectShape(pixels, null, null);
        ShapeDrawable  drawable  = new ShapeDrawable(roundRect);

        drawable.getPaint().setColor(badgeColor_);

        return drawable;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private int dipToPixels(int dip)
    {
        Resources resources  = getResources();
        float     pixels     = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());

        return (int) pixels;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static final int POSITION_TOP_LEFT     = 1;
    public static final int POSITION_TOP_RIGHT    = 2;
    public static final int POSITION_BOTTOM_LEFT  = 3;
    public static final int POSITION_BOTTOM_RIGHT = 4;
    public static final int POSITION_CENTER       = 5;

    //--------------------------------------------------------------------------------------------------------------------------
    private static final int DEFAULT_MARGIN_DIP        = 5;
    private static final int DEFAULT_LR_PADDING_DIP    = 5;
    private static final int DEFAULT_CORNER_RADIUS_DIP = 8;
    private static final int DEFAULT_POSITION          = POSITION_TOP_RIGHT;
    private static final int DEFAULT_BADGE_COLOR       = Color.parseColor("#CCFF0000"); // Color.RED;
    private static final int DEFAULT_TEXT_COLOR        = Color.WHITE;

    //--------------------------------------------------------------------------------------------------------------------------
    private static Animation fadeIn_;
    private static Animation fadeOut_;

    //--------------------------------------------------------------------------------------------------------------------------
    private boolean       isShown_;
    private Context       context_;
    private int           badgePosition_;
    private int           badgeMarginH_;
    private int           badgeMarginV_;
    private int           badgeColor_;
    private int           targetTabIndex_;
    private ShapeDrawable badgeBg_;
    private View          target_;
}
