package co.mandeep_singh.vitcomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WardenLogin extends AppCompatActivity {
    Button button;
    EditText email, password;
    TextView newApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_warden_login);
        button = findViewById(R.id.button);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        newApp = findViewById(R.id.textView2);
        addButtonListener();
        addNewAppListener();
    }

    private void addBackLogin() {
        newApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("Sign in");
                newApp.setText("New to our app?");
                addNewAppListener();
            }
        });
    }

    private void addNewAppListener() {
        newApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("Sign up");
                newApp.setText("Back to login");
                addBackLogin();
            }
        });
    }

    private void addButtonListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), email.getText() + " " + password.getText(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
            }
        });
    }


}