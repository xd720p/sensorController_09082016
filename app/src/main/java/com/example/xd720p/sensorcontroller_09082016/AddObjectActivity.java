package com.example.xd720p.sensorcontroller_09082016;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.activeandroid.ActiveAndroid;
import com.example.xd720p.sensorcontroller_09082016.models.ObservationPoints;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class AddObjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);
        ActiveAndroid.initialize(this);

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button okButton = (Button) findViewById(R.id.ok_button);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservationPoints observationPoints = new ObservationPoints();

                System.out.println("lalaslda");
                finish();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tempSimEdit = (EditText) findViewById(R.id.temp_sim_edit);
                EditText tempPasswordEdit = (EditText) findViewById(R.id.temp_password_edit);
                EditText tempObjectEdit = (EditText) findViewById(R.id.temp_object_edit);
                EditText elecSimEdit = (EditText) findViewById(R.id.elec_sim_edit);
                EditText elecPasswordEdit = (EditText) findViewById(R.id.elec_password_edit);
                Switch tempSwitch = (Switch) findViewById(R.id.temperature_switch);
                Switch elecSwitch = (Switch) findViewById(R.id.electricty_switch);

                Integer temp = (tempSwitch.isChecked()) ? 1 : 0;
                Integer elec = (elecSwitch.isChecked()) ? 1 : 0;

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-HH-mm-ss");
                String currentDate = df.format(c.getTime());


                ObservationPoints observationPoints = new ObservationPoints(
                        tempObjectEdit.getText().toString(),
                        0, 1,
                        tempSimEdit.getText().toString(),
                        parseString(tempPasswordEdit.getText().toString()),
                        temp,
                        elecSimEdit.getText().toString(),
                        parseString(elecPasswordEdit.getText().toString()),
                        elec,
                        "",currentDate, currentDate);

                if (checkObject(observationPoints)) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddObjectActivity.this);

                    builder.setTitle("Ошибка")
                            .setMessage("Объект с таким именем уже существует")
                            .setCancelable(false)
                            .setNegativeButton("Ок",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    observationPoints.save();
                    finish();
                }

            }
        });

    }

    private Integer parseString(String input) {
        return input.length() != 0 ? Integer.parseInt(input) : 0;
    }

    private boolean checkObject(ObservationPoints input) {
        ObservationPoints temp = ObservationPoints.findByName(input.getNAME());

        return temp!=null;
    }

}
