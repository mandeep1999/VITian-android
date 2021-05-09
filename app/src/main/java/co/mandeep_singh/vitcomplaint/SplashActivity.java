package co.mandeep_singh.vitcomplaint;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.mandeep_singh.vitcomplaint.Auth.Auth;

public class SplashActivity extends AppCompatActivity {
    Auth auth = new Auth();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        auth.CheckWardenOrStudent(SplashActivity.this);
    }
}