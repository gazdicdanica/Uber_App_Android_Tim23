package com.example.uberapp_tim.activities.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.CarActivity;
import com.example.uberapp_tim.activities.EditActivity;
import com.example.uberapp_tim.activities.UserLoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DriverAccountActivity extends AppCompatActivity {

    TextView fullName;
    EditText email;
    EditText address;
    EditText phoneNum;
    EditText password;
    Button car;
    Button documents;
    BottomNavigationView driverNav;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.driver_account_activity);

        Toolbar toolbar = findViewById(R.id.toolbarDriverAcc);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        car = findViewById(R.id.carButton);
        documents = findViewById(R.id.papersButton);
        fullName = findViewById(R.id.driver_full_name);
        email = findViewById(R.id.user_email);
        address = findViewById(R.id.user_address);
        phoneNum = findViewById(R.id.user_phone);
        password = findViewById(R.id.user_pw);

        setListener();

        driverNav = findViewById(R.id.driverAccNav);
        driverNav.setSelectedItemId(R.id.action_account);
        driverNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        i = new Intent(DriverAccountActivity.this, DriverMainActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case (R.id.action_account):
                        return true;
                    case (R.id.action_inbox):

                        i = new Intent(DriverAccountActivity.this, DriverInboxActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
        setParameters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void setParameters(){
        fullName.setText("Petar Petrovic");
        email.setText("test@gmail.com", TextView.BufferType.EDITABLE);
        address.setText("Branka Bajica 16", TextView.BufferType.EDITABLE);
        phoneNum.setText("0642314554", TextView.BufferType.EDITABLE);
        password.setText("password", TextView.BufferType.EDITABLE);
    }

    private void setListener(){

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverAccountActivity.this, CarActivity.class));
            }
        });

        documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverAccountActivity.this, DriverPapersActivity.class));
            }
        });

        fullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriverAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Name: ");
                bundle.putString("label2", "Lastname: ");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriverAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "E-mail: ");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriverAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Address: ");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        phoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriverAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Phone: ");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriverAccountActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label1", "Old password: ");
                bundle.putString("label2", "New password: ");
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(android.R.id.home):
                this.finish();
                overridePendingTransition(0,0);
                return true;
            case (R.id.action_logout):
                Intent loginScreen =new Intent(DriverAccountActivity.this, UserLoginActivity.class);
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
        driverNav.setSelectedItemId(R.id.action_account);
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
