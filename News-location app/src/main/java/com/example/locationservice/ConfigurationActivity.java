package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ConfigurationActivity extends AppCompatActivity {

    private EditText configName;
    private EditText configUser;
    private Button configSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuration);

        configName = findViewById(R.id.configName);
        configUser = findViewById(R.id.configUsername);
        configSave = findViewById(R.id.btnConfigSave);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ConfigurationActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.interests));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        Spinner updateSpinner = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(ConfigurationActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.updateFrequencies));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateSpinner.setAdapter(myAdapter2);

        //Fill in current info to textedits
        Intent intent = getIntent();
        String oldUserName = intent.getStringExtra("user");
        configName.setText(intent.getStringExtra("name"));
        configUser.setText(oldUserName);
        String currPassword = intent.getStringExtra("pw");

        configSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = configUser.getText().toString();
                String name = configName.getText().toString();
                String interest = mySpinner.getSelectedItem().toString();
                String updateFreq = updateSpinner.getSelectedItem().toString();

                if(user.isEmpty() || name.isEmpty()){
                    Toast.makeText(ConfigurationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else{

                    AsyncTask<String, Void, String> c = new Connect(new Connect.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            //Deal with result here
                            Log.d("UPDATE post", "Result from after UPDATE = "+ output);

                            if(output.equals("failed update")){
                                Toast.makeText(ConfigurationActivity.this, "Failed Update: Pick a different username", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intent = new Intent(ConfigurationActivity.this, HomePageActivity.class);
                                intent.putExtra("currUserInfo", output);
                                startActivity(intent);
                            }



                        }
                    }).execute(user, currPassword, "update", name, interest, updateFreq, oldUserName);
                }

            }
        });

    }
}