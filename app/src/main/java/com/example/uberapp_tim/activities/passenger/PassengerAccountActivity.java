package com.example.uberapp_tim.activities.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.EditActivity;
import com.example.uberapp_tim.activities.UserLoginActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.model.users.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerAccountActivity extends AppCompatActivity {

    TextView fullName;
    EditText email;
    EditText address;
    EditText phoneNum;
    EditText password;
    BottomNavigationView passengerNav;

    MaterialButton changeBtn;

    User passenger;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.passenger_account_activity);

        Toolbar toolbar = findViewById(R.id.toolbarPassAcc);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        fullName = findViewById(R.id.user_full_name);
        email = findViewById(R.id.user_email);
        address = findViewById(R.id.user_address);
        phoneNum = findViewById(R.id.user_phone);
        password = findViewById(R.id.user_password);
        changeBtn = findViewById(R.id.change_btn);

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassengerAccountActivity.this, EditActivity.class);
                i.putExtra("label1", "Old password");
                i.putExtra("label2", "New password");
                startActivity(i);

            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PassengerAccountActivity.this, UpdateProfilePassengerActivity.class);
                startActivity(i);
            }
        });


        SharedPreferences pref = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
        String jwt = pref.getString("accessToken", "");
        Long id = Long.valueOf(pref.getString("id", ""));
        ServiceUtils.passengerService.getPassenger("Bearer " + jwt, id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                passenger = response.body();
                setParameters(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.wtf("message",  t.getMessage());
            }
        });

        passengerNav = findViewById(R.id.passNav);
        passengerNav.setSelectedItemId(R.id.action_account);
        passengerNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            i = new Intent(PassengerAccountActivity.this, PassengerMainActivity.class);
                            startActivity(i);
                        }

                        return true;
                    case (R.id.action_account):
                        return true;
                    case (R.id.action_inbox):
                        i = new Intent(PassengerAccountActivity.this, PassengerInboxActivity.class);
                        startActivity(i);
                        return true;

                    case (R.id.action_reports):
                        i = new Intent(PassengerAccountActivity.this, PassengerReportsActivity.class);
                        startActivity(i);
                        return true;

                }
                return false;
            }
        });


    }

    private void setParameters(User user){
        String name = user.getName() + " " + user.getLastName();
        fullName.setText(name);
        email.setText(user.getEmail());
        phoneNum.setText(user.getPhoneNumber());
        address.setText(user.getAddress());
        password.setText(user.getPassword());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(android.R.id.home):
                this.finish();
                overridePendingTransition(0,0);
                return true;
            case (R.id.action_logout):
                Intent loginScreen =new Intent(PassengerAccountActivity.this, UserLoginActivity.class);
                loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginScreen);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }


    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        passengerNav.setSelectedItemId(R.id.action_account);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
