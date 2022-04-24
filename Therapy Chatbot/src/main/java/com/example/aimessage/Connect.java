package com.example.aimessage;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
            String userMsg = (String)params[0];
            String data = URLEncoder.encode("userMsg", "UTF-8") + "=" + URLEncoder.encode(userMsg, "UTF-8");

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
