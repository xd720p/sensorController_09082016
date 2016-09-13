package com.example.xd720p.sensorcontroller_09082016;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RefreshingSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshing_settings);

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button okButton = (Button) findViewById(R.id.ok_button);

        SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                Context.MODE_PRIVATE);
        EditText tempPeriodEdit = (EditText) findViewById(R.id.temp_period_edit);
        EditText elecPeriodEdit = (EditText) findViewById(R.id.elec_period_edit);

        tempPeriodEdit.setText(refreshSettings.getString("tempPeriod", "30"));
        elecPeriodEdit.setText(refreshSettings.getString("elecPeriod", "30"));


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                        Context.MODE_PRIVATE);
                EditText tempPeriodEdit = (EditText) findViewById(R.id.temp_period_edit);
                EditText elecPeriodEdit = (EditText) findViewById(R.id.elec_period_edit);

                refreshSettings.edit().putString("tempPeriod", tempPeriodEdit.getText().toString()).commit();
                refreshSettings.edit().putString("elecPeriod", elecPeriodEdit.getText().toString()).commit();

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
