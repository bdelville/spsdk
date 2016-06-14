package eu.hithredin.spsdk.ui.popup;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Simple base for a Date picker Fragment
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface DateSetListener{
        void onDateSet(Date date);
    }

    private DateSetListener listener;

    public DatePickerFragment(){

    }

    @SuppressLint("ValidFragment")
    public DatePickerFragment(DateSetListener listener) {
        //This fragment shuld disapear instead of been rebuild onResume
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(listener != null){
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            listener.onDateSet(c.getTime());
        }

        dismiss();
    }
}