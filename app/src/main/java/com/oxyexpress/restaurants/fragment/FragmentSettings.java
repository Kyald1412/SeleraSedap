
package com.oxyexpress.restaurants.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oxyexpress.restaurants.MainActivity;
import com.oxyexpress.restaurants.R;
import com.oxyexpress.restaurants.constanst.Constants;
import com.oxyexpress.restaurants.constanst.IMyRestaurantConstants;
import com.oxyexpress.restaurants.settings.NotificationListener;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.abtractclass.fragment.IDBFragmentConstants;
import com.ypyproductions.utils.ApplicationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class FragmentSettings extends DBFragment  {

    public static final String TAG = FragmentSettings.class.getSimpleName();


    private ArrayList<Fragment> mListFragments= new ArrayList<Fragment>();
    private MainActivity mContext;
    private Button button;
    private EditText editTextEmail;



    @Override
    public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }




    @Override
    public void findView() {
        this.mContext = (MainActivity) getActivity();

        this.button = (Button) mRootView.findViewById(R.id.buttonRegister);
        this.editTextEmail = (EditText) mRootView.findViewById(R.id.editTextEmail);

       this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if the device is not already registered
                if (!isRegistered()) {
                    //registering the device
                    registerDevice();
                    getActivity().startService(new Intent(getContext(), NotificationListener.class));
                } else {
                    //if the device is already registered
                    //displaying a toast
                    Toast.makeText(getContext(), "Already registered...", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isRegistered() {
        //Getting shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF, getContext().MODE_PRIVATE);

        //Getting the value from shared preferences
        //The second parameter is the default value
        //if there is no value in sharedprference then it will return false
        //that means the device is not registered
        return sharedPreferences.getBoolean(Constants.REGISTERED, false);
    }

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

        //Finally we need to implement a method to store this unique id to our server
        sendIdToServer(uniqueId);
    }

    private void sendIdToServer(final String uniqueId) {
        //Creating a progress dialog to show while it is storing the data on server
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Registering device...");
        progressDialog.show();

        //getting the email entered
        final String email = editTextEmail.getText().toString().trim();

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
                            Toast.makeText(getContext(), "Registered successfully", Toast.LENGTH_SHORT).show();

                            //Opening shared preference
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF, getContext().MODE_PRIVATE);

                            //Opening the shared preferences editor to save values
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Storing the unique id
                            editor.putString(Constants.UNIQUE_ID, uniqueId);

                            //Saving the boolean as true i.e. the device is registered
                            editor.putBoolean(Constants.REGISTERED, true);

                            //Applying the changes on sharedpreferences
                            editor.apply();

                            //Starting our listener service once the device is registered
                            getContext().startService(new Intent(getContext(), NotificationListener.class));
                        } else {
                            Toast.makeText(getContext(), "Choose a different email", Toast.LENGTH_SHORT).show();
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
                params.put("email", email);
                return params;
            }
        };

        //Adding the request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mListFragments!=null){
            mListFragments.clear();
            mListFragments=null;
        }
    }
}
