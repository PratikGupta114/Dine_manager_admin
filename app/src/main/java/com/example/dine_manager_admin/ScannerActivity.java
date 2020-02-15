package com.example.dine_manager_admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "Scanner Activity";
    private static final int PERMISSION_REQUEST_CAMERA = 1001;

    private ZXingScannerView scannerView;
    private Toast toast;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scanner);

        scannerView = new ZXingScannerView(ScannerActivity.this);
        setContentView(scannerView);

        // Request permission. This does it asynchronously so we have to wait for onRequestPermissionResult before trying to open the camera.
        if (!haveCameraPermission()) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void handleResult(Result result) {

        String decryptedMessage = "";
        String key = getResources().getString(R.string.crypto_key);

        try {
            decryptedMessage = AESCrypt.decrypt(key, result.getText());
        } catch (Exception e) {
            //Toast.makeText(this, "Decryption Failed", Toast.LENGTH_SHORT).show();
            showAToast("Decryption Failed");
            //generateFailureBeep();
            Log.e(TAG, e.getMessage());
            onBackPressed();
        }

        try {
            JSONObject jsonObject = new JSONObject(decryptedMessage);
            String userName = jsonObject.getString("PHONE_NUMBER");
            String emailAddress = jsonObject.getString("EMAIL_ADDRESS");
            String restaurantName = jsonObject.getString("RESTAURANT_NAME");
            String restaurantOwnerName = jsonObject.getString("RESTAURANT_OWNER_NAME");
            String password = jsonObject.getString("PASSWORD");

            Intent intent = new Intent(ScannerActivity.this, AddNewUserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("PHONE_NUMBER", userName);
            intent.putExtra("EMAIL_ADDRESS", emailAddress);
            intent.putExtra("RESTAURANT_NAME", restaurantName);
            intent.putExtra("RESTAURANT_OWNER_NAME", restaurantOwnerName);
            intent.putExtra("PASSWORD", password);
            intent.putExtra("IS_COMING_FROM_SCANNER", true);

            startActivity(intent);

            //Toast.makeText(this, "username : "+userName+"\npassword : "+password, Toast.LENGTH_SHORT).show();
            //showAToast("username : "+userName+"\npassword : "+password+"\nemail : "+emailAddress+"\nrestaurantName : "+restaurantName+"\nrestaurantOwnerName : "+restaurantOwnerName);
        } catch (JSONException e) {
            e.printStackTrace();

            generateFailureBeep();
            Log.e(TAG, "Invalid QR");
            //Toast.makeText(this, "Invalid QR. Please Try again", Toast.LENGTH_SHORT).show();
            showAToast("Invalid QR, please try again");
            onBackPressed();
        }

        generateSuccessBeep();
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        // This is because the dialog was cancelled when we recreated the activity.
        if (permissions.length == 0 || grantResults.length == 0)
            return;

        switch (requestCode)
        {
            case PERMISSION_REQUEST_CAMERA:
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    startCamera();
                }
                else
                {
                    finish();
                }
            }
            break;
        }
    }

    public void startCamera() {
        scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        scannerView.startCamera();          // Start camera on resume
    }

    public void stopCamera() {
        scannerView.stopCamera();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        startCamera();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        stopCamera();
    }

    @SuppressLint("ShowToast")
    public void showAToast (String st){
        try{
            toast.getView().isShown();
            toast.setText(st);
        } catch (Exception e) {
            toast = Toast.makeText(ScannerActivity.this, st, Toast.LENGTH_SHORT);
        }
        toast.show();  //finally display it
    }

    private boolean haveCameraPermission()
    {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void generateSuccessBeep() {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE,250);
        vibrate.vibrate(250);
    }

    public void generateFailureBeep() {
        long[] vibrationPattern = new long[]{0, 300, 150, 300};

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        toneGen1.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,750);
        vibrate.vibrate(vibrationPattern, -1);
    }

}
