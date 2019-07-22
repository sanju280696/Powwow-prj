package com.desire.powwow.Notifications;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PushNotifictionHelper extends AsyncTask<String, Void, String> {
    public final static String AUTH_KEY_FCM = "AAAAOhW7pNM:APA91bEe_o2jIt1Ywzf8kEIvy2B_jHYkgkLEWAWv9LC5zUif30wJZuAkOm9BnWtEIl6k9ef4ibYUx3Z_HXRqvM3sQwZUaa2TD4CYJdbneaNaWpobVNk_cjAfom3pm0vBFmugH_9ZIT9l";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    public static String TAG = PushNotifictionHelper.class.getName();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        try {
            URL url = new URL(API_URL_FCM);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();

            String fcmId = strings[0];
            String msg = strings[1];
            String sender = strings[2];
            Log.d(TAG, "doInBackground: " + fcmId + "::" + msg + "::" + sender);
            try {
                json.put("to", fcmId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject info = new JSONObject();
            try {
                info.put("title", sender); // Notification title
                info.put("body", msg); // Notification
                json.put("notification", info);
                json.put("priority", "high");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "sendPushNotification: info " + json);
            try {
                OutputStream outputStream = conn.getOutputStream();

                OutputStreamWriter wr = new OutputStreamWriter(outputStream);
                wr.write(json.toString());
                wr.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                Log.d(TAG, "sendPushNotification: Output from Server");
                while ((output = br.readLine()) != null) {
                    Log.d(TAG, "sendPushNotification: OP " + output);
                }
                result = "Success";
            } catch (Exception e) {
                e.printStackTrace();
                result = "Failure";
            }
        } catch (Exception e) {

        }

        Log.d(TAG, "sendPushNotification: FCM Notification is sent successfully ");
        return result;
    }
}