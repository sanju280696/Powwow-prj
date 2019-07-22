package com.desire.powwow.QuizPK;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desire.powwow.Chat.UserDetails;
import com.desire.powwow.Home;
import com.desire.powwow.R;
import com.desire.powwow.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class Quiz extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase database;
    String TAG = "Quiz";
    Button OptA, OptB, OptC, OptD;
    TextView play_button;
    TextView ques;
    public int visibility = 0, c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, c6 = 0, c7 = 0, c8 = 0, c9 = 0, c10 = 0, i, j = 0, k = 0, l = 0;
    QuizModel quizModel;
    List<QuizModel> quizModelsList;
    String global = null;
    public int ch = 0;
    int variable = 0;
    String isQuizAvl, timeStamp;
    EasyCountDownTextview countDownTextView, quizCountDown;
    RelativeLayout timerLayout, mainTimerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // TODO: 23/02/2019 Init Method Call
        init();

        database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals("quiz")) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        isQuizAvl = (String) messageSnapshot.child("isavl").getValue();
                        timeStamp = (String) messageSnapshot.child("timestamp").getValue();

                        if (isQuizAvl.equalsIgnoreCase("true")) {
                            Log.d(TAG, "onChildAdded: data true");
                            for (DataSnapshot childSnapshot : messageSnapshot.getChildren()) {
                                if (childSnapshot.getKey().equals("questions")) {
                                    for (DataSnapshot commentSnapshot : childSnapshot.getChildren()) {
                                        String question = (String) commentSnapshot.child("q").getValue();
                                        String a = (String) commentSnapshot.child("A").getValue();
                                        String b = (String) commentSnapshot.child("B").getValue();
                                        String c = (String) commentSnapshot.child("C").getValue();
                                        String d = (String) commentSnapshot.child("D").getValue();
                                        String ans = (String) commentSnapshot.child("Ans").getValue();
                                        Log.d(TAG, "onChildAdded: " + a + b + c + d + a + ans + "::" + isQuizAvl + "::" + timeStamp);

                                        quizModel = new QuizModel(a, b, c, d, question, ans);
                                        quizModelsList.add(quizModel);
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "onChildAdded: data false");
                        }
                        Log.d(TAG, "onChildAdded: username " + isQuizAvl + "desc" + timeStamp);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef.addChildEventListener(childEventListener);
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private void init() {

        // TODO: 23/02/2019 List Initialization
        quizModelsList = new ArrayList<>();

        // TODO: 23/02/2019 Button or TextView Init
        OptA = findViewById(R.id.OptionA);
        OptB = findViewById(R.id.OptionB);
        OptC = findViewById(R.id.OptionC);
        OptD = findViewById(R.id.OptionD);
        ques = findViewById(R.id.Questions);
        play_button = findViewById(R.id.play_button);

        countDownTextView = findViewById(R.id.easyCountDownTextview);
        timerLayout = findViewById(R.id.timerLayout);
        quizCountDown = findViewById(R.id.quizCountDown);
        mainTimerLayout = findViewById(R.id.maimTimerLayout);


        OptA.setBackgroundColor(getRandomColor());
        OptB.setBackgroundColor(getRandomColor());
        OptC.setBackgroundColor(getRandomColor());
        OptD.setBackgroundColor(getRandomColor());


        // TODO: 23/02/2019 OnClick Listner
        play_button.setOnClickListener(this);
        OptA.setOnClickListener(this);
        OptB.setOnClickListener(this);
        OptC.setOnClickListener(this);
        OptD.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        k++;
        switch (v.getId()) {
            case R.id.play_button:
                if (!Utils.checkConnection(Quiz.this)) {
                    // Not Available...
                    Snackbar.make(findViewById(android.R.id.content), "Please Connect to Internet", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                } else {
                    play_button.setVisibility(View.GONE);
                    if (!isQuizAvl.equalsIgnoreCase("true")) {
                        //Toast.makeText(this, "No Test Available", Toast.LENGTH_SHORT).show();
                        /**
                         * Inform user to contact administration department to know about the
                         * upcoming test
                         */
                        Utils.showDialog(Quiz.this, "No Test Available",
                                "Hello " + UserDetails.username + ",\n Please contact administration department to know about the upcoming test.",
                                "Got it!", null,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(Quiz.this, Home.class);
                                        startActivity(intent);
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                    } else {
                        timerLayout.setVisibility(View.VISIBLE);
                        countDownTextView.setTime(0, 1, 0);// setTime(hours, minute, second)

                        countDownTextView.setOnTick(new CountDownInterface() {
                            @Override
                            public void onTick(long time) {
                                Log.e("Quiz timer", "onTick Call..." + timeStamp);
                            }

                            @Override
                            public void onFinish() {
                                Log.e(TAG, "Quiz Timer: Finish Called...");
                                timerLayout.setVisibility(View.INVISIBLE);

                                startQuiz(isQuizAvl);
                                if (isQuizAvl.equalsIgnoreCase("true")) {
                                    if (visibility == 0) {
                                        //showing the buttons which were previously invisible
                                        OptA.setVisibility(View.VISIBLE);
                                        OptB.setVisibility(View.VISIBLE);
                                        OptC.setVisibility(View.VISIBLE);
                                        OptD.setVisibility(View.VISIBLE);
                                        play_button.setVisibility(View.GONE);
                                        //donutProgress.setVisibility(View.VISIBLE);
                                        mainTimerLayout.setVisibility(View.VISIBLE);
                                        quizCountDown.setVisibility(View.VISIBLE);
                                        visibility = 1;

                                        int minutes = Integer.parseInt(timeStamp);
                                        quizCountDown.setTime(0, minutes, 0);// setTime(hours, minute, second)
                                        quizCountDown.setOnTick(new CountDownInterface() {
                                            @Override
                                            public void onTick(long time) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                Toast.makeText(Quiz.this, "Time Over", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Quiz.this, Result.class);
                                                intent.putExtra("correct", l);
                                                intent.putExtra("attemp", k);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }

                break;
            case R.id.OptionA:
                if (global.equals("A")) {
                    l++;
                    ch++;
                    startQuiz(isQuizAvl);
                } else {
                    ch++;
                    startQuiz(isQuizAvl);
                }
                break;
            case R.id.OptionB:
                if (global.equals("B")) {
                    l++;
                    ch++;
                    startQuiz(isQuizAvl);
                } else {
                    ch++;
                    startQuiz(isQuizAvl);
                }
                break;
            case R.id.OptionC:
                if (global.equals("C")) {
                    l++;
                    ch++;
                    startQuiz(isQuizAvl);
                } else {
                    ch++;
                    startQuiz(isQuizAvl);
                }
                break;
            case R.id.OptionD:
                if (global.equals("D")) {
                    l++;
                    ch++;
                    startQuiz(isQuizAvl);
                } else {
                    ch++;
                    startQuiz(isQuizAvl);
                }
                break;

        }
    }

    @SuppressLint("SetTextI18n")
    private void startQuiz(String isShow) {
        int a = quizModelsList.size();
        Log.d(TAG, "startQuiz: isShow " + isShow);
        if (isShow.equalsIgnoreCase("true")) {
            Log.d(TAG, "onClick: " + quizModelsList.size());
            Log.d(TAG, "onClick: Ch " + ch + " A " + a);
            if (ch < a) {

                int currentQue = ch + 1;
                ques.setText("[Que. : " + currentQue + "]\n\n " + quizModelsList.get(ch).getQue());
                OptA.setText(quizModelsList.get(ch).getA());
                OptB.setText(quizModelsList.get(ch).getB());
                OptC.setText(quizModelsList.get(ch).getC());
                OptD.setText(quizModelsList.get(ch).getD());
                global = quizModelsList.get(ch).getAns();
            } else {
                if (variable == 0) {
                    Toast.makeText(Quiz.this, "Congratulations! You have completed Test Successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Quiz.this, Result.class);
                    intent.putExtra("correct", l);
                    intent.putExtra("attemp", k - 1);
                    startActivity(intent);
                    finish();
                }
            }
        }
        Log.d(TAG, "startQuiz: global" + global);
    }

    @Override
    protected void onPause() {
        super.onPause();
        variable = 1;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        variable = 1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        variable = 1;
        quizCountDown.stopTimer();
        countDownTextView.stopTimer();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        quizCountDown.stopTimer();
        countDownTextView.stopTimer();
    }
}