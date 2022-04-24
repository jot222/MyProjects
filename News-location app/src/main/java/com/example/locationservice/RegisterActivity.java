package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity{

    private EditText eUser;
    private EditText eFullName;
    private EditText ePW;
    private EditText eConfirmPW;
    private Button eSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eUser = findViewById(R.id.etUserReg);
        eFullName = findViewById(R.id.etFullName);
        ePW = findViewById(R.id.etPasswordReg);
        eConfirmPW = findViewById(R.id.etConfirmPassword);
        eSubmit = findViewById(R.id.btnregsubmit);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.interests));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        //Listener for Submit button
        eSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String inputUser = eUser.getText().toString();
                String inputFullName = eFullName.getText().toString();
                String pw1 = ePW.getText().toString();
                String pw2 = eConfirmPW.getText().toString();

                if(inputUser.isEmpty() || inputFullName.isEmpty() || pw1.isEmpty() || pw2.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else if(!pw1.equals(pw2)){
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Insert into table
                    String myInterest  = mySpinner.getSelectedItem().toString();

                    AsyncTask<String, Void, String> c = new Connect(new Connect.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            //Deal with result here
                            Log.d("Registration post", "Result from after registration = "+ output);
                            if(output.equals("successful insert")){
                                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Registration Failed: Try choosing a different username", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute(inputUser, pw1, "register", inputFullName, myInterest);
                }

            }
        });

    }

}