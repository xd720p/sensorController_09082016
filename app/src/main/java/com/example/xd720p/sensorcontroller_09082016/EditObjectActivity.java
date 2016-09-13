package com.example.xd720p.sensorcontroller_09082016;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.xd720p.sensorcontroller_09082016.models.ObservationPoints;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by xd720p on 11.09.16.
 */
public class EditObjectActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);

        Intent i = getIntent();
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button okButton = (Button) findViewById(R.id.ok_button);

        EditText tempSimEdit = (EditText) findViewById(R.id.temp_sim_edit);
        EditText tempPasswordEdit = (EditText) findViewById(R.id.temp_password_edit);
        EditText tempObjectEdit = (EditText) findViewById(R.id.temp_object_edit);
        EditText elecSimEdit = (EditText) findViewById(R.id.elec_sim_edit);
        EditText elecPasswordEdit = (EditText) findViewById(R.id.elec_password_edit);
        Switch tempSwitch = (Switch) findViewById(R.id.temperature_switch);
        Switch elecSwitch = (Switch) findViewById(R.id.electricty_switch);

        ObservationPoints currentObj = ObservationPoints.findByName(i.getStringExtra("objectName"));

        tempSimEdit.setText(currentObj.getPHONE_T());
        tempPasswordEdit.setText(currentObj.getPASSWORD_T().toString());
        tempObjectEdit.setText(currentObj.getNAME());
        elecSimEdit.setText(currentObj.getPHONE_E());
        elecPasswordEdit.setText(currentObj.getPASSWORD_E().toString());
        tempSwitch.setChecked(currentObj.getACTIVE_T() == 1);
        elecSwitch.setChecked(currentObj.getACTIVE_E() == 1);


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

                Intent i = getIntent();


                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-HH-mm-ss");
                String currentDate = df.format(c.getTime());


                ObservationPoints observationPoints = new ObservationPoints(
                        tempObjectEdit.getText().toString(),
                        0, 1,
                        tempSimEdit.getText().toString(),
                        Integer.parseInt(tempPasswordEdit.getText().toString()),
                        temp,
                        elecSimEdit.getText().toString(),
                        Integer.parseInt(elecPasswordEdit.getText().toString()),
                        elec,
                        String.valueOf(0),currentDate, currentDate);

                ObservationPoints.updateObject(observationPoints, i.getStringExtra("objectName"));
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
