package co.mandeep_singh.vitcomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.mandeep_singh.vitcomplaint.Auth.Auth;

public class StudentLogin extends AppCompatActivity {
    Button button;
    EditText email, password;
    TextView newApp, resetPassword;
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
                if (button.getText().toString().equals("Sign up")){
                    if(!email.getText().toString().equals("") && !password.getText().toString().equals("") && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
                        auth.SignUp(email.getText().toString(),password.getText().toString(),false, StudentLogin.this);
                        if(auth.errorMessage.equals("") && auth.id !=null){
                            Toast.makeText(getApplicationContext(),"Please check your email",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),auth.errorMessage,Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    if(!email.getText().toString().equals("") && !password.getText().toString().equals("")  && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
                        auth.SignIn(email.getText().toString(),password.getText().toString(),false, StudentLogin.this);
                        if(auth.errorMessage.equals("") && auth.id !=null){
                            Intent i = new Intent(StudentLogin.this, HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),auth.errorMessage,Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }


}