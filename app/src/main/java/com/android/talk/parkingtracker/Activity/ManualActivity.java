package com.android.talk.parkingtracker.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;

import com.android.talk.parkingtracker.R;

public class ManualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WebView view = new WebView(this);
//        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl("file:///android_asset/manual.html");
        setContentView(view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_left);
    }
}
