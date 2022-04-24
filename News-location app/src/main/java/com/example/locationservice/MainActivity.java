package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText eUsername;
    private EditText ePassword;
    private Button eLogin;
    private Button eRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        eUsername = findViewById(R.id.usernamefield);
        ePassword = findViewById(R.id.passwordfield);
        eLogin  = findViewById(R.id.btnlogin);
        eRegister = findViewById(R.id.btnregister);

        //Listener for register button
        eRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //Listener for login button
        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputName = eUsername.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if(inputName.isEmpty() || inputPassword.isEmpty()){
                    //Show error message to user
                    Toast.makeText(MainActivity.this, "Please enter all credentials or register below", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Check if credentials are good
                    //AsyncTask<String, Void, String> c = new Connect().execute(inputName, inputPassword);
                    Connect asyncTask = (Connect) new Connect(new Connect.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            //Deal with result here
                            Log.d("OUTPUT LOGIN", "OUTPUT IS: "+output);
                            if(output.equals("Failed")){
                                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //Deal with output string here. Break up into fields
                                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                //Send to new activity
                                //source, destination
                                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                                intent.putExtra("currUserInfo", output);
                                intent.putExtra(("currPassword"), inputPassword);
                                startActivity(intent);
                            }
                        }
                    }).execute(inputName, inputPassword, "login");

                }
            }
        });

    }

}