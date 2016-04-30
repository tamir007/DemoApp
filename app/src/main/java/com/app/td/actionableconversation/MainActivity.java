package com.app.td.actionableconversation;

import com.app.td.actionableconversation.Algorithm.PSTMultiClassClassifier;
import com.app.td.actionableconversation.AppUtils.CallLogInfo;
import com.app.td.actionableconversation.AppUtils.PSTUtils;
import com.app.td.actionableconversation.AppUtils.SerializationUtil;
import com.app.td.actionableconversation.DB.DB;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    String debugTag = "debug";
    PSTUtils pstUtils;
    String dbPath = "db.ser";
    static DB commonData;
    public static boolean pstSaved = false;
    public static boolean dbSaved = false;
    public static SharedPreferences  mPrefs;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(debugTag, "on create");
        super.onCreate(savedInstanceState);
        PhoneCallHandlerTrans.myContext = this;
        setContentView(R.layout.activity_main);
        pstUtils = new PSTUtils("classifier.ser");
        buildGoogleApiClient();
        PhoneCallHandlerTrans.classifier = pstUtils.loadPST();
        loadDB();
        HashMap<Character,String> mostCalled = commonData.getCToS();
        Log.i(debugTag, "Most 5 called contacts are : " + mostCalled.get('1') + " " + mostCalled.get('2') +
        " " + mostCalled.get('3') + " " + mostCalled.get('4') + " " + mostCalled.get('5'));

        // Make reset button

        Button myButton = (Button) findViewById(R.id.reset);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PhoneCallHandlerTrans.classifier = pstUtils.loadPST();
                Log.i("debug" , "classifier reset");
            }
        });


        Toast.makeText(this, "Activated Completed", Toast.LENGTH_SHORT).show();
    }

    public void saveDB(boolean isDestroy){
        SerializationUtil.serialize(commonData , dbPath , isDestroy);

    }

    public static HashMap<Character,String> getCharToContactMap(){
        return commonData.getCToS();
    }

    public static HashMap<String,Character> getContactToCharMap(){
        return commonData.getSToC();
    }

    public void loadDB(){
            Log.i(debugTag, " creating DB from scratch");
        commonData =  (DB)SerializationUtil.deserialize(this.getResources().openRawResource(R.raw.db));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(debugTag, "building google client");
        PhoneCallHandlerTrans.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(debugTag, "google: on connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(debugTag, "google: on connection failed");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(debugTag, "google: on connected");
    }

    @Override
    protected void onStop() {
        Log.i(debugTag, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(debugTag, "onDestroy)");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(debugTag, "onPause ");
        super.onPause();
    }
}