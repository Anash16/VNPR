package com.project.vnpr.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.vnpr.R;

public class login extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword;
    Button login;
    Button register;
    FirebaseAuth auth;
    TextView ForgotPassword;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInClient mGoogle= GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        startActivityForResult(mGoogle.getSignInIntent(),222);

        editTextEmail = (EditText) findViewById(R.id.text_email);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        register = (Button) findViewById(R.id.text_view_register);
        auth = FirebaseAuth.getInstance();
        ForgotPassword = (TextView) findViewById(R.id.text_view_forget_password);
        findViewById(R.id.button_sign_in).setOnClickListener(this);

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, ResendPassword.class));
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_in:
                loginUser();

                break;

            case R.id.text_view_register:

                startActivity(new Intent(this, create.class));

                break;

        }



    }



    private void loginUser() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        System.out.println("hey pochi gaya");
        if (email.isEmpty()) {
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of Password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    handler.post(()->{
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                    });
                    Intent intent=new Intent(login.this, MainActivity.class);
                    AuthResult result=task.getResult();
                    startActivity(intent);
                }
                else {
                    handler.post(()->{
                        Toast.makeText(getApplicationContext(),"Check Password and Email",Toast.LENGTH_SHORT).show();

                    });
                }
            }
        });

    }


    public void createActivity(View view){
        startActivity(new Intent(login.this,create.class));
    }

}
