package com.rku.tutorial_13;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 11;
    private static final int MAKE_PHONE_PERMISSION_REQUEST_CODE = 1;
    private Button sendMessage;
    private Button dial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dial = (Button) findViewById(R.id.dial);
        sendMessage = (Button) findViewById(R.id.send_message);
        final EditText phone = (EditText) findViewById(R.id.phone_no);
        final EditText message = (EditText) findViewById(R.id.message);
        sendMessage.setEnabled(false);

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phone.getText().toString();

                if (!TextUtils.isEmpty(phoneNumber)) {
                    if (checkPermission(Manifest.permission.CALL_PHONE)) {
                        String dial = "tel:" + phoneNumber;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            dial.setEnabled(true);

        } else {
            dial.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_PHONE_PERMISSION_REQUEST_CODE);

        }


        if (checkPermission(Manifest.permission.SEND_SMS)) {
            sendMessage.setEnabled(true);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);

        }

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message.getText().toString();
                String phoneNumber = phone.getText().toString();

                if (!TextUtils.isEmpty(msg) && !TextUtils.isEmpty(phoneNumber)) {

                    if (checkPermission(Manifest.permission.SEND_SMS)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Enter a message and a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean checkPermission(String permission){
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case SEND_SMS_PERMISSION_REQUEST_CODE :
                      if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                          sendMessage.setEnabled(true);
                      }
                    break;

                case MAKE_PHONE_PERMISSION_REQUEST_CODE :
                    if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        dial.setEnabled(true);
                        Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                    }
                    return;
            }
    }

}