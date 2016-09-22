package com.example.xd720p.sensorcontroller_09082016;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.xd720p.sensorcontroller_09082016.models.ObservationPoints;
import com.example.xd720p.sensorcontroller_09082016.models.Sensors;
import com.example.xd720p.sensorcontroller_09082016.models.Temperature;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SaveActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);


        Spinner objectSpinner = (Spinner) findViewById(R.id.objects_spinner);
        final Button firstDate = (Button) findViewById(R.id.first_date_button);
        final Button lastDate = (Button) findViewById(R.id.last_date_button);
        Button okButton = (Button) findViewById(R.id.ok_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button pathButton = (Button) findViewById(R.id.find_path);

        List<String> obsNames = ObservationPoints.getObjectNames();


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, obsNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectSpinner.setAdapter(spinnerAdapter);


        firstDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                        Context.MODE_PRIVATE);
                refreshSettings.edit().putBoolean("firstButton", true).commit();


                DialogFragment dateDialog = new DatePicker();
                dateDialog.show(getSupportFragmentManager(), "datePicker");
            }
        });

        lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                        Context.MODE_PRIVATE);
                refreshSettings.edit().putBoolean("firstButton", false).commit();

                DialogFragment dateDialog = new DatePicker();
                dateDialog.show(getSupportFragmentManager(), "datePicker");
            }
        });

        pathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SaveToActivity.class);
                startActivityForResult(i, 1);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                        Context.MODE_PRIVATE);

                Long firstMillis = refreshSettings.getLong("firstDate", 0);
                Long lastMillis = refreshSettings.getLong("lastDate", -1);

                if (firstMillis > lastMillis) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SaveActivity.this);

                    builder.setTitle("Неверные даты")
                            .setMessage("Вы ввели некорректную дату")
                            .setCancelable(false)
                            .setNegativeButton("Ок",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }

                Spinner objectSpinner = (Spinner) findViewById(R.id.objects_spinner);

                if (objectSpinner.getSelectedItem() != null) {
                    createSCVFIle();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SaveActivity.this);

                    builder.setTitle("Нет объекта")
                            .setMessage("Не выбран объект для сохранения")
                            .setCancelable(false)
                            .setNegativeButton("Ок",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {return;}

        TextView pathText = (TextView) findViewById(R.id.path_text);
        Button okButton = (Button) findViewById(R.id.ok_button);
        okButton.setVisibility(View.VISIBLE);


        String name = data.getStringExtra("path");
        pathText.setText(name);
    }

    private void createSCVFIle() {

        TextView path = (TextView) findViewById(R.id.path_text);
        TextView startDate = (TextView) findViewById(R.id.first_date_text);
        TextView lastDate = (TextView) findViewById(R.id.last_date_text);

        Spinner objectSpinner = (Spinner) findViewById(R.id.objects_spinner);

        List<String[]> resList = new ArrayList<String[]>();
        List<String> tempStrings = new ArrayList<>();

        SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                Context.MODE_PRIVATE);

        Long firstMillis = refreshSettings.getLong("firstDate", 0);
        Long lastMillis = refreshSettings.getLong("lastDate", -1);
        String objName = objectSpinner.getSelectedItem().toString();
        Long objID = ObservationPoints.getIdByName(objName);


        List<Sensors> allSens = Sensors.getSensorsForObject(objID);
        List<Temperature> allTemps = Temperature.getTempsForObjByDate(firstMillis, lastMillis, objID);

        tempStrings.add("Date/time");
        for (int i = 0; i < allSens.size(); i++) {
            int j = i + 1;
            String temp = new String("Temperature " + j);
            tempStrings.add(temp);
        }

        String[] header = new String[tempStrings.size()];
        header = tempStrings.toArray(header);
        resList.add(header);

        int p = 0;
        while (p < allTemps.size()) {
            tempStrings = new ArrayList<>();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Long millis = allTemps.get(p).getDATE_TIME();
            String date = dateFormat.format(millis);

            tempStrings.add(date);

            for (int i = 0; i < allSens.size(); i++) {
                tempStrings.add(allTemps.get(p).getVALUE().toString());
                ++p;
            }

            String[] values = new String[tempStrings.size()];
            values = tempStrings.toArray(values);
            resList.add(values);

        }
        int l =0;


        String baseDir = path.getText().toString();
        String fileName = new String(objName + " " + startDate.getText().toString() + " " + lastDate.getText().toString() + ".csv");
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer;

        try {
            if (f.exists() && !f.isDirectory()) {
                FileWriter fileWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(fileWriter);
            } else {
                writer = new CSVWriter(new FileWriter(filePath));
            }
            writer.writeAll(resList);

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
