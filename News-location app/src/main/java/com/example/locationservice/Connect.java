package com.example.locationservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Connect extends AsyncTask<String, Void, String> {

    public interface AsyncResponse{
        void processFinish(String output);
    }
    public AsyncResponse delegate = null;

    public Connect(AsyncResponse delegate){
        this.delegate = delegate;
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            String username = (String)params[0];
            String password = (String)params[1];
            String method = (String)params[2];
            String fullName = "";
            String interest = "";
            String oldUserName ="";
            String updateFreq ="";//Might need to cast to int


            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


            //Add insert information if registering
            if(method.equals("register")){
                fullName = (String)params[3];
                interest = (String)params[4];
                data += "&" + URLEncoder.encode("fullName", "UTF-8") + "=" + URLEncoder.encode(fullName, "UTF-8");
                data += "&" + URLEncoder.encode("interest", "UTF-8") + "=" + URLEncoder.encode(interest, "UTF-8");
            }
            //Add more info if updating
            if(method.equals("update")){
                fullName = (String)params[3];
                interest = (String)params[4];
                updateFreq = (String)params[5];
                oldUserName = (String)params[6];
                data += "&" + URLEncoder.encode("fullName", "UTF-8") + "=" + URLEncoder.encode(fullName, "UTF-8");
                data += "&" + URLEncoder.encode("interest", "UTF-8") + "=" + URLEncoder.encode(interest, "UTF-8");
                data += "&" + URLEncoder.encode("updateFreq", "UTF-8") + "=" + URLEncoder.encode(updateFreq, "UTF-8");
                data += "&" + URLEncoder.encode("oldUserName", "UTF-8") + "=" + URLEncoder.encode(oldUserName, "UTF-8");
            }
            if(method.equals("search")){
                String searchTerm = (String)params[3];
                data += "&" + URLEncoder.encode("searchTerm", "UTF-8") + "=" + URLEncoder.encode(searchTerm, "UTF-8");
            }
            if(method.equals("convert")){
                String macAddress = (String)params[3];
                data += "&" + URLEncoder.encode("macAddress", "UTF-8") + "=" + URLEncoder.encode(macAddress, "UTF-8");
            }
            if(method.equals("news")){
                interest = (String)params[3];
                data += "&" + URLEncoder.encode("interest", "UTF-8") + "=" + URLEncoder.encode(interest, "UTF-8");
            }
            if(method.equals("updateMac")){
                String macAddress = (String)params[3];
                data += "&" + URLEncoder.encode("macAddress", "UTF-8") + "=" + URLEncoder.encode(macAddress, "UTF-8");
            }


            Log.d("URL DEBUG", "Data IS "+data);

            //String link = "http://172.19.144.1:8888/demo/index.php";
            //String link = "http://192.168.1.137:8888/demo/index.php";
            String link = "http://10.0.2.2:8888/demo/index.php";



            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //GETS STUCK ABOVE. To fix make sure ip address is right
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;


            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            String result = sb.toString();
            Log.d("My result:", "My result is "+sb.toString());

            return sb.toString();

        }catch (Exception e) {
            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }
    }

    protected void onPostExecute(String result){
        delegate.processFinish(result);
    }


}