package gpovallas.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.widget.DatePicker;

class DatePickerDialog extends android.app.DatePickerDialog {
    public DatePickerDialog(Context context, OnDateSetListener callBack,
            int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        updateTitle(year, monthOfYear, dayOfMonth);
    }
    public void onDateChanged(DatePicker view, int year,
            int month, int day) {
        updateTitle(year, month, day);
    }
    private void updateTitle(int year, int month, int day) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        setTitle(getFormat().format(mCalendar.getTime()));
    }   
        /*
         * the format for dialog tile,and you can override this method
         */
    public SimpleDateFormat getFormat(){
        return new SimpleDateFormat("dd/MM/yyyy");
    };
}
