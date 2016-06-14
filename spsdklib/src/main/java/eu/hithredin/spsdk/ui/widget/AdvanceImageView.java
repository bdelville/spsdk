package eu.hithredin.spsdk.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

import eu.hithredin.spsdk.R;

/**
 * Some advance feature added to the ImageView: focus state
 */
public class AdvanceImageView extends ImageView {
    private boolean stateFilterFocused;

    public static final int FOCUS_NORMAL = 0;
    public static final int FOCUS_FOCUSABLE = 1;
    public static final int NO_FOCUS_STATE = 2;
    private int focusType;

    public AdvanceImageView(Context context) {
        super(context);
    }

    public AdvanceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AdvanceImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AdvanceImageView);
        focusType = a.getInt(R.styleable.AdvanceImageView_focus_type, NO_FOCUS_STATE);
        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (focusType == NO_FOCUS_STATE) {
            return;
        }
        //isSelected() for HorizontalListView's hack, isPressed for classic view
        boolean needFilter = isPressed() || isSelected();
        if (needFilter == stateFilterFocused) {
            return;
        }

        if (needFilter) {
            setFocusImage(true);
        } else {
            setFocusImage(false);
        }
        stateFilterFocused = needFilter;
    }

    protected void setFocusImage(boolean focused) {
        if (focused) {
            //http://softwyer.files.wordpress.com/2012/01/porterduffmodes.png
            setColorFilter(Color.argb(100, 255, 255, 255), PorterDuff.Mode.SRC_ATOP);
        } else {
            setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.DST);
        }
    }

}
