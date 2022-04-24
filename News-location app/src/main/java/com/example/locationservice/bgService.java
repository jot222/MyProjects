package com.example.locationservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



public class bgService extends Service {

        @Override
        public IBinder onBind (Intent intent){
            // TODO: Return the communication channel to the service.
            //throw new UnsupportedOperationException("Not yet implemented");
            return null;
        }

        @Override
        public void onCreate () {
            Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDestroy () {
            Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onStart (Intent intent,int startid){
            Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
            //Multiply by 1000 for timer
            String currUsername = intent.getStringExtra("currUsername");
            int updateTime = Integer.parseInt(intent.getStringExtra("updateTime")) * 1000;

            ArrayList<String> macList = new ArrayList<String>();
            macList.add("00:24:6c:12:6e:20");
            macList.add("c8:b3:73:29:73:7e");
            macList.add("04:bd:88:64:13:40");
            macList.add("00:24:6c:13:6d:a0");
            macList.add("c0:38:96:18:32:db");
            macList.add("04:bd:88:64:16:20");
            macList.add("58:6d:8f:b1:19:be");
            macList.add("d8:c7:c8:9b:07:20");

            Timer t = new Timer();
            //Below is performed every n seconds depending on update frequency n
            t.schedule(new TimerTask(){
                @Override
                public void run(){
                    //Get random MAC
                    int index = (int)(Math.random() * macList.size());

                    Connect asyncTask = (Connect) new Connect(new Connect.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            if(output.equals("failed update")){
                                Toast.makeText(bgService.this, "Error updating location. Please restart app", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Log.d("BG UPDATE OUPUT IS ", output);
                            }

                        }
                    }).execute(currUsername, "dummyString to prevent crash", "updateMac", macList.get(index));
                                    }
            }, 0, updateTime);

        }


}