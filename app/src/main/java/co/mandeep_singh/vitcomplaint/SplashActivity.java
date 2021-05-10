package co.mandeep_singh.vitcomplaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.os.Bundle;
import android.view.WindowManager;

import co.mandeep_singh.vitcomplaint.Auth.Auth;

public class SplashActivity extends AppCompatActivity {
    Auth auth = new Auth();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        auth.CheckWardenOrStudent(SplashActivity.this);
    }
}