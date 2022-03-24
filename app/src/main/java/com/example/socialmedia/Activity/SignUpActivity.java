package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Create account");
        progressDialog.setMessage("we are creating your account");

        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("user");

        binding.signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=binding.name.getText().toString();
                String profession=binding.profession.getText().toString();
                String email=binding.email.getText().toString();
                String password=binding.password.getText().toString();

                if(name.isEmpty())
                {
                    binding.name.setError("please enter your name");
                    binding.name.requestFocus();
                }else if(profession.isEmpty())
                {
                    binding.profession.setError("please enter your profession");
                    binding.profession.requestFocus();
                }else if (email.isEmpty())
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
                    progressDialog.show();

                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful())
                            {
                                String userId=auth.getUid();
                                Users users=new Users(name,profession,email,password);

                                databaseReference.child(userId).setValue(users);

                                Toast.makeText(getApplicationContext(), "account created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });

    }
}