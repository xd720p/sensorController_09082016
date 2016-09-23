package com.example.xd720p.sensorcontroller_09082016;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by xd720p on 22.09.16.
 */
public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // определяем текущую дату
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        SharedPreferences refreshSettings = getActivity().getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                Context.MODE_PRIVATE);
        boolean check = refreshSettings.getBoolean("firstButton", false);

        Dialog picker;

        if (check) {
            picker = new DatePickerDialog(getActivity(), this,
                    year, month-1, day);
            picker.setTitle("Выберите дату");
        } else {
            picker = new DatePickerDialog(getActivity(), this,
                    year, month, day);
            picker.setTitle("Выберите дату");
        }

        // создаем DatePickerDialog и возвращаем его


        return picker;
    }
    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText("Готово");

    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year,
                          int month, int day) {

        Button firstDate = (Button) getActivity().findViewById(R.id.first_date_button);
        TextView firstDateText = (TextView) getActivity().findViewById(R.id.first_date_text);
        TextView lastDateText = (TextView) getActivity().findViewById(R.id.last_date_text);

        ++month;

        SharedPreferences refreshSettings = getActivity().getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                Context.MODE_PRIVATE);
        boolean check = refreshSettings.getBoolean("firstButton", false);

        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        Long date = calendar.getTimeInMillis();


        if (check) {
            firstDateText.setText(day + "-" + month + "-" + year);

            refreshSettings.edit().putLong("firstDate", date).commit();
        } else {
            lastDateText.setText(day + "-" + month + "-" + year);

            refreshSettings.edit().putLong("lastDate", date).commit();
        }




    }
}