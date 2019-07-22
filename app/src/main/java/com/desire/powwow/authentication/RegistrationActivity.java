
package com.desire.powwow.authentication;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.desire.powwow.Chat.UserDetails;
import com.desire.powwow.Home;
import com.desire.powwow.Notifications.FcmDataMode;
import com.desire.powwow.R;
import com.desire.powwow.User;
import com.desire.powwow.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView logo, joinus;
    private AutoCompleteTextView username, email, password;
    private Button signup;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeGUI();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.checkConnection(RegistrationActivity.this)) {
                    // Not Available...
                    Snackbar.make(findViewById(android.R.id.content), "Please Connect to Internet", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED).show();
                } else {
                    final String inputName = username.getText().toString().trim();
                    final String inputPw = password.getText().toString().trim();
                    final String inputEmail = email.getText().toString().trim();

                    if (validateInput(inputName, inputPw, inputEmail))

                        Utils.showDialog(RegistrationActivity.this, "Caution : ",
                                "Make Sure you Enter valid email address otherwise you wouldn't be able to reset your password in future.",
                                "Proceed", "Let me check again",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        registerUser(inputName, inputPw, inputEmail);
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }


    private void initializeGUI() {

        logo = findViewById(R.id.ivRegLogo);
        joinus = findViewById(R.id.ivJoinUs);
        username = findViewById(R.id.atvUsernameReg);
        email = findViewById(R.id.atvEmailReg);
        password = findViewById(R.id.atvPasswordReg);
        signin = findViewById(R.id.tvSignIn);
        signup = findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(final String inputName, final String inputPw, final String inputEmail) {
        progressDialog.setMessage("Verificating...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
//                    sendUserData(inputName, inputPw);
                    Toast.makeText(RegistrationActivity.this, "You've been registered successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, Home.class));
                    username.setText("");
                    email.setText("");
                    password.setText("");
                    String[] name = inputEmail.split("@");
                    String fcmData = FirebaseInstanceId.getInstance().getToken();
                    FcmDataMode f = new FcmDataMode(fcmData, name[0]);
                    myRef.child("fcmData").push().setValue(f);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void sendUserData(String username, String password) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference users = firebaseDatabase.getReference("users");
        //UserProfile user = new UserProfile(username, password);
        User user = new User(username, password);
        users.push().setValue(user);
    }

    private boolean validateInput(String inName, String inPw, String inEmail) {

        if (inName.isEmpty()) {
            username.setError("Username is empty.");
            return false;
        }
        if (!isValidEmailId(email.getText().toString().trim())) {
            email.setError("Please enter valid address");
            return false;
        }

        if (inPw.isEmpty()) {
            password.setError("Password is empty.");
            return false;
        }
        return true;
    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}
