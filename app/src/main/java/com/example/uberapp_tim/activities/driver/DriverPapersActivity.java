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
import com.example.uberapp_tim.activities.EditActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.model.vehicle.Document;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverPapersActivity extends AppCompatActivity {
    List<Document> documents = new ArrayList<>();
    Button confirm;
    ImageView idPhoto, vehicleReg;
    String[] storagePermission, cameraPermission;
    Uri idUri, vrUri;

    Document idCardResp=new Document(), vehicleRegResp=new Document();
    String jwt;
    Long id;
    Bundle data1 = new Bundle();
    Bundle data2 = new Bundle();

    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST = 400;

    boolean pickId, pickVr;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.driver_papers_activity);
        fillPhotos();

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        Toolbar toolbar = findViewById(R.id.toolbarDriverPapers);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        setListeners();
    }

    private void fillPhotos() {
        SharedPreferences pref = getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
        jwt = pref.getString("accessToken", "");
        id = Long.valueOf(pref.getString("id", ""));
        ServiceUtils.driverService.getDriverDoc("Bearer "+jwt, id).enqueue(new Callback<List<Document>>() {

            @Override
            public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {
                if (response.code() == 200) {
                    documents = response.body();
                    Log.i("Documents: ", documents.toString());
                    for (Document doc : documents) {
                        Log.i("iz fora:", doc.toString());
                        if (doc.getName().equals("ID Card")) {
                            idCardResp = doc;
                        } else if (doc.getName().equals("Vehicle Registration")){
                            vehicleRegResp = doc;
                        }
                    }
                    Log.i("DATA123:", idCardResp+" "+vehicleRegResp);
                    if (idCardResp != null) {
                        byte[] bytesId = Base64.decode(idCardResp.getDocumentImage(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytesId, 0, bytesId.length);
                        idPhoto.setImageBitmap(bitmap);
                    }
                    if (vehicleRegResp != null) {
                        byte[] bytesVR = Base64.decode(vehicleRegResp.getDocumentImage(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytesVR, 0, bytesVR.length);
                        vehicleReg.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Document>> call, Throwable t) {
                Log.wtf("get Docs: ", t.getMessage());
            }
        });
    }

    public void setListeners(){
        confirm = findViewById(R.id.confirmDriverIds);
        idPhoto = findViewById(R.id.idCardPhoto);
        vehicleReg = findViewById(R.id.vehicleRegistrationPhoto);

        idPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicker(1);
            }
        });

        vehicleReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicker(2);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showImagePicker(int flag) {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (flag == 1) {
                    if (i == 0) {
                        if (!checkCameraPermission()) {
                            requestCameraPermission(0);
                        } else {
                            pickFromCamera(0);
                        }
                    } else if (i == 1) {
                        if (!checkStoragePermission()) {
                            requestStoragePermission(0);
                        } else {
                            pickFromGallery(0);
                        }
                    }
                } else {
                    if (i == 0) {
                        if (!checkCameraPermission()) {
                            requestCameraPermission(1);
                        } else {
                            pickFromCamera(1);
                        }
                    } else if (i == 1) {
                        if (!checkStoragePermission()) {
                            requestStoragePermission(1);
                        } else {
                            pickFromGallery(1);
                        }
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery(int flag) {
        if (flag == 0) {
            pickId = true;
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_REQUEST);
        } else {
            pickVr = true;
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_REQUEST);
        }
    }

    private void requestStoragePermission(int flag) {
        if (flag == 0) {
            data1.putString("id", "0");
            requestPermissions(storagePermission, STORAGE_REQUEST);
        } else {
            data1.putString("id", "1");
            requestPermissions(storagePermission, STORAGE_REQUEST);
        }
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromCamera(int i) {
        if (i == 0) {
            pickId = false;
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
            idUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, idUri);
            startActivityForResult(camerIntent, IMAGE_PICK_CAMERA_REQUEST);
        } else {
            pickVr = false;
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
            vrUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, vrUri);
            startActivityForResult(camerIntent, IMAGE_PICK_CAMERA_REQUEST);
        }
    }

    private void requestCameraPermission(int flag) {
        if (flag == 0) {
            data2.putString("id", "0");
            requestPermissions(cameraPermission, STORAGE_REQUEST);
        } else {
            data2.putString("id", "1");
            requestPermissions(cameraPermission, STORAGE_REQUEST);
        }
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(android.R.id.home):
                this.finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (pickId) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == IMAGE_PICK_GALLERY_REQUEST) {
                    idUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), idUri);
                        uploadProfileCoverPhoto(bitmap, "ID Card");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if (requestCode == IMAGE_PICK_CAMERA_REQUEST) {
                    try {
                        Log.wtf("IMAGE", String.valueOf(idUri != null));
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), idUri);
                        uploadProfileCoverPhoto(bitmap, "ID Card");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (pickVr) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == IMAGE_PICK_GALLERY_REQUEST) {
                    vrUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), vrUri);
                        uploadProfileCoverPhoto(bitmap, "Vehicle Registration");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if (requestCode == IMAGE_PICK_CAMERA_REQUEST) {
                    try {
                        Log.wtf("IMAGE", String.valueOf(vrUri != null));
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), vrUri);
                        uploadProfileCoverPhoto(bitmap, "Vehicle Registration");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Bitmap bitmap, String what) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        byte[] byteArr = output.toByteArray();
        String encoded = Base64.encodeToString(byteArr, Base64.DEFAULT).replace("\n", "");
        Document doc = new Document(what, encoded, id);
        ServiceUtils.driverService.addDriverDoc("Bearer "+jwt, id, doc).enqueue(new Callback<Document>() {
            @Override
            public void onResponse(Call<Document> call, Response<Document> response) {
                if (response.code() == 200) {
                    Toast.makeText(DriverPapersActivity.this, "Docs Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Document> call, Throwable t) {
                Log.wtf("add DOC: ", t.getMessage());
            }
        });

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
                        String temp = data2.getString("id");
                        if (temp.equals("0")){
                            pickFromCamera(0);
                        } else {
                            pickFromCamera(1);
                        }
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
                        String temp = data1.getString("id");
                        if (temp.equals("0")) {
                            pickFromGallery(0);
                        } else {
                            pickFromCamera(1);
                        }
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }
}
