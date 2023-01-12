package com.example.uberapp_tim.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.uberapp_tim.service.ServiceUtils;
import com.example.uberapp_tim.tools.AppTools;

import com.example.uberapp_tim.model.users.Passenger;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncService extends Service {

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId){
//        Intent intent1 = new Intent("SYNC_DATA");
//        int status = AppTools.getConnectivityStatus(getApplicationContext());
//        intent1.putExtra("RESULT_CODE", status);
//
//        Passenger u2 = new Passenger(2, "Danica", "Gazdic", "test2@gmail.com", "0691852001", "JNA 12", "danica", null, false, null);
//
//        if(status == AppTools.TYPE_WIFI || status == AppTools.TYPE_MOBILE){
//
//            Call<ResponseBody> call = ServiceUtils.passengerService.createPassenger(u2);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if(response.code()==200){
//                        Log.d("REZ", "Message received");
//                    }else{
//                        Log.d("REZ","Meesage recieved: "+response.code());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
//                }
//            });
//        }
//
//        sendBroadcast(intent1);
//
//        stopSelf();
//
//        return START_NOT_STICKY;
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
