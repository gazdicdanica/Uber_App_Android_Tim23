package com.example.uberapp_tim.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.passenger.PassengerAccountActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dto.UpdatePasswordDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.edit_activity);

        Toolbar toolbar = findViewById(R.id.toolbarEdit);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();

        TextInputLayout layout1 = findViewById(R.id.layout1);
        TextInputLayout layout2 = findViewById(R.id.layout2);
        TextInputLayout layout3 = findViewById(R.id.layout3);

        String label1 = bundle.getString("label1");
        TextInputEditText editText1 = findViewById(R.id.editTxtEdit1);
        editText1.setHint(label1);
        editText1.setText(bundle.getString("value1"));

        String label2 = bundle.getString("label2");

        TextInputEditText editText2 = findViewById(R.id.editTxtEdit2);
        TextInputEditText et = findViewById(R.id.editTxtEdit3);
        if(label2!=null){
            layout2.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);
            editText2.setText(bundle.getString("value2"));
            editText2.setHint(label2);

            if(label2.equals("New password")){
                layout3.setVisibility(View.VISIBLE);
                et.setVisibility(View.VISIBLE);
                et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et.setHint("Confirm new password");
            }
        }
        setInputType(label1, findViewById(R.id.editTxtEdit1), editText2);

        MaterialButton saveBtn = findViewById(R.id.confirm);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO promeni sve ostalo ako ces koristiti ovaj activity

                String oldPw = editText1.getText().toString();
                String newPw = editText2.getText().toString();
                String confirmPw = et.getText().toString();

                if(oldPw.equals("")){
                    layout1.setError("Required");
                }if(newPw.equals("")){
                    layout2.setError("Required");
                }if(confirmPw.equals("")){
                    layout3.setError("Required");
                    return;
                }
                if(!newPw.equals(confirmPw)){
                    layout2.setError("  ");
                    editText1.setText("");
                    editText2.setText("");
                    et.setText("");
                    layout3.setError("Passwords don't match");
                    return;
                }
                Long id = Long.valueOf(getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null));
                String jwt = "Bearer " + getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");
                ServiceUtils.userService.changePassword(jwt, id, new UpdatePasswordDTO(newPw, oldPw)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200){
                            Toast.makeText(EditActivity.this, "Password successfully updated", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EditActivity.this, PassengerAccountActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }else if(response.code() == 400){
                            layout1.setError("Invalid password");
                            editText1.setText("");
                            editText2.setText("");
                            et.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

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
        }

        return super.onOptionsItemSelected(item);
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

    private void setInputType(String label, EditText editText1, EditText editText2){
        switch(label){
            case "Name":
                editText1.setInputType(InputType.TYPE_CLASS_TEXT);
                editText2.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case "Address":
                editText1.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case "Phone":
                editText1.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case "E-mail":
                editText1.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case "Old password":
                editText1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }
    }
}
