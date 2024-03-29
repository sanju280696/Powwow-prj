package com.desire.powwow;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URISyntaxException;

public class Upload extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    private String branch = "Computer Science";
    private String TAG = Upload.class.getName();
    private String extension;
    private TextView pleaseWait;
    private EditText uploadFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        Log.d(TAG, "onCreate");
        Button btnUpload = findViewById(R.id.btnUpload);
        pleaseWait = findViewById(R.id.text_wait);
        uploadFileName = findViewById(R.id.upload_filename);

        //spinner
        final Spinner spinner = findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner chatHeader
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_arrays, android.R.layout.simple_spinner_item);
        // Specify the chatHeader to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Utils.checkConnection(Upload.this)) {
                    // Not Available...
                    Snackbar.make(findViewById(android.R.id.content), "Please Connect to Internet", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                } else {
                    String fileName = uploadFileName.getText().toString();
                    if (fileName.isEmpty()) {
                        uploadFileName.setError("Please Enter File Name");
                    } else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, FILE_SELECT_CODE);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Log.e("Upload@@", " Ok ");
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.VISIBLE);
                    pleaseWait.setVisibility(View.VISIBLE);
                    pleaseWait.setText("Uploading file...");

                    LinearLayout linearLayout = findViewById(R.id.upload_content);
                    linearLayout.setVisibility(View.GONE);

                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = null;
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        System.out.print(0);
                    }
                    Log.e("Upload@@", "Path " + path);

                    File f = new File(path);
                    //System.out.println(f.);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://powwow-fac7a.appspot.com"); //gs://bonding-bad75.appspot.com

                    Uri file = Uri.fromFile(f);
                    Log.e("Upload@@", "File " + file);
                    EditText et = (EditText) findViewById(R.id.upload_filename);

                    //System.out.println(file.getLastPathSegment());
                    if (et.getText().toString().isEmpty()) {
                        Toast.makeText(Upload.this, R.string.toast_filename_req, Toast.LENGTH_SHORT).show();
                        //loadingIndicator = findViewById(R.id.loading_indicator);
                        loadingIndicator.setVisibility(View.GONE);
                        pleaseWait.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    } else {
                        extension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));
                        Log.e(TAG, "onActivityResult: " + extension);
                        StorageReference ref = storageRef.child(et.getText().toString() + extension);
                        UploadTask uploadTask = ref.putFile(file);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("Upload@@", "onFailure " + exception.toString());
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.e("Upload@@", "onSuccess ");
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference();
                                EditText et = (EditText) findViewById(R.id.upload_filename);
                                StorageMetadata metadata = taskSnapshot.getMetadata();
                                //String size =  metadata.getSizeBytes()+"";
                                FileDetails f = new FileDetails(et.getText().toString() + extension, branch);

                                myRef.child("files").push().setValue(f);

                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

//                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                View loadingIndicator = findViewById(R.id.loading_indicator);
                                loadingIndicator.setVisibility(View.GONE);
                                pleaseWait.setVisibility(View.GONE);

                                LinearLayout r = findViewById(R.id.upload_content);
                                r.setVisibility(View.VISIBLE);
                                Toast.makeText(Upload.this, R.string.toast_successfully_upload, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(Upload.this, Home.class);
                                startActivity(intent);
                            }
                        });
                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
