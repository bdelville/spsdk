package eu.hithredin.spsdk.ui.widget;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import eu.hithredin.spsdk.app.BaseApplication;
import eu.hithredin.spsdk.ui.popup.DatePickerFragment;

/**
 * Simple TextView that handle the DatePicker and display the result
 */
public class DateFormView extends TextView implements View.OnClickListener, DatePickerFragment.DateSetListener {

    private DateFormat format;
    private Date date;

    public DateFormView(Context context) {
        super(context);
        init();
    }

    public DateFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(this);
        format = DateFormat.getDateInstance();
    }

    @Override
    public void onClick(View v) {
        DialogFragment newFragment = new DatePickerFragment(this);
        newFragment.show(((AppCompatActivity) BaseApplication.getCurrentActivity()).getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(Date date) {
        this.date = date;
        setText(format.format(date));
    }

    public DateFormat getFormat() {
        return format;
    }

    public void setFormat(DateFormat format) {
        this.format = format;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        onDateSet(date);
    }
}
