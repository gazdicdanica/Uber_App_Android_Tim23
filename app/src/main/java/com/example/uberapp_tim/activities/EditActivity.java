package com.example.uberapp_tim.activities;

import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim.R;

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

        String label1 = bundle.getString("label1");

        AppCompatTextView textView = findViewById(R.id.txtViewEdit1);
        textView.setText(label1);


        String label2 = bundle.getString("label2");

        EditText editText1 = findViewById(R.id.editTxtEdit1);
        editText1.setText(bundle.getString("value1"));

        EditText editText2 = findViewById(R.id.editTxtEdit2);
        if(label2!=null){
            AppCompatTextView textView2 = findViewById(R.id.txtViewEdit2);
            textView2.setText(label2);
            textView2.setVisibility(View.VISIBLE);

            editText2.setVisibility(View.VISIBLE);
            editText2.setText(bundle.getString("value2"));

            if(label2.equals("New password")){
                AppCompatTextView tw = findViewById(R.id.txtViewEdit3);
                EditText et = findViewById(R.id.editTxtEdit3);

                tw.setVisibility(View.VISIBLE);
                et.setVisibility(View.VISIBLE);
            }
        }
        setInputType(label1, findViewById(R.id.editTxtEdit1), editText2);

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
