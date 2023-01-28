package com.example.uberapp_tim.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.UserShortDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    TextInputLayout emailLayout;
    TextInputEditText email;
    MaterialButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        this.emailLayout = findViewById(R.id.emailLayout);
        this.email = findViewById(R.id.emailInput);
        this.btn = findViewById(R.id.confirmBtn);

        this.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString();
                if(e.equals("")){
                    emailLayout.setError("Required");
                    return;
                }
                ServiceUtils.userService.sendResetEmail(new UserShortDTO(e)).enqueue(
                        new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.code() == 200){
                                    Toast.makeText(ResetPasswordActivity.this, "Check your email for unique reset code", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", e);
                                    editor.apply();

                                    Intent i = new Intent(ResetPasswordActivity.this, ResetPasswordCodeActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        }
                );
            }
        });
    }

}