package com.example.uberapp_tim.activities.driver;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.CarActivity;
import com.example.uberapp_tim.activities.EditActivity;
import com.example.uberapp_tim.activities.UserLoginActivity;
import com.example.uberapp_tim.activities.passenger.PassengerAccountActivity;
import com.example.uberapp_tim.activities.passenger.UpdateProfilePassengerActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.model.users.Driver;
import com.example.uberapp_tim.model.users.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverAccountActivity extends AppCompatActivity {

    TextView fullName;
    EditText email, phoneNum, address, password;
    Button car, documents, updateBtn;
    BottomNavigationView driverNav;
    User driver;
    ImageView set;
    String[] storagePermission, cameraPermission;
    Uri imageUri;

    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST = 400;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.driver_account_activity);
        fillTextWithData();

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
        updateBtn = findViewById(R.id.updateDataBtn);

        set = findViewById(R.id.profile_pic);

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
    }

    private void fillTextWithData() {
        SharedPreferences pref = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
        String jwt = pref.getString("accessToken", "");
        Long id = Long.valueOf(pref.getString("id", ""));
        ServiceUtils.driverService.getDriver("Bearer "+jwt, id).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                driver = response.body();
                setParameters(driver);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.wtf("message fill data: ", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void setParameters(User user){
        String fn = user.getName() + " " + user.getLastName();
        fullName.setText(fn);
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        phoneNum.setText(user.getPhoneNumber());
        password.setText(user.getPassword());
        if(driver.getProfilePhoto() != null){
            byte[] bytes = Base64.decode(driver.getProfilePhoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            set.setImageBitmap(bitmap);
        }

    }

    private void showImagePicker() {
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

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_REQUEST);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(camerIntent, IMAGE_PICK_CAMERA_REQUEST);
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, STORAGE_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void setListener(){
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriverAccountActivity.this, EditActivity.class);
                i.putExtra("label1", "Old password");
                i.putExtra("label2", "New password");
                startActivity(i);
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicker();
            }
        });

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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriverAccountActivity.this, UpdateProfilePassengerActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_REQUEST) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    uploadProfileCoverPhoto(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (requestCode == IMAGE_PICK_CAMERA_REQUEST) {
                try {
                    Log.wtf("IMAGE", String.valueOf(imageUri !=null));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    uploadProfileCoverPhoto(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Bitmap bitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        byte[] byteArr = output.toByteArray();
        String encoded = Base64.encodeToString(byteArr, Base64.DEFAULT).replace("\n", "");
        Long id = Long.valueOf(getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null));
        String jwt = "Bearer " + getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");
        ServiceUtils.driverService.updateDriver(jwt, id, new User(id, driver.getName(), driver.getLastName(), driver.getEmail(), driver.getPhoneNumber(), driver.getAddress(),"", encoded, driver.isBlocked())).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.code() == 200){
                            Toast.makeText(DriverAccountActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("IMAGE UPLOAD", t.getMessage());
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }

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
                Long id = Long.valueOf(sh.getString("id", null));
                String jwt = "Bearer " + sh.getString("accessToken", null);
                ServiceUtils.driverService.endShift(jwt, id).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

                sh.edit().clear().commit();

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
        fillTextWithData();
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
