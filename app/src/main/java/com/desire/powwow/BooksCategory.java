package com.desire.powwow;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BooksCategory extends AppCompatActivity {
    private Spinner spinner;
    private ListView listView;
    private TextView mEmptyStateTextView;
    private BookAdapter mAdapter;
    private ArrayList<BookDetails> bookDetails = new ArrayList<>();
    private String branch = "Other";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.books_category);
        spinner = (Spinner) findViewById(R.id.branch_category);
        // Create an ArrayAdapter using the string array and a default spinner chatHeader
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_arrays, android.R.layout.simple_spinner_item);
        // Specify the chatHeader to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        listView = (ListView) findViewById(R.id.listview_branch_books);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_books_view);
        listView.setEmptyView(mEmptyStateTextView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                if (dataSnapshot.getKey().equals("books")) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        String branchName = (String) messageSnapshot.child("branch").getValue();
                        if (branchName.equals(branch)) {
                            String bookname = (String) messageSnapshot.child("bookName").getValue();
                            String bookOwner = (String) messageSnapshot.child("ownerName").getValue();
                            String ownerContact = (String) messageSnapshot.child("contact").getValue();

                            BookDetails book = new BookDetails(bookname, bookOwner, ownerContact, branchName);
                            bookDetails.add(book);
                        }
                    }
                    mAdapter = new BookAdapter(BooksCategory.this, bookDetails);
                    listView.setAdapter(mAdapter);
                    View loadingIndicator = findViewById(R.id.books_loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                String key = dataSnapshot.getKey();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                String key = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BooksCategory.this, R.string.toast_books_failed_to_load,
                        Toast.LENGTH_SHORT).show();
            }
        };

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!Utils.checkConnection(BooksCategory.this)) {
                    // Not Available...
                    Snackbar.make(findViewById(android.R.id.content), "Please Connect to Internet", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
                branch = (String) parent.getItemAtPosition(position);
                if (mAdapter != null)
                    mAdapter.clear();
                myRef.addChildEventListener(childEventListener);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookDetails b = mAdapter.getItem(position);
                String arr[] = {"Owner: " + b.getOwnerName(), "Contacts: " + b.getContact()};
                AlertDialog.Builder builder = new AlertDialog.Builder(BooksCategory.this);
                builder.setTitle(b.getBookName())
                        .setItems(arr, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
}

