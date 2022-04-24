package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FindFriendsActivity extends AppCompatActivity {

    private TextView searchBar;
    private Button searchButton;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        searchBar = findViewById(R.id.etFriendName);
        searchButton = findViewById(R.id.btnSearchFriends);

        //Listener for find friends button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On button click get list of all users
                String searchTerm = searchBar.getText().toString();

                AsyncTask<String, Void, String> c = new Connect(new Connect.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        //Deal with result here
                        Log.d("SEARCH FRIENDS", "Result from after SEARCH = "+ output);

                        if(output.equals("Search Empty")){
                            Toast.makeText(FindFriendsActivity.this, "No matching results", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            String [] searchResults = output.split(";");
                            int numResults = Integer.parseInt(searchResults[0]);

                            listView =(ListView) findViewById(R.id.searchList);

                            ArrayList<String> nameList = new ArrayList<>();
                            ArrayList<String> macList = new ArrayList<>();

                            for(int i=0; i<numResults; i++){
                                String name = searchResults[2*i+1];
                                String mac = searchResults[2*i+2];

                                nameList.add(name);
                                macList.add(mac);
                            }
                            ArrayAdapter arrayAdapter = new ArrayAdapter(FindFriendsActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, nameList){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent){
                                    View view = super.getView(position, convertView, parent);
                                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                                    String mac = macList.get(position);

                                    text1.setText(nameList.get(position));  //Set name on list

                                    //Convert mac address to building and floor here
                                    Connect asyncTask = (Connect) new Connect(new Connect.AsyncResponse() {
                                        @Override
                                        public void processFinish(String output) {
                                            if(output.equals("Failure")){
                                                text2.setText("Couldn't match MAC with known location");
                                            }
                                            else{
                                                String [] locationInfo = output.split(";");
                                                String building = locationInfo[1];
                                                String floor = locationInfo[2];
                                                text2.setText(building+": "+floor); //Set location on list
                                            }
                                        }
                                    }).execute("dummyUsername", "dummyPassword", "convert", mac);
                                    return view;
                                }
                            };
                            listView.setAdapter(arrayAdapter);
                        }
                    }
                }).execute("dummyUsername", "dummyPassword", "search", searchTerm);

            }
        });
    }

}