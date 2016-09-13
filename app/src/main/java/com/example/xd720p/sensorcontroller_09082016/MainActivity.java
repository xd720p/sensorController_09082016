package com.example.xd720p.sensorcontroller_09082016;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.activeandroid.ActiveAndroid;
import com.example.xd720p.sensorcontroller_09082016.models.ObservationPoints;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settings_button);
        ImageButton addObjectButton = (ImageButton) findViewById(R.id.add_button);
        ImageButton editObjectButton = (ImageButton) findViewById(R.id.edit_button);
        ImageButton deleteButton = (ImageButton) findViewById(R.id.delete_button);


        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RefreshingSettingsActivity.class);
                startActivity(i);
            }
        });

        addObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddObjectActivity.class);
                startActivity(i);
            }
        });

        editObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditObjectActivity.class);
                Spinner objectSpinner = (Spinner) findViewById(R.id.object_spinner);

                i.putExtra("objectName", objectSpinner.getSelectedItem().toString());

                startActivity(i);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dia = AskOption();
                dia.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Spinner objectSpinner = (Spinner) findViewById(R.id.object_spinner);

        ObservationPoints observationPoints = new ObservationPoints();
        List<String> obsNames = ObservationPoints.getObjectNames();
        List<ObservationPoints> l = ObservationPoints.getItems();


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, obsNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectSpinner.setAdapter(arrayAdapter);



    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)

                .setTitle("Удаление")
                .setMessage("Удвлить текущий объект?")


                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Spinner objectSpinner = (Spinner) findViewById(R.id.object_spinner);
                        ObservationPoints.deleteByName(objectSpinner.getSelectedItem().toString());
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
