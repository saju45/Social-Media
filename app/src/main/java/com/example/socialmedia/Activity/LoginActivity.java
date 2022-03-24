package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.socialmedia.R;
import com.example.socialmedia.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        dialog=new ProgressDialog(this);
        dialog.setTitle("Login account");
        dialog.setMessage("Please wait...");
        auth=FirebaseAuth.getInstance();
        binding.registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.email.getText().toString();
                String password=binding.password.getText().toString();

                 if (email.isEmpty())
                {
                    binding.email.setError("enter your email");
                    binding.email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    binding.email.setError("please enter valid email");
                    binding.email.requestFocus();
                }else if (password.isEmpty())
                {
                    binding.password.setError("please enter password");
                    binding.password.requestFocus();
                }else if (password.length()<6)
                {
                    binding.password.setError("enter password should be length 6");
                    binding.password.requestFocus();
                }else {

                     dialog.show();

                     auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             dialog.dismiss();
                             if (task.isSuccessful())
                             {
                                 Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                 finish();
                             }
                         }
                     });
                 }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }
}