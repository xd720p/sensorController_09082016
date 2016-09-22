package com.example.xd720p.sensorcontroller_09082016;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.example.xd720p.sensorcontroller_09082016.models.ObservationPoints;
import com.example.xd720p.sensorcontroller_09082016.models.Sensors;
import com.example.xd720p.sensorcontroller_09082016.models.SmsForView;
import com.example.xd720p.sensorcontroller_09082016.models.Temperature;
import com.example.xd720p.sensorcontroller_09082016.services.SmsAlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean mIsReceiverRegistered = false;
    UpdateReceiver mReceiver = null;

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

         int permissions_all = 1;

         String[] permissions = {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};

         if (!hasPermissions(this, permissions)) {
             ActivityCompat.requestPermissions(this, permissions, permissions_all);
         }



    // Ищем нужные вьюхи и запрашиваем необходимые разрешения



        //Прописываем слушателей для кнопок
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ObservationPoints> observationPointsList = ObservationPoints.getAllActiveTPoints();
                for (ObservationPoints item : observationPointsList) {
                    sendSms(item.getPHONE_T());
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

                    Long compID = ObservationPoints.getIdByName(objectSpinner.getSelectedItem().toString());

                    i.putExtra("company", compID);

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


        if (!mIsReceiverRegistered) {
            if (mReceiver == null)
                mReceiver = new UpdateReceiver();
            registerReceiver(mReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
            mIsReceiverRegistered = true;
        }

        Spinner objectSpinner = (Spinner) findViewById(R.id.object_spinner);

        List<String> obsNames = ObservationPoints.getObjectNames();


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, obsNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectSpinner.setAdapter(spinnerAdapter);

        SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                Context.MODE_PRIVATE);
        int spinnerValue = refreshSettings.getInt("spinner", -1);
        final int lastObs = obsNames.size() - 1;
        if (spinnerValue > lastObs) {
            spinnerValue = lastObs;
        }
        if (spinnerValue != -1) {
            objectSpinner.setSelection(spinnerValue);
        }

        objectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int choice = position;
                SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                        Context.MODE_PRIVATE);
                refreshSettings.edit().putInt("spinner", choice).commit();


                ListView sensorList = (ListView) findViewById(R.id.temp_list);

                Long companyID = ObservationPoints.getIdByName(parent.getSelectedItem().toString());

                List<Sensors> allSensors = Sensors.getSensorsForObject(companyID);
                List<String> allTemps = new ArrayList<String>();

                TextView date = (TextView) findViewById(R.id.date_text);
                TextView time = (TextView) findViewById(R.id.time_text);

                if (allSensors.size() != 0) {
                    Temperature temp = Temperature.getLastSensorTemp(allSensors.get(0).getId());

                    if (temp != null) {

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(temp.getDATE_TIME());
                        int month = cal.get(Calendar.MONTH)+2;
                        time.setText(cal.get(Calendar.DAY_OF_MONTH) + "." + month + "." + cal.get(Calendar.YEAR));
                        date.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
                    }

                }

                for (int i = 0; i < allSensors.size(); i++) {
                    if (Temperature.getLastSensorTemp(allSensors.get(i).getId()) == null) {
                        allTemps.add("---");
                    } else {
                        if (Temperature.getLastSensorTemp(allSensors.get(i).getId()).getVALUE() > 0) {
                            allTemps.add("+" + Temperature.getLastSensorTemp(allSensors.get(i).getId()).getVALUE().toString() + " \u00b0" + "C");
                        } else {
                            allTemps.add(Temperature.getLastSensorTemp(allSensors.get(i).getId()).getVALUE().toString() + " \u00b0" + "C");
                        }

                    }


                }

                List<SmsForView> viewListSms = new ArrayList<SmsForView>();

                for (int i = 0; i < allSensors.size(); i++) {

                    if (allTemps.get(i).equals("-127.0")) {
                        viewListSms.add(new SmsForView(allSensors.get(i).getSMS_NAME(), "---", allSensors.get(i).getNAME()));
                    } else {
                        viewListSms.add(new SmsForView(allSensors.get(i).getSMS_NAME(), allTemps.get(i), allSensors.get(i).getNAME()));
                    }

                }

            //    ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, allSensors);

                sensorList.setAdapter(new ListViewAdapter(MainActivity.this, -1, viewListSms));



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

    @Override
    protected void onPause() {
        if (mIsReceiverRegistered) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
            mIsReceiverRegistered = false;
        }

        super.onPause();
    }

    //Диалог на подвтерждение удаления текущего объекта
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)

                .setTitle("Удаление")
                .setMessage("Удaлить текущий объект?")


                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Spinner objectSpinner = (Spinner) findViewById(R.id.object_spinner);
                        ObservationPoints.deleteByName(objectSpinner.getSelectedItem().toString());
                        dialog.dismiss();
                        final Intent i = getIntent();
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        overridePendingTransition(0, 0);
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }

                })



                .setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
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

                Long objectID = ObservationPoints.getIdByName(objectSpinner.getSelectedItem().toString());

                i.putExtra("company", objectID);

                startActivity(i);
                return true;
            case R.id.edit:
                int pos = info.position;
                ListView sensorList = (ListView) findViewById(R.id.temp_list);
                String smsName = sensorList.getItemAtPosition(pos).toString();

                int start = smsName.indexOf('\'');
                String temp = smsName.substring(start+1);
                int end = temp.indexOf('\'');
                smsName = temp.substring(0, end);


                objectSpinner = (Spinner) findViewById(R.id.object_spinner);

                Intent p = new Intent(getApplicationContext(), EditSensorActivity.class);

                Long objectIdentificator = ObservationPoints.getIdByName(objectSpinner.getSelectedItem().toString());

                p.putExtra("company", objectIdentificator);
                p.putExtra("smsName", smsName);

                startActivity(p);

                return true;
            case R.id.delete:
                pos = info.position;
                sensorList = (ListView) findViewById(R.id.temp_list);
                smsName = sensorList.getItemAtPosition(pos).toString();

                int starts = smsName.indexOf('\'');
                String temps = smsName.substring(starts+1);
                int ends = temps.indexOf('\'');
                smsName = temps.substring(0, ends);

                objectSpinner = (Spinner) findViewById(R.id.object_spinner);

                Long compID = ObservationPoints.getIdByName(objectSpinner.getSelectedItem().toString());

                Sensors.delete(smsName, compID);
                final Intent ps = getIntent();
                ps.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);

