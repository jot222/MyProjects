package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GetNewsActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_get_news);
        String interest = intent.getStringExtra("interest");


        Connect asyncTask = (Connect) new Connect(new Connect.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                //Deal with result here
                Log.d("OUTPUT GET NEWS", "OUTPUT IS: "+output);

                if(output.equals("News Search Empty")){
                    Toast.makeText(GetNewsActivity.this, "No news for category: "+interest, Toast.LENGTH_SHORT).show();
                }
                else{
                    String [] searchResults = output.split(";");
                    int numResults = Integer.parseInt(searchResults[0]);

                    listView =(ListView) findViewById(R.id.newsList);

                    ArrayList<String> newsList = new ArrayList<>();

                    for(int i=1; i<=numResults; i++){
                        String url = searchResults[i];
                        newsList.add(url);
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(GetNewsActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, newsList){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            View view = super.getView(position, convertView, parent);
                            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                            TextView text2 = (TextView) view.findViewById(android.R.id.text2);


                            text1.setText(newsList.get(position));  //Set name on list
                            text2.setText("CATEGORY: " + interest);

                            Linkify.addLinks(text1, Linkify.WEB_URLS);



                            return view;
                        }
                    };
                    listView.setAdapter(arrayAdapter);




                }


            }
        }).execute("dummyUsernameToPreventCrash", "dummyString to prevent crash", "news", interest);



    }
}