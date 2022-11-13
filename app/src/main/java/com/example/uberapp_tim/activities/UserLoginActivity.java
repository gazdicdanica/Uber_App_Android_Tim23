package com.example.uberapp_tim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverMainActivity;
import com.example.uberapp_tim.activities.passenger.PassengerMainActivity;
import com.example.uberapp_tim.activities.passenger.PassengerRegisterActivity;

import java.util.List;

import model.users.Driver;
import model.users.Passenger;
import model.users.User;
import tools.Mokap;

public class UserLoginActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.user_login_activity);
        editTextEmail= findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);

        Button logInButton = findViewById(R.id.button3);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = checkLogin();
                if(user == null){
                    Toast t = Toast.makeText(UserLoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT);
                    t.show();
                }else{
                    try{
                        Driver d = (Driver)user;
                        Intent i = new Intent(UserLoginActivity.this, DriverMainActivity.class);
                        i.putExtra("user", d);
                        UserLoginActivity.this.finish();
                        startActivity(i);
                    }catch(ClassCastException e){
                        Passenger p = (Passenger)user;
                        Intent i = new Intent(UserLoginActivity.this, PassengerMainActivity.class);
                        i.putExtra("user", p);
                        UserLoginActivity.this.finish();
                        startActivity(i);

                    }
                }

            }
        });
        Button signInButton = findViewById(R.id.buttonSignIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginActivity.this, PassengerRegisterActivity.class));
            }
        });

        TextView txtForgotPassword = findViewById(R.id.txtViewForgotPassword);
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Forgot password activity
//                startActivity(new Intent(UserLoginActivity.this, ));
            }
        });
    }

    private User checkLogin(){
        List<User> users = Mokap.getUsers();

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        for(User u : users){
            if(u.getEmail().equals(email)){
                if(u.getPassword().equals(password)){
                    return u;
                }else{
                    return null;
                }
            }
        }
        return null;
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
