package com.example.xd720p.sensorcontroller_09082016;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.activeandroid.ActiveAndroid;
import com.example.xd720p.sensorcontroller_09082016.models.ObservationPoints;
import com.example.xd720p.sensorcontroller_09082016.models.Sensors;

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

           Button addFirst = (Button) findViewById(R.id.add_first_button);

            addFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), AddSensorActivity.class);
                    Spinner objectSpinner = (Spinner) findViewById(R.id.object_spinner);

                    i.putExtra("company", objectSpinner.getSelectedItem().toString());

                    startActivity(i);
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


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, obsNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectSpinner.setAdapter(spinnerAdapter);


        objectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListView sensorList = (ListView) findViewById(R.id.temp_list);
                List<String> allSensors = Sensors.getObjectSMSNames(parent.getSelectedItem().toString());
                ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, allSensors);

                sensorList.setAdapter(tempAdapter);
                registerForContextMenu(sensorList);

                Button addFirst = (Button) findViewById(R.id.add_first_button);
                if (allSensors.size() == 0) {
                    addFirst.setVisibility(View.VISIBLE);
                    addFirst.setClickable(true);
                } else {
                    addFirst.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//////////////////////////////////////////////
//


    }
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)

                .setTitle("Удаление")
                .setMessage("Удaлить текущий объект?")


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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.temp_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.create:
                Intent i = new Intent(getApplicationContext(), AddSensorActivity.class);
                Spinner objectSpinner = (Spinner) findViewById(R.id.object_spinner);

                i.putExtra("company", objectSpinner.getSelectedItem().toString());

                startActivity(i);
                return true;
            case R.id.edit:
                int pos = info.position;
                ListView sensorList = (ListView) findViewById(R.id.temp_list);
                String smsName = sensorList.getItemAtPosition(pos).toString();
                objectSpinner = (Spinner) findViewById(R.id.object_spinner);

                Intent p = new Intent(getApplicationContext(), EditSensorActivity.class);
                p.putExtra("company", objectSpinner.getSelectedItem().toString());
                p.putExtra("smsName", smsName);

                startActivity(p);

                return true;
            case R.id.delete:
                pos = info.position;
                sensorList = (ListView) findViewById(R.id.temp_list);
                smsName = sensorList.getItemAtPosition(pos).toString();

                objectSpinner = (Spinner) findViewById(R.id.object_spinner);

                Sensors.delete(smsName, objectSpinner.getSelectedItem().toString());

                List<String> allSensors = Sensors.getObjectSMSNames(objectSpinner.getSelectedItem().toString());
                ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, allSensors);

                sensorList.setAdapter(tempAdapter);


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