//                Long compsID = ObservationPoints.getIdByName(objectSpinner.getSelectedItem().toString());
//
//                List<String> allSensors = Sensors.getObjectSMSNames(compsID);
//                ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, allSensors);
//
//                sensorList.setAdapter(tempAdapter);


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

        Intent i = new Intent(this, SmsAlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), SmsAlarmReceiver.REQUEST_CODE,
                i, PendingIntent.FLAG_CANCEL_CURRENT);

        SharedPreferences refreshSettings = getSharedPreferences("com.example.xd720p.sensorcontroller_09082016",
                Context.MODE_PRIVATE);

        double refreshForTValue = 0;

        if (!refreshSettings.getString("tempPeriod", "30").equals("")) {
            refreshForTValue = Double.valueOf(refreshSettings.getString("tempPeriod", "30"));
        }

        if (refreshForTValue <= 0 ) {
            cancelAlarm();
        } else {
            long period = Math.round(refreshForTValue * 60 * 1000);
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            long firstMillis = System.currentTimeMillis();
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis + period,
                    period, pendingIntent);
        }
    }

    private void cancelAlarm() {
        try {
            Intent i = new Intent(this, SmsAlarmReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(this.getApplicationContext(), SmsAlarmReceiver.REQUEST_CODE,
                    i, 0);
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.cancel(pIntent);
            pIntent.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }






    private class ListViewAdapter extends ArrayAdapter<SmsForView> {

        public ListViewAdapter(final Context context, final int resource, final List<SmsForView> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View view = convertView;

            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                final LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                view = layoutInflater.inflate(R.layout.temperature_layout, parent, false);
                holder = new ViewHolder();
                view.setTag(holder);
                holder.name = (TextView) view.findViewById(R.id.temperature_sms_list_view);
                holder.temperature = (TextView) view.findViewById(R.id.temperature_list_view);
                holder.sensorName = (TextView) view.findViewById(R.id.temperature_sensor_name);
            }

            final SmsForView item = getItem(position);
            holder.name.setText(item.getSmsName());
            holder.temperature.setText(item.getTemperature());
            holder.sensorName.setText(item.getSensorName());

            return view;
        }

        private class ViewHolder {

            TextView name;
            TextView temperature;
            TextView sensorName;

        }
    }

    public class UpdateReceiver extends BroadcastReceiver {



        public UpdateReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    final Intent i = getIntent();
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    overridePendingTransition(0, 0);
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);

                }
            }, 300);


        }
    }

}
