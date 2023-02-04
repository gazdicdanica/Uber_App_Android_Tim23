package com.example.uberapp_tim.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.ResetPasswordDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordCodeActivity extends AppCompatActivity {

    TextInputLayout codeLayout;
    TextInputEditText code;
    TextInputLayout pwLayout;
    TextInputEditText pw;
    MaterialButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_code);

        codeLayout = findViewById(R.id.code_layout);
        code = findViewById(R.id.code_input);
        pwLayout = findViewById(R.id.pw_layout);
        pw = findViewById(R.id.pw_input);
        btn = findViewById(R.id.resetBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = code.getText().toString();
                String p = pw.getText().toString();
                if(c.equals("")){
                    codeLayout.setError("Required");
                    return;
                }if(p.equals("")){
                    codeLayout.setError("Required");
                    return;
                }

                ServiceUtils.userService.resetPassword(new ResetPasswordDTO(getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("email", ""), c, p)).enqueue(
                        new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.code() == 404){
                                    codeLayout.setError("Invalid code");
                                    code.setText("");
                                    pw.setText("");
                                }if(response.code() == 200){
                                    Toast.makeText(ResetPasswordCodeActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ResetPasswordCodeActivity.this, UserLoginActivity.class);
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