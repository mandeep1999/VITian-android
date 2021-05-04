package co.mandeep_singh.vitcomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

    }

    public void card1Click(View view) {
        Intent First = new Intent(getApplicationContext(),WardenLogin.class);
        startActivity(First);
    }

    public void card2Click(View view) {
        Intent Second = new Intent(getApplicationContext(),StudentLogin.class);
         startActivity(Second);
    }
}