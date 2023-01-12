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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.uberapp_tim.dto.LoginDTO;
import com.example.uberapp_tim.model.users.Driver;
import com.example.uberapp_tim.model.users.Passenger;
import com.example.uberapp_tim.model.users.User;
import com.example.uberapp_tim.service.ServiceUtils;
import com.example.uberapp_tim.tools.Mokap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                LoginDTO loginDTO = getLoginData();

                ServiceUtils.userService.login(loginDTO).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(UserLoginActivity.this, "SUCCESS!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(UserLoginActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(UserLoginActivity.class.getName()).log(Level.SEVERE, "ERROR", t);
                    }
                });
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

    private LoginDTO getLoginData(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        return new LoginDTO(email, password);
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
