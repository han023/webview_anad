package com.example.second;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util util = new Util();
        Random random = new Random();

        if(util.getLocalData(this, "userid").equals("")){
            util.saveLocalData(this,"userid", Integer.toString(random.nextInt(999999999)) );
        }

        geopermission.requestPermissions(this);

        if(geopermission.hasGeoPermissions(this)) {
            if (!isServiceRunning(this, MyForegroundService.class)) {
                Intent serviceIntent = new Intent(this, MyForegroundService.class);
                ContextCompat.startForegroundService(this, serviceIntent);
            }
        }

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.loadUrl("https://sehyliyr.cleverapps.io/e1");


    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private void requestBatteryOptimizationPermission() {
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            // Get the list of running services
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!geopermission.hasGeoPermissions(this)) {
            if (!geopermission.shouldShowRequestPermissionRationale(this)) {
                geopermission.launchPermissionSettings(this);
            }
            finish();
        }

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
            requestBatteryOptimizationPermission();
        }

        if (!isServiceRunning(this, MyForegroundService.class)) {
            Intent serviceIntent = new Intent(this, MyForegroundService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }


    }

}
