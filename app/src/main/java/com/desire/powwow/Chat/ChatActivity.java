package com.desire.powwow.Chat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.desire.powwow.Notifications.FcmDataMode;
import com.desire.powwow.Notifications.PushNotifictionHelper;
import com.desire.powwow.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.desire.powwow.Notifications.MyFirebaseMessagingService.pref;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layout;
    ImageView sendButton, ivBackHeader;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    TextView tvUserNameHeader;
    private String TAG = "ChatActivity";
    List<FcmDataMode> fcmDataModes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = findViewById(R.id.layout1);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        ivBackHeader = findViewById(R.id.header_iv_back);
        tvUserNameHeader = findViewById(R.id.header_user_name);

        tvUserNameHeader.setText(UserDetails.chatWith);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        Firebase.setAndroidContext(this);

        fcmDataModes = new ArrayList<>();

        Log.e("ChatActivity", " username " + UserDetails.username + ": chatwith " + UserDetails.chatWith);

        reference1 = new Firebase("https://powwow-fac7a.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://powwow-fac7a.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(this);
        ivBackHeader.setOnClickListener(this);

        reference1.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(UserDetails.username)) {
                    addMessageBox(message, 1);
                } else {
                    addMessageBox(message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals("fcmData")) {
                        for (com.google.firebase.database.DataSnapshot childSnap : dataSnapshot1.getChildren()) {
                            String fcmId = (String) childSnap.child("fcmId").getValue();
                            String uname = (String) childSnap.child("name").getValue();
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
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        if (type == 1) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(100, 20, 20, 20);
            textView.setLayoutParams(lp);
            textView.setElevation(2);
            textView.setGravity(20);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(20);
            textView.setPadding(20, 20, 20, 20);
            textView.setBackgroundResource(R.drawable.my_message);
        } else {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 20, 100, 20);
            textView.setLayoutParams(lp);
            textView.setElevation(2);
            textView.setGravity(20);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(20);
            textView.setPadding(20, 20, 20, 20);
            textView.setBackgroundResource(R.drawable.their_message);
        }

        layout.addView(textView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_iv_back:
                onBackPressed();
                break;
            case R.id.sendButton:
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    getUserData(fcmDataModes,messageText);
                }
                messageArea.setText("");
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

                break;
        }
    }

    public void getUserData(List<FcmDataMode> fcmData,String msgText) {
        Log.d(TAG, "getUserData: called...");
        for (int i = 0; i < fcmData.size(); i++) {
            String chatwith = UserDetails.chatWith;
            String sender = UserDetails.username;
            Log.d(TAG, "getUserData: chatWith : "+chatwith);
            Log.d(TAG, "getUserData: sender : "+sender);
            if (chatwith.equalsIgnoreCase(fcmData.get(i).getName())) {
                Log.d(TAG, "getUserData: If : ");
                new PushNotifictionHelper().execute(fcmData.get(i).getFcmId(),msgText,sender);
            }
        }
    }
}