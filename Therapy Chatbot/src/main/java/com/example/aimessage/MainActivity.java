package com.example.aimessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chatsRV;
    private EditText userMsgET;
    private FloatingActionButton sendMsgFab;
    private ArrayList<ChatsModal> chatsModalArrayList;
    private ChatRVAdapter chatRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatsRV = findViewById(R.id.idRVChats);
        userMsgET = findViewById(R.id.idEdtMessage);
        sendMsgFab = findViewById(R.id.idFABSend);

        chatsModalArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModalArrayList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatsRV.setLayoutManager(manager);
        chatsRV.setAdapter(chatRVAdapter);

        sendMsgFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard(MainActivity.this);
                //First check if message is empty or not
                if(userMsgET.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    String msg = userMsgET.getText().toString();

                    getResponse(msg);

                    //Then set line back to empty
                    userMsgET.setText("");
                }
            }
        });
    }

    private void getResponse(String message){
        //Add user message
        chatsModalArrayList.add(new ChatsModal(message, "user"));
        //Update screen
        chatRVAdapter.notifyDataSetChanged();


        Connect asyncTask = (Connect) new Connect(new Connect.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                //Deal with result here
                Log.d("OUTPUT", "OUTPUT IS: "+output);

                String[] parts = output.split(";");
                //Remove duplicates from array

                ArrayList<String> noDupes = new ArrayList<String>();
                for(String s: parts){
                    //Make sure the same thing isnt printed out twice
                    if(!noDupes.contains(s)){
                        if(s.equals("")){
                            chatsModalArrayList.add(new ChatsModal("Couldn't quite understand that phrase", "bot"));
                            chatRVAdapter.notifyDataSetChanged();
                            noDupes.add(s);
                        }
                        else{
                            chatsModalArrayList.add(new ChatsModal(s, "bot"));
                            chatRVAdapter.notifyDataSetChanged();
                            noDupes.add(s);
                        }
                    }

                }

            }
        }).execute(message);
    }

    //Method to hide the keyboard after entering a message
    //Taken from: https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-programmatically
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




}