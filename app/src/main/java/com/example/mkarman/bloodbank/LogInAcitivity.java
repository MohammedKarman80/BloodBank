package com.example.mkarman.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInAcitivity extends AppCompatActivity {
Button btnlogin,btnSinUp;
EditText userEmail,userPassword;
FirebaseAuth mAuth;
DatabaseReference mDatabase;
ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_acitivity);
        btnlogin= (Button)findViewById(R.id.btnlogin1);
        btnSinUp=(Button)findViewById(R.id.btnlogin1);
        userEmail = (EditText)findViewById(R.id.loginemail);
        userPassword = (EditText)findViewById(R.id.loginpassword);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabase.keepSynced(true);
        mProgress = new ProgressDialog(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheck();
            }
        });
    }

    private void loginCheck() {
        String Email = userEmail.getText().toString().trim();
        String Password = userPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(Email)&& !TextUtils.isEmpty(Password)) {

            mProgress.setMessage("Login Checking ..");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        mProgress.dismiss();
                        checkUserExist();

                    } else {
                        mProgress.dismiss();
                        Toast.makeText(LogInAcitivity.this, "Email Or Password Incorrect ..", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            Toast.makeText(LogInAcitivity.this, "Please Enter Your Email and Password ..", Toast.LENGTH_LONG).show();
        }



    }

    private void checkUserExist() {
       final String User_id = mAuth.getCurrentUser().getUid();
       mDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.hasChild(User_id)){
                   Intent loginIntent = new Intent(LogInAcitivity.this,MainActivity.class);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(loginIntent);

               }else {
                   Toast.makeText(LogInAcitivity.this,"Email or Password not Correct  ..",Toast.LENGTH_LONG).show();

               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

    }

    public void signup(View view){
        Intent signintn = new Intent(LogInAcitivity.this,SignUp.class);
         startActivity(signintn);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.back_menu,menu);
        menu.findItem(R.id.item_logout).setVisible(false);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.item_logout){
            LogOut();
        }
        if(item.getItemId() == R.id.item_back){
            Intent mainintent = new Intent(LogInAcitivity.this,MainActivity.class);
            mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainintent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        mAuth.signOut();
    }
}
