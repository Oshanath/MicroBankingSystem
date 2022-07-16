package com.example.microbankingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.microbankingsystem.ui.OpeningWindow;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.microbankingsystem.databinding.ActivityMainBinding;
import com.example.microbankingsystem.ui.VerificationPage;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    Button btn_test;
    EditText tv_usr_name;
    EditText tv_password;

    String url;
    String agentID;
    String username, password;

    OkHttpClient client;

    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = new OkHttpClient();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Setting the system alarm to sync at 3.00pm daily
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        SharedPreferences.Editor edit = prefs.edit();
        if(!previouslyStarted) {
          edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
          edit.commit();
          //function
            AlarmHandler alarmHandler = new AlarmHandler(this);
//            alarmHandler.cancelAlarm(SyncService.class);
            alarmHandler.setAlarm(SyncService.class);
        }

        // setting the login page to show up only in the first app launch.
        boolean previouslySignedIn = prefs.getBoolean(getString(R.string.pref_previously_signed_in), false);
        if(previouslySignedIn){
            openOpeningWindow(agentID);
        }

        // initializing of buttons
        btn_test = findViewById(R.id.button_test);
        tv_usr_name = findViewById(R.id.tv_usr_name);
        tv_password = findViewById(R.id.tv_pw);


        // calling setOnClickListner on initialized buttons
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = tv_usr_name.getText().toString();
                password = tv_password.getText().toString();

                VerifyAgent verifyAgent = new VerifyAgent();
                verifyAgent.execute();

            }
        });

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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void openOpeningWindow(String agentID){
        Intent test = new Intent(this, OpeningWindow.class);
        test.putExtra("agentID",agentID);
        startActivity(test);
    }

    public class VerifyAgent extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {

            url = "http://10.0.2.2:8083/agentSignIn";

            RequestBody formBody = new FormBody.Builder()
                    .add("agentID", username)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder().url(url).post(formBody).build();

            Response response = null;

            try {

                response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(String.valueOf(response.body().string()));
                String success = jsonObject.getString("result");

                if( success.equals("success")){
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(getString(R.string.pref_previously_signed_in), Boolean.TRUE);
                    edit.commit();
                    agentID = username;
                    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                    databaseHelper.addAgentID(username);
                    openOpeningWindow(agentID);
                }
                else if ( success.equals("user not found")) {
                    makeToast("User not found");
                }
                else if ( success.equals("password mismatch")){
                    makeToast("Wrong password");
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void makeToast(String message){
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }

}