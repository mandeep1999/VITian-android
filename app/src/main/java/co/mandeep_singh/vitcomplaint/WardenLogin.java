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

import co.mandeep_singh.vitcomplaint.Auth.Auth;

public class WardenLogin extends AppCompatActivity {
    Button button;
    EditText email, password;
    TextView newApp;
    Auth auth = new Auth();
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
                if(!email.getText().toString().equals("") && !password.getText().toString().equals("")){
                   auth.SignUp(email.getText().toString(),password.getText().toString(),true, WardenLogin.this);
                   if(auth.errorMessage.equals("") && !Auth.id.equals("")){
                       Toast.makeText(getApplicationContext(),"Signed Up",Toast.LENGTH_LONG).show();
                   }
                   else {
                       Toast.makeText(getApplicationContext(),auth.errorMessage,Toast.LENGTH_LONG).show();
                   }
                }
//                Intent i = new Intent(getApplicationContext(), HomeActivityWarden.class);
//                startActivity(i);
            }
        });
    }


}