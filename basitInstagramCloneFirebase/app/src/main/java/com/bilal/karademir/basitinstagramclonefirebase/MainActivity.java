package com.bilal.karademir.basitinstagramclonefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button buttonSignUp,buttonSignIn;
    EditText editTextEmail,editTextPass;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        tanimla();
        signIn();
        signUp();
        //Kullanıcıyı hatırlaması için. çıkana kadar uygulama açık kalsın
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){

            startActivity(new Intent(getApplicationContext(),feedActivity.class));
        }




    }

    public void tanimla(){

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPass);
    }

    //Kullanıcı Giriş İşlemi

    public void signIn(){

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(),editTextPass.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    startActivity(new Intent(getApplicationContext(),feedActivity.class));
                                    finish();

                                }

                            }
                        }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });
    }
//Kullanıcı Kaydetme İşlemi
    public void signUp(){

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(),editTextPass.getText().toString())
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Kullanıcı Oluşturuldu", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),feedActivity.class));
                            finish();

                        }





                    }
                }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
