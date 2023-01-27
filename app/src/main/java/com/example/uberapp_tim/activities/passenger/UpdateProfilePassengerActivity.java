package com.example.uberapp_tim.activities.passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.model.users.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfilePassengerActivity extends AppCompatActivity {

    TextInputEditText name;
    TextInputEditText lastName;
    TextInputEditText email;
    TextInputEditText address;
    TextInputEditText phoneNum;
    TextInputLayout nameLayout;
    TextInputLayout lastNameLayout;
    TextInputLayout emailLayout;
    TextInputLayout addressLayout;
    TextInputLayout phoneNumLayout;

    MaterialButton saveBtn;

    User passenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_passenger);

        name = findViewById(R.id.user_name_change);
        lastName = findViewById(R.id.user_lastName_change);
        email = findViewById(R.id.user_email_change);
        address = findViewById(R.id.user_address_change);
        phoneNum = findViewById(R.id.user_phone_change);

        nameLayout = findViewById(R.id.name_layout);
        lastNameLayout = findViewById(R.id.lastName_layout);
        emailLayout = findViewById(R.id.email_layout);
        addressLayout = findViewById(R.id.address_layout);
        phoneNumLayout = findViewById(R.id.phone_layout);

        saveBtn = findViewById(R.id.save_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = UpdateProfilePassengerActivity.this.address.getText().toString();
                String name = UpdateProfilePassengerActivity.this.name.getText().toString();
                String surname = UpdateProfilePassengerActivity.this.lastName.getText().toString();
                String email = UpdateProfilePassengerActivity.this.email.getText().toString();
                String phoneNum = UpdateProfilePassengerActivity.this.phoneNum.getText().toString();

                if (address.equals("")){
                    addressLayout.setError("Required");
                }if(name.equals("")){
                    nameLayout.setError("Required");
                }if(surname.equals("")){
                    lastNameLayout.setError("Required");
                }if(email.equals("")){
                    emailLayout.setError("Required");
                }if(phoneNum.equals("")){
                    phoneNumLayout.setError("Required");
                }

                Long id = Long.valueOf(getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", ""));
                String jwt = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");
                ServiceUtils.passengerService.updatePassenger("Bearer " + jwt, id, new User(id,name, surname, email, phoneNum, address, "", passenger.getProfilePhoto(), passenger.isBlocked())).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(UpdateProfilePassengerActivity.this, "Profile successfully updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateProfilePassengerActivity.this, PassengerAccountActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });

        SharedPreferences pref = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
        String jwt = pref.getString("accessToken", "");
        Long id = Long.valueOf(pref.getString("id", ""));
        ServiceUtils.passengerService.getPassenger("Bearer " + jwt, id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                passenger = response.body();
                setParameters();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.wtf("message",  t.getMessage());
            }
        });

    }

    private void setParameters(){
        this.name.setText(passenger.getName());
        this.lastName.setText(passenger.getLastName());
        this.email.setText(passenger.getEmail());
        this.address.setText(passenger.getAddress());
        this.phoneNum.setText(passenger.getPhoneNumber());
    }
}