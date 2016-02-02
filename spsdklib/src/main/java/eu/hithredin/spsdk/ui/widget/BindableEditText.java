package eu.hithredin.spsdk.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Edit text that correct the carret moved after binding changed
 */
public class BindableEditText extends EditText {

    public BindableEditText(Context context) {
        super(context);
    }

    public BindableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BindableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean isInititalized = false;

    @Override
    public void setText(CharSequence text, BufferType type) {
        //Initialization
        if (!isInititalized) {
            super.setText(text, type);
            if (type == BufferType.EDITABLE) {
                isInititalized = true;
            }
            return;
        }

        //No change
        if (TextUtils.equals(getText(), text)) {
            return;
        }

        //Change
        int prevCaretPosition = getSelectionEnd();
        super.setText(text, type);
        setSelection(prevCaretPosition);
    }
}