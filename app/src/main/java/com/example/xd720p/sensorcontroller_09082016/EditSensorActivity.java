package com.example.xd720p.sensorcontroller_09082016;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xd720p.sensorcontroller_09082016.models.Sensors;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by xd720p on 13.09.16.
 */
public class EditSensorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        Button okButton = (Button) findViewById(R.id.ok_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        ImageButton backButton = (ImageButton) findViewById(R.id.back_button);

        TextView companyName = (TextView) findViewById(R.id.company_name);
        EditText editRoom = (EditText) findViewById(R.id.sensor_room_edit);
        EditText sensorIndex = (EditText) findViewById(R.id.sensor_index_edit);
        EditText sensorNumb = (EditText) findViewById(R.id.sensor_position_edit);
        EditText smsName = (EditText) findViewById(R.id.sensor_smsname_edit);
        Intent i = getIntent();

        Sensors sensors = Sensors.findBySmsNameAndPoint(i.getStringExtra("smsName"), i.getStringExtra("company"));

        companyName.setText(sensors.getOBSERVATION_POINT());
        editRoom.setText(sensors.getNAME());
        sensorIndex.setText(sensors.getCODE_NAME());
        sensorNumb.setText(String.valueOf(sensors.getPOSITION()));
        smsName.setText(sensors.getSMS_NAME());


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView companyName = (TextView) findViewById(R.id.company_name);
                EditText editRoom = (EditText) findViewById(R.id.sensor_room_edit);
                EditText sensorIndex = (EditText) findViewById(R.id.sensor_index_edit);
                EditText sensorNumb = (EditText) findViewById(R.id.sensor_position_edit);
                EditText smsName = (EditText) findViewById(R.id.sensor_smsname_edit);

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-HH-mm-ss");
                String currentDate = df.format(c.getTime());

                Sensors sensor = new Sensors("",
                        parseString(sensorNumb.getText().toString()), 0,
                        editRoom.getText().toString(),
                        sensorIndex.getText().toString(),
                        smsName.getText().toString(),
                        "", currentDate);

                Intent i = getIntent();
                sensor.updateObject(sensor, i.getStringExtra("smsName"), companyName.getText().toString());
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private Integer parseString(String input) {
        return input.length() != 0 ? Integer.parseInt(input) : 0;
    }

}
