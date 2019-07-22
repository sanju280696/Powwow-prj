package com.desire.powwow;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShareBook extends AppCompatActivity {
    private String branch = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_book_details);

        final Spinner spinner = (Spinner) findViewById(R.id.branch_upload_spinner);
        // Create an ArrayAdapter using the string array and a default spinner chatHeader
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_arrays, android.R.layout.simple_spinner_item);
        // Specify the chatHeader to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final EditText bookNameET = (EditText) findViewById(R.id.book_name);
        final EditText quantityET = (EditText) findViewById(R.id.quantity);
        final EditText ownerNameET = (EditText) findViewById(R.id.owner_name);
        final EditText contactNumberET = (EditText) findViewById(R.id.contact_number);
        Button submitBook = (Button) findViewById(R.id.bookDetails_submit);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bookName = bookNameET.getText().toString();
                String owner = ownerNameET.getText().toString();
                String contactNo = contactNumberET.getText().toString();

                if (!Utils.checkConnection(ShareBook.this)) {
                    // Not Available...
                    Snackbar.make(findViewById(android.R.id.content), "Please Connect to Internet", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                } else if (bookName.isEmpty()) {
                    bookNameET.setError("Book Name cannot be Empty");
                } else if (owner.isEmpty()) {
                    ownerNameET.setError("Book Adviser Name cannot be Empty ");
                } else if (!isValidMobile(contactNo)) {
                    contactNumberET.setError("Please enter valid Contact Number");
                } else {
                    BookDetails b = new BookDetails(bookName, owner, contactNo, branch);
                    myRef.child("books").push().setValue(b);
                    Toast.makeText(ShareBook.this, R.string.toast_submitted, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private boolean isValidMobile(String phone) {
        return !TextUtils.isEmpty(phone) && (phone.length() == 10) && android.util.Patterns.PHONE.matcher(phone).matches();

    }
}
