package com.desire.powwow;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.desire.powwow.Chat.UserDetails;
import com.desire.powwow.Notifications.FcmDataMode;
import com.desire.powwow.Notifications.PushNotifictionHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DiscussionSubmit extends AppCompatActivity {
    List<FcmDataMode> fcmDataModes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_submit);

        fcmDataModes = new ArrayList<>();

        Button b = findViewById(R.id.discussionSubmitButton);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final EditText title = findViewById(R.id.discussionTopic);
        final EditText desc = findViewById(R.id.discussionDescription);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals("fcmData")) {
                        for (com.google.firebase.database.DataSnapshot childSnap : dataSnapshot1.getChildren()) {
                            String fcmId = (String) childSnap.child("fcmId").getValue();
                            String uname = (String) childSnap.child("username").getValue();
                            FcmDataMode fcmDataMode = new FcmDataMode(fcmId, uname);
                            fcmDataMode.setFcmId(fcmId);
                            fcmDataMode.setName(uname);
                            fcmDataModes.add(fcmDataMode);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                final String email = user.getEmail();

                String titleString = title.getText().toString();
                String descString = desc.getText().toString();

                if (!Utils.checkConnection(DiscussionSubmit.this)) {
                    // Not Available...
                    Snackbar.make(findViewById(android.R.id.content), "Please Connect to Internet", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                } else if (titleString.isEmpty()) {
                    title.setError("Title Cannot be empty");
                } else if (titleString.startsWith(" ")) {
                    title.setError("Please Remove Space before first Character.");
                } else if (descString.isEmpty()) {
                    desc.setError("Description Cannot be empty");
                } else if (Character.isDigit(titleString.charAt(0))) {
                    title.setError("Discussion must start with character not digit.");
                } else {
                    Toast.makeText(DiscussionSubmit.this, R.string.toast_sent, Toast.LENGTH_SHORT).show();
                    ForumQuestion f = new ForumQuestion(titleString, descString, "", email);
                    myRef.child("forums").push().setValue(f);
                    getUserData(fcmDataModes, titleString);
                    finish();
                }
            }
        });
    }

    public void getUserData(List<FcmDataMode> fcmData, String msgText) {
        for (int i = 0; i < fcmData.size(); i++) {
            String chatwith = UserDetails.chatWith;
            String sender = UserDetails.username;

            Log.d("DiscussionScreen ", "getUserData: " + fcmData.get(i).getFcmId());
            new PushNotifictionHelperForMultiple().execute(fcmData.get(i).getFcmId(), msgText);
        }
    }
}

