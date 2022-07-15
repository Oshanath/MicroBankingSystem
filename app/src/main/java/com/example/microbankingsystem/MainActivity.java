package com.example.microbankingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.microbankingsystem.ui.OpeningWindow;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.microbankingsystem.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    Button btn_test;
    EditText tv_usr_name;
    EditText tv_password;

//    String usrName = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//<<<<<<< HEAD
//
//        // initializing of buttons
//        btn_test = findViewById(R.id.button_test);
//
//
//        // calling setOnClickListner on initialized buttons
//        btn_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                testact();
//            }
//        });
//=======
//>>>>>>> 31af53fee54dcf8017ff56b87b5e3785ae6f7ede


        // Setting the system alarm to sync at 3.00pm daily
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        SharedPreferences.Editor edit = prefs.edit();
        if(!previouslyStarted) {
//          SharedPreferences.Editor edit = prefs.edit();
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
            openOpeningWindow();
        }

        // initializing of buttons
        btn_test = findViewById(R.id.button_test);
        tv_usr_name = findViewById(R.id.tv_usr_name);
        tv_password = findViewById(R.id.tv_pw);


        // calling setOnClickListner on initialized buttons
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrName = tv_usr_name.getText().toString();
                String password = tv_password.getText().toString();

                if (logInWith(usrName, password)){
                    edit.putBoolean(getString(R.string.pref_previously_signed_in), Boolean.TRUE);
                    edit.commit();
                    Toast.makeText(MainActivity.this, "Successfully logged", Toast.LENGTH_SHORT).show();
                    openOpeningWindow();
                }else{
                    Toast.makeText(MainActivity.this, "Login credentials are incorrect", Toast.LENGTH_SHORT).show();
                }

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

    public boolean logInWith(String userName, String password){
        //do the validation here
        if(userName.equals("aa") && password.equals("aa")){
            return true;
        }else{
            return false;
        }
//        return true;
    }

    public void openOpeningWindow(){
        Intent test = new Intent(this, OpeningWindow.class);
        startActivity(test);
    }

}