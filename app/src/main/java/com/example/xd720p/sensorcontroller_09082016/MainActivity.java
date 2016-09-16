package com.example.xd720p.sensorcontroller_09082016;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.example.xd720p.sensorcontroller_09082016.services.SmsAlarmReceiver;
import com.example.xd720p.sensorcontroller_09082016.services.SmsService;

import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {

         // Ищем нужные вьюхи и запрашиваем необходимые разрешения
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);

         makeAlarm();

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settings_button);
        ImageButton addObjectButton = (ImageButton) findViewById(R.id.add_button);
        ImageButton editObjectButton = (ImageButton) findViewById(R.id.edit_button);
        ImageButton deleteButton = (ImageButton) findViewById(R.id.delete_button);
        ImageButton refreshButton = (ImageButton) findViewById(R.id.refresh_button);

         int permissionSmsSendCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                 android.Manifest.permission.SEND_SMS);

         if (permissionSmsSendCheck != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(MainActivity.this,
                     new String[]{android.Manifest.permission.SEND_SMS}, 1);
         }
         // Ищем нужные вьюхи и запрашиваем необходимые разрешения


//         SharedPreferences prefs = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
//                 Context.MODE_PRIVATE);
//         SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//             @Override
//             public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//                 if (key.equals("tempPeriod")) {
//                     makeAlarm();
//                 }
//             }
//         };

        //Прописываем слушателей для кнопок
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner objectSpinner = (Spinner) findViewById(R.id.object_spinner);
                ObservationPoints point = ObservationPoints.findByName( objectSpinner.getSelectedItem().toString());
                sendSms(point.getPHONE_T());
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
    //Конец onCreate()


    //Прописываем слушателей для кнопок

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


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

    // Обновляем spinner в onResume(), чтобы информация автоматически обновлялась после редактирования
    @Override
    protected void onResume() {
        super.onResume();
        //будим часики



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
    }

    //Диалог на подвтерждение удаления текущего объекта
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

    //Создаём менюшку по долгому нажатию на элемент
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.temp_menu, menu);
    }


    //Обрабатываем выбор пункта менюшки
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


    //Функция отправки СМС
    private void sendSms(String phoneNumber) {
        String smsBody = "*";

        SmsManager smsManager = android.telephony.SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);

    }

    //а это функция для запуска AlarmManager для запросов раз в N минут
    private void makeAlarm() {
        cancelAlarm();

        Intent i = new Intent(getApplicationContext(), SmsAlarmReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), SmsAlarmReceiver.REQUEST_CODE,
                i, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();

        SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                Context.MODE_PRIVATE);

        double refreshForTValue = Double.valueOf(refreshSettings.getString("tempPeriod", "30"));

        if (refreshForTValue <= 0 ) {
            cancelAlarm();
        } else {
            long period = Math.round(refreshForTValue * 60 * 1000);
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    period, pendingIntent);
        }
    }

    private void cancelAlarm() {
        try {
            Intent i = new Intent(getApplicationContext(), SmsAlarmReceiver.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), SmsAlarmReceiver.REQUEST_CODE,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.cancel(pIntent);
            pIntent.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
