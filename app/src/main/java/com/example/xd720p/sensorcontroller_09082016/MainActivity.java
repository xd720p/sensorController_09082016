package com.example.xd720p.sensorcontroller_09082016;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.xd720p.sensorcontroller_09082016.models.ObservationPoints;
import com.example.xd720p.sensorcontroller_09082016.models.Sensors;

import java.util.List;
import java.util.jar.Manifest;

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
        ImageButton refreshButton = (ImageButton) findViewById(R.id.refresh_button);


        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionSmsSendCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.SEND_SMS);

                if (permissionSmsSendCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.SEND_SMS}, 1);
                } else {
                    sendSms("+79218711725", "*");
                }

            }
        });

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms("+79218711725", "*");

                } else {
                    AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
                    build.setMessage("NOOOOO!");
                    build.setTitle("NOOOO");
                    AlertDialog ad = build.create();
                    ad.show();

                }
                return;
            }
        }
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

    private void sendSms(String phoneNumber, String smsBody) {
        phoneNumber = "+79218711725";
        smsBody = "*";
        String SMS_SENT = "SMS_SENT";
        String SMS_DELIVERED = "SMS_DELIVERED";

        PendingIntent sendPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

        // For when the SMS has been sent
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_SENT));

        // For when the SMS has been delivered
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_DELIVERED));

        SmsManager smsManager = android.telephony.SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, smsBody, sendPendingIntent, deliveredPendingIntent);

    }

}
