package eu.hithredin.spsdk.ui.common;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * TextWatcher is usually used only for onTextChanged. This one will be less verbose
 */
public abstract class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        onTextChanged(charSequence.toString());
    }

    public abstract void onTextChanged(String newValue);

    @Override
    public void afterTextChanged(Editable editable) {
    }
}