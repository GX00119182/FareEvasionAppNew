package com.example.gavin_000.fareevasionappnew;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.example.gavin_000.fareevasionappnew.models.Offender;

public class MainActivity extends AppCompatActivity {
    //form input fields
    private TextView offenderDetails;
    private EditText editTextFName;
    private EditText editTextLName;
    private EditText editTextAddress;
    private EditText editTextDOB;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextStopName;
    private Button subButton;
    private String offenderId;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editTextFName = (EditText) findViewById(R.id.editFirstName);
        editTextLName = (EditText) findViewById(R.id.editLastName);
        editTextAddress = (EditText) findViewById(R.id.editAddress);
        editTextDOB = (EditText) findViewById(R.id.editDateOfBirth);
        editTextPhone = (EditText) findViewById(R.id.editPhone);
        editTextEmail = (EditText) findViewById(R.id.editEmail);
        editTextStopName = (EditText) findViewById(R.id.editStopName);
        subButton = (Button) findViewById(R.id.subButton);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to offenders node
        mFirebaseDatabase = mFirebaseInstance.getReference("offenders");

        // store app_title to DB node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");


        subButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String firstName = editTextFName.getText().toString();
                String lastName = editTextLName.getText().toString();
                String addressIn = editTextAddress.getText().toString();
                String dobIn = editTextDOB.getText().toString();
                String phoneIn = editTextPhone.getText().toString();
                String emailIn = editTextEmail.getText().toString();
                String stopIn = editTextStopName.getText().toString();

                // Check if offenderId already existed offenderId
                if (TextUtils.isEmpty(offenderId)) {
                    createOffender(firstName,lastName,addressIn,dobIn,phoneIn,emailIn,stopIn);
                } else {
                    Toast.makeText(getApplicationContext(), "Offender Already Exists", Toast.LENGTH_LONG).show(); //output message
                }
            }
        });
    }
    //Create a offender object to add to db.
    private void createOffender(String fname, String lname,String address,String dob,String phone,String email, String stopName)
    {
        if (TextUtils.isEmpty(offenderId)) {
            offenderId = mFirebaseDatabase.push().getKey();
        }

        Offender offender = new Offender(fname, lname, address,dob,phone,email,stopName);

        mFirebaseDatabase.child(offenderId).setValue(offender);

        addOffenderListener();
    }

    /*
    * Title: Android working with Firebase Realtime Database
    * Usage: Modified
    * URL: https://www.androidhive.info/2016/10/android-working-with-firebase-realtime-database/
    * Author: Ravi Tamada
    * Date Accessed: 18/03/18
    */

    //Add offender to db.
    private void addOffenderListener()
    {
        //Add offender data listener
        mFirebaseDatabase.child(offenderId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Offender offender = dataSnapshot.getValue(Offender.class);

                //Check if object = null
                if (offender == null)
                {
                    Toast.makeText(getApplicationContext(), "Offender data is empty", Toast.LENGTH_LONG).show();//output message
                    return;
                }

                Toast.makeText(getApplicationContext(), "Offender data was added", Toast.LENGTH_LONG).show();//output message
                //clear form fields
                editTextFName.setText("");
                editTextLName.setText("");
                editTextAddress.setText("");
                editTextDOB.setText("");
                editTextPhone.setText("");
                editTextEmail.setText("");
                editTextStopName.setText("");
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                // if failed to read data in
                Toast.makeText(getApplicationContext(), "Offender data input failed", Toast.LENGTH_LONG).show(); //output message
            }
        });
    }

}
