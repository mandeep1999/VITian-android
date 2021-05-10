package co.mandeep_singh.vitcomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import co.mandeep_singh.vitcomplaint.Auth.Auth;

public class StudentLogin extends AppCompatActivity {
    Button button;
    EditText email, password;
    TextView newApp, resetPassword;
    ProgressBar progressBar;
    Auth auth = new Auth();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_login);
        button = findViewById(R.id.button_student);
        email = findViewById(R.id.Email_student);
        password = findViewById(R.id.Password_student);
        newApp = findViewById(R.id.new_student);
        resetPassword = findViewById(R.id.resetPassword_Student);
        progressBar = findViewById(R.id.progressBar_student_login);
        addButtonListener();
        addNewAppListener();
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogBox();
            }
        });
    }
    private void createDialogBox(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentLogin.this);
        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setTitle("Enter your email to reset password").setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.resetPassword(input.getText().toString());
                Toast.makeText(getApplicationContext(), "Please check your email", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

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
                boolean pass = StudentEmail(email);
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                if(pass && !(passwordText.isEmpty() || passwordText.equals(""))){
                    if (button.getText().toString().equals("Sign up")) {
                        if (!emailText.equals("") && !passwordText.equals("")) {
                            progressBar.setVisibility(View.VISIBLE);
                            auth.SignUp(emailText, passwordText, false, StudentLogin.this, progressBar);
                        }
                    } else {
                        if (!emailText.equals("") && !passwordText.equals("")) {
                            progressBar.setVisibility(View.VISIBLE);
                            auth.SignIn(emailText, passwordText, false, StudentLogin.this, progressBar);
                        }
                    }
                }
                else if(pass && (passwordText.isEmpty() || passwordText.equals("") )){
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if(!pass){
                    Toast.makeText(getApplicationContext(), "There is a problem with your email", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "I smell something weird", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean StudentEmail(EditText email){
        String emailText = email.getText().toString().trim();
        boolean first = Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
        boolean second = emailText.endsWith("@vitstudent.ac.in") || emailText.equals("mandy.sgh.99@gmail.com");
        return  first && second;
    }

}