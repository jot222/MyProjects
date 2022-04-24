package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity {

    private TextView tvFullName;
    private TextView tvUsername;
    private TextView tvInterest;
    private TextView tvUpdate;
    private Button bGetNews;
    private Button bConfiguration;
    private Button bFindFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvInterest = findViewById(R.id.tvInterest);
        tvUpdate = findViewById(R.id.tvUpdate);
        bGetNews = findViewById(R.id.buttonGetNews);
        bConfiguration = findViewById(R.id.buttonConfiguration);
        bFindFriends = findViewById(R.id.buttonFindFriends);


        //On create get info about current user
        Intent intent = getIntent();
        String str = intent.getStringExtra("currUserInfo");
        String currPW = intent.getStringExtra("currPassword");
        String [] currUserInfo = str.split(";");


        String currUsername = currUserInfo[1];
        String currFullName = currUserInfo[2];
        String currInterest = currUserInfo[3];
        String currUpdate = currUserInfo[4];

        tvFullName.setText("Full Name: "+ currFullName);
        tvUsername.setText("User Name: " + currUsername);
        tvInterest.setText("Interest: " + currInterest);
        tvUpdate.setText("Update Frequency " + currUpdate +" seconds");


        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();

        Log.d("MAC ADDRESS", "MAC ADDRESS: " +address);


        Intent bgIntent = new Intent(this, bgService.class);
        bgIntent.putExtra("updateTime", currUpdate);
        bgIntent.putExtra("currUsername", currUsername);
        startService(bgIntent);


        //Listener for find friends button
        bFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, FindFriendsActivity.class);
                startActivity(intent);

            }
        });


        bGetNews.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, GetNewsActivity.class);
                intent.putExtra("interest", currInterest);
                startActivity(intent);

            }
        });



        //Listener for configuration button
        bConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, ConfigurationActivity.class);
                intent.putExtra("name", currFullName);
                intent.putExtra("user", currUsername);
                intent.putExtra("pw", currPW);
                startActivity(intent);
            }
        });


    }



}