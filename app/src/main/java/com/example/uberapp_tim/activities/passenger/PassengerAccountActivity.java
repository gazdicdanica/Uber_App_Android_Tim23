package com.example.uberapp_tim.activities.passenger;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.EditActivity;
import com.example.uberapp_tim.activities.UserLoginActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.model.users.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    ImageView set;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    Uri imageuri;

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

        set = findViewById(R.id.profile_pic);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        fullName = findViewById(R.id.user_full_name);
        email = findViewById(R.id.user_email);
        address = findViewById(R.id.user_address);
        phoneNum = findViewById(R.id.user_phone);
        password = findViewById(R.id.user_password);
        changeBtn = findViewById(R.id.change_btn);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicDialog();
            }
        });

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
                setParameters(passenger);
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

    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // requesting for storage permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permission ,if given then we can click image using our camera
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // requesting for camera permission if not given
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    public void showImagePicDialog(){
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                         pickFromCamera();
                    }
                }else if(i == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {
                imageuri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
                    uploadProfileCoverPhoto(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                try {
                    Log.wtf("IMAGE", String.valueOf(imageuri !=null));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
                    uploadProfileCoverPhoto(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    private void uploadProfileCoverPhoto(final Bitmap bitmap){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        byte[] byteArr = output.toByteArray();
        String encoded = Base64.encodeToString(byteArr, Base64.DEFAULT).replace("\n", "");
        Long id = Long.valueOf(getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null));
        String jwt = "Bearer " + getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");
        ServiceUtils.passengerService.updatePassenger(jwt, id, new User(id, passenger.getName(), passenger.getLastName(), passenger.getEmail(), passenger.getPhoneNumber(), passenger.getAddress(),"", encoded, passenger.isBlocked())).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.code() == 200){
                            Toast.makeText(PassengerAccountActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("IMAGE UPLOAD", t.getMessage());
                    }
                }
        );
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageuri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }

    private void setParameters(User user){
        String name = user.getName() + " " + user.getLastName();
        fullName.setText(name);
        email.setText(user.getEmail());
        phoneNum.setText(user.getPhoneNumber());
        address.setText(user.getAddress());
        password.setText(user.getPassword());
        if(passenger.getProfilePhoto() != null){
            byte[] bytes = Base64.decode(passenger.getProfilePhoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            set.setImageBitmap(bitmap);
        }

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
                SharedPreferences sh = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
                sh.edit().clear().commit();
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
        Toast.makeText(this, "OnStart", Toast.LENGTH_SHORT).show();
        super.onStart();
        SharedPreferences pref = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
        String jwt = pref.getString("accessToken", "");
        Long id = Long.valueOf(pref.getString("id", ""));
        ServiceUtils.passengerService.getPassenger("Bearer " + jwt, id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                passenger = response.body();
                setParameters(passenger);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.wtf("message",  t.getMessage());
            }
        });
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
        Toast.makeText(this, "OnPause", Toast.LENGTH_SHORT).show();
        super.onPause();
        SharedPreferences pref = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
        String jwt = pref.getString("accessToken", "");
        Long id = Long.valueOf(pref.getString("id", ""));
        ServiceUtils.passengerService.getPassenger("Bearer " + jwt, id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                passenger = response.body();
                setParameters(passenger);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.wtf("message",  t.getMessage());
            }
        });
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
