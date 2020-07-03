package com.project.vnpr.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.project.vnpr.R;

public class ResendPassword extends AppCompatActivity {

    EditText RegisteredEmail;
    Button ResetPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend_password);

        RegisteredEmail = (EditText) findViewById(R.id.etResendEmail);
        ResetPassword = (Button) findViewById(R.id.btnResend);
        mAuth= FirebaseAuth.getInstance();

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String Useremail = RegisteredEmail.getText().toString().trim();

               if(Useremail.isEmpty()){
                   Toast.makeText(ResendPassword.this,"Enter registered Email",Toast.LENGTH_SHORT).show();
               }
               else{
                   mAuth.sendPasswordResetEmail(Useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(ResendPassword.this,"Password Reset Link Send",Toast.LENGTH_SHORT).show();
                               finish();
                               startActivity(new Intent(ResendPassword.this, login.class));
                           }
                           else{
                               Toast.makeText(ResendPassword.this,"Check Registered Email",Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
            }
        });
    }
}
