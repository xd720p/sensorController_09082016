package com.example.xd720p.sensorcontroller_09082016;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaveToActivity extends AppCompatActivity {
    File file;
    ListView fileList;
    List<String> myList;
    String root_sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_to);

        fileList = (ListView) findViewById(R.id.file_list);
        Button path = (Button) findViewById(R.id.save_path);
        myList = new ArrayList<>();

        root_sd = Environment.getExternalStorageDirectory().toString();
        file = new File(root_sd);

        File list[] = file.listFiles();

        for (int i = 0; i < list.length; i++) {
            myList.add(list[i].getName());
        }

        fileList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList));

        path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("path", file.toString());
                setResult(1, intent);
                finish();
            }
        });

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File tempFile = new File(file, myList.get(position));

                if (!tempFile.isFile()) {
                    file = new File(file, myList.get(position));
                    File list[] = file.listFiles();

                    myList.clear();

                    File checkFile = new File("/..");

                    myList.add("/..");


                    for (int i = 0; i < list.length; i++) {
                        myList.add(list[i].getName());
                    }
                    Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();
                    fileList.setAdapter(new ArrayAdapter<String>(SaveToActivity.this,
                            android.R.layout.simple_list_item_1, myList ));
                }
            }
        });
    }
}
