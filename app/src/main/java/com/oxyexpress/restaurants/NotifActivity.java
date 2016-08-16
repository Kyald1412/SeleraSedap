package com.oxyexpress.restaurants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.oxyexpress.restaurants.constanst.Constants;
import com.oxyexpress.restaurants.settings.NotificationListener;
import com.oxyexpress.restaurants.settings.SettingManager;

import java.util.HashMap;
import java.util.Map;

public class NotifActivity extends AppCompatActivity implements View.OnClickListener {

    //Creating Views
    private Button button;
    private EditText editTextEmail;
    private CheckBox notificationCheckbox;
    private SettingManager smgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingsapp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        notificationCheckbox = (CheckBox)findViewById(R.id.subscribe);
        smgr = new SettingManager(getApplicationContext());

/*
        //if the device is registered
        if(!smgr.isRegistered()){
            startService(new Intent(this, NotificationListener.class));
        }*/

        assert notificationCheckbox != null;
        notificationCheckbox.setOnClickListener(this);

        //Initializing views
       // button = (Button) findViewById(R.id.buttonRegister);
        //editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        //Attaching an onclicklistener
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if the device is not already registered
                if (!smgr.isRegistered()) {
                    //registering the device
                    registerDevice();
                } else {
                    //if the device is already registered
                    //displaying a toast
                    Toast.makeText(NotifActivity.this, "Already registered...", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        String sentToken = smgr.unik();
        if (sentToken != null) {
            notificationCheckbox.setChecked(true);
            Log.e("TESTING",sentToken);
        } else {

            notificationCheckbox.setChecked(false);
           // Log.e("TESTING",sentToken);
        }



    }

   /* private boolean isRegistered() {
        //Getting shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);

        //Getting the value from shared preferences
        //The second parameter is the default value
        //if there is no value in sharedprference then it will return false
        //that means the device is not registered
        return sharedPreferences.getBoolean(Constants.REGISTERED, false);
    }*/



    private void registerDevice() {
        //Creating a firebase object
        Firebase firebase = new Firebase(Constants.FIREBASE_APP);

        //Pushing a new element to firebase it will automatically create a unique id
        Firebase newFirebase = firebase.push();

        //Creating a map to store name value pair
        Map<String, String> val = new HashMap<>();

        //pushing msg = none in the map
        val.put("msg", "none");

        //saving the map to firebase
        newFirebase.setValue(val);

        //Getting the unique id generated at firebase
        String uniqueId = newFirebase.getKey();

        smgr = new SettingManager(getApplicationContext());

        smgr.saveuniqueid(uniqueId);

        //Finally we need to implement a method to store this unique id to our server
        sendIdToServer(uniqueId);
    }

    private void unregisterDevice() {

        smgr = new SettingManager(getApplicationContext());

        String unik = smgr.unik();

        Firebase firebase = new Firebase("https://kyaldtestapp.firebaseio.com/"+unik);

        firebase.removeValue();

        //Finally we need to implement a method to store this unique id to our server
        deleteIdServer(unik);
    }



    private void sendIdToServer(final String uniqueId) {
        //Creating a progress dialog to show while it is storing the data on server
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering device...");
        progressDialog.show();

        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.d("Android","Android ID : "+android_id);

        //Creating a string request
        StringRequest req = new StringRequest(Request.Method.POST, Constants.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();

                        //if the server returned the string success
                        if (response.trim().equalsIgnoreCase("success")) {
                            //Displaying a success toast
                            Toast.makeText(NotifActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            startService(new Intent(getBaseContext(), NotificationListener.class));
                            smgr = new SettingManager(getApplicationContext());

                            smgr.saveNotificationSubscription(uniqueId,true);

                            notificationCheckbox.setChecked(true);
                            //notificationCheckbox.setEnabled(false);
                            notificationCheckbox.setText(getString(R.string.registered));


                            final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
                            Snackbar.make(coordinatorLayoutView, getString(R.string.registered), Snackbar.LENGTH_LONG).show();


                           /* if(smgr.unik() != null) {
                                startService(new Intent(getApplicationContext(), NotificationListener.class));
                            } else {
                                stopService(new Intent(getApplicationContext(), NotificationListener.class));
                            }*/

                        } else {
                            Toast.makeText(NotifActivity.this, "Choose a different email", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //adding parameters to post request as we need to send firebase id and email
                params.put("firebaseid", uniqueId);
                params.put("email", android_id);
                return params;
            }
        };

        //Adding the request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }


    private void deleteIdServer(final String uniqueId) {
        //Creating a progress dialog to show while it is storing the data on server
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Unregistering device...");
        progressDialog.show();

        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.d("Android","Android ID : "+android_id);

        //Creating a string request
        StringRequest req = new StringRequest(Request.Method.POST, Constants.UNREGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();

                        //if the server returned the string success
                        if (response.trim().equalsIgnoreCase("success")) {
                            //Displaying a success toast
                            Toast.makeText(NotifActivity.this, "Successfully unregister", Toast.LENGTH_SHORT).show();
                            stopService(new Intent(getBaseContext(), NotificationListener.class));
                            smgr = new SettingManager(getApplicationContext());
                            smgr.clearAllSubscriptions();

                            notificationCheckbox.setChecked(false);
                            //notificationCheckbox.setEnabled(false);
                            notificationCheckbox.setText(getString(R.string.un_register));

                            final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
                            Snackbar.make(coordinatorLayoutView, getString(R.string.un_register), Snackbar.LENGTH_LONG).show();



                            //Starting our listener service once the device is registered
                            //stopService(new Intent(getBaseContext(), NotificationListener.class));
                        } else {
                            Toast.makeText(NotifActivity.this, "Choose a different email", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //adding parameters to post request as we need to send firebase id and email
                params.put("firebaseid", uniqueId);
                params.put("email", android_id);
                return params;
            }
        };

        //Adding the request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subscribe:
                if (notificationCheckbox.isChecked())
                    registerDevice();
                else{
                    unregisterDevice();
                }
                break;
        }
    }
}
