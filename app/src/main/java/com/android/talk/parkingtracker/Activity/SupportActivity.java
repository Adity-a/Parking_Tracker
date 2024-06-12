package com.android.talk.parkingtracker.Activity;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.android.talk.parkingtracker.R;

public class SupportActivity extends AppCompatActivity {
    Button btnCall;
    Button btnSMS;
    Button btnEmail;
    ImageView imgCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

        btnSMS = (Button) findViewById(R.id.btnSMS);
        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });

        btnEmail = (Button) findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    //No permission required
    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:7668157900"));
        startActivity(intent);
    }

    //No permission required
    private void sendSMS() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:7668157900"));
        sendIntent.putExtra("sms_body", "Hey, we need help");
        startActivity(sendIntent);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@parkme.com"});

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help us");

        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please help us!!");

        emailIntent.setType("message/rfc822");

        startActivity(Intent.createChooser(emailIntent, "support@parkme.ca"));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_right);
    }
}