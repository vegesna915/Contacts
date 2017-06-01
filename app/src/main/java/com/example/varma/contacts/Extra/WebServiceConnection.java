package com.example.varma.contacts.Extra;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;



public class WebServiceConnection {


    public static JSONObject getData(String urlString, String parameters) {
        HttpURLConnection connection;
        OutputStreamWriter request;
        String response;
        JSONObject json = null;


        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");


            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line;
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            isr.close();
            reader.close();

            json = new JSONObject(response);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

}
