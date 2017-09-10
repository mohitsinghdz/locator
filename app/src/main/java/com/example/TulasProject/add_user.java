package com.example.TulasProject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;



public class add_user extends AppCompatActivity
{
    private String userId,myDevId;
    private static final String TAG1 = MainActivity.class.getSimpleName();
    /*private TextView txtDetails;*/
    private EditText inputName, inputEmail, inputLocation;
    private Button btnSave, btnGetLocation, get_email, get_name, btn_cancel;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        /*txtDetails = (TextView) findViewById(R.id.txt_user);*/
        inputName = (EditText) findViewById(R.id.name);
        get_email = (Button)findViewById(R.id.get_email);
        inputEmail = (EditText) findViewById(R.id.email);
        get_name = (Button)findViewById(R.id.get_name);
        btn_cancel = (Button)findViewById(R.id.btn_back);
        inputLocation = (EditText)findViewById(R.id.current_location);
        btnGetLocation = (Button)findViewById(R.id.btn_location);
        btnSave = (Button) findViewById(R.id.btn_save);
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        final String timestamp = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        myDevId = deviceUuid.toString();

        //firebase work
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Registration");
        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.e(TAG1, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);
                // update toolbar title
                getSupportActionBar().setTitle(appTitle);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                // Failed to read value
                Log.e(TAG1, "Failed to read app title value.", databaseError.toException());

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = getIntent();
                //The second parameter below is the default string returned if the value is not there.
                String get_location = i.getExtras().getString("txtData","");
                inputLocation.setText(get_location);
            }
        });

     /*   get_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent ilogin_data = getIntent();
                //The second parameter below is the default string returned if the value is not there.
                String get_email = ilogin_data.getExtras().getString("txtData","");
                inputEmail.setText(get_email);
            }
        });

        get_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent ireg_data = getIntent();
                //The second parameter below is the default string returned if the value is not there.
                String get_name = ireg_data.getExtras().getString("txtData","");
                inputName.setText(get_name);

            }
        });*/
        Intent i = getIntent();
        //The second parameter below is the default string returned if the value is not there.
        String get_location = i.getExtras().getString("txtData","");
        inputLocation.setText(get_location);
        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String userLocation = inputLocation.getText().toString();
                String time = timestamp;
                String DeviceID = myDevId;
                // Check for already existed userId
                if (TextUtils.isEmpty(userId))
                {
                    createUser(name, email, userLocation, time, DeviceID);
                } else
                {
                    updateUser(name, email, userLocation, time, DeviceID);
                }
            }
        });
        toggleButton();
    }

    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave.setText("Save");
        } else {
            btnSave.setText("Update");
        }
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String name, String email, String userLocation,String time,String DeviceID )
    {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId))
        {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name, email, userLocation, time, DeviceID);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }
    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG1, "User data is null!");
                    return;
                }

                Log.e(TAG1, "User data is changed!" + user.name + ", " + user.email + ", " + user.userLocation);

                // Display newly updated name and email
                /*txtDetails.setText(user.name + ", " + user.email + ", " + user.userLocation);*/

                // clear edit text
                inputEmail.setText("");
                inputName.setText("");
                inputLocation.setText("");
                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG1, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String name, String email, String userLocation, String time, String DeviceID)
    {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);
        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
        if (!TextUtils.isEmpty(userLocation))
            mFirebaseDatabase.child(userId).child("userLocation").setValue(userLocation);
        if (!TextUtils.isEmpty(time))
            mFirebaseDatabase.child(userId).child("time").setValue(time);
        if (!TextUtils.isEmpty(DeviceID))
            mFirebaseDatabase.child(userId).child("DeviceID").setValue(DeviceID);
    }
}

