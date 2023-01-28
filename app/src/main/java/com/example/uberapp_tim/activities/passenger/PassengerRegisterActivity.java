package com.example.uberapp_tim.activities.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.UserLoginActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.model.users.Passenger;
import com.example.uberapp_tim.model.users.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRegisterActivity extends AppCompatActivity {

    TextInputEditText name, lastname, email, address, phoneNum, password, confirmPassword;
    TextInputLayout nameLyt, lastnameLyt, emailLyt, addressLyt, phoneNumLyt, pass1Lyt, pass2Lyt;
    Passenger user = new Passenger();
    boolean nullFlag=false;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.passenger_register_activity);

        setViews();

        Button registerBtn = findViewById(R.id.regFormCreateAccButton);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               attemptRegister();
               if (!nullFlag) {
                   ServiceUtils.passengerService.createPassenger(user).enqueue(new Callback<User>() {
                       @Override
                       public void onResponse(Call<User> call, Response<User> response) {
                           if (response.code() == 200) {
                               Toast.makeText(PassengerRegisterActivity.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(PassengerRegisterActivity.this, UserLoginActivity.class));
                           }
                       }

                       @Override
                       public void onFailure(Call<User> call, Throwable t) {
                           Log.wtf("create Psngr:", t.getMessage());
                       }
                   });
               }
            }
        });

    }

    private void attemptRegister() {
        user.setName(checkData(name.getText().toString(), nameLyt));
        user.setLastName(checkData(lastname.getText().toString(), lastnameLyt));
        user.setEmail(checkData(email.getText().toString(), emailLyt));
        user.setAddress(checkData(address.getText().toString(), addressLyt));
        user.setPhoneNumber(checkData(phoneNum.getText().toString(), phoneNumLyt));
        if(password.getText().toString().equals(confirmPassword.getText().toString())){
            user.setPassword(checkData(password.getText().toString(), pass1Lyt));
        } else {
            pass2Lyt.setError("Passwords Do Not Match");
            nullFlag = true;
        }
    }

    private String checkData(String str, TextInputLayout lyt) {
        if (str.equals("")) {
            lyt.setError("Required");
            nullFlag = true;
        } else {
            lyt.setError("");
        }
        return str;
    }

    private void setViews() {
        name = findViewById(R.id.regFormName);
        lastname = findViewById(R.id.regFormLastName);
        email = findViewById(R.id.regFormEmail);
        address = findViewById(R.id.regFormAddress);
        phoneNum = findViewById(R.id.regFormPhoneNum);
        password = findViewById(R.id.regFormPass1);
        confirmPassword = findViewById(R.id.regFormPass2);

        nameLyt = findViewById(R.id.nameLyt);
        lastnameLyt = findViewById(R.id.lastnameLyt);
        emailLyt = findViewById(R.id.emailRegLyt);
        addressLyt = findViewById(R.id.addressLyt);
        phoneNumLyt = findViewById(R.id.phoneNumLyt);
        pass1Lyt = findViewById(R.id.pass1Lyt);
        pass2Lyt = findViewById(R.id.pass2Lyt);
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
