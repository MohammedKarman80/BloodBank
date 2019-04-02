package com.example.mkarman.bloodbank;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditePassword extends AppCompatActivity {
     private Button btnCancel;
     private Button btnSave;
     public EditText txtEmail ;
     private EditText txtOlspass;
     private EditText txtNewpass;
     private EditText txtRenewpass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_password);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnSave = (Button)findViewById(R.id.btnSave);
        txtEmail = (EditText)findViewById(R.id.txtemail);
        txtOlspass = (EditText)findViewById(R.id.txtolspass);
        txtNewpass = (EditText)findViewById(R.id.txtnewpass);
        txtRenewpass = (EditText)findViewById(R.id.txtrenewpass);
        final String email = user.getEmail();
        txtEmail.setText(user.getEmail().toString());
        txtEmail.setEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent login = new Intent(EditePassword.this, LogInAcitivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);

                }
            }
        };



        btnSave.setOnClickListener(new View.OnClickListener() {

            //String oldPss = txtOlspass.toString().trim();

            @Override
            public void onClick(View v) {
               String oldPss = txtOlspass.getText().toString();
                if(!TextUtils.isEmpty(txtEmail.getText().toString())&&!TextUtils.isEmpty(txtNewpass.getText().toString()) && !TextUtils.isEmpty(txtOlspass.getText().toString())
                        && !TextUtils.isEmpty(txtRenewpass.getText().toString()) )
                {
                   if(txtNewpass.getText().toString().equals(txtRenewpass.getText().toString())) {
                        AuthCredential credential = EmailAuthProvider.getCredential(email,oldPss);

                  // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    user.updatePassword(txtNewpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(EditePassword.this, "Password Updated..", Toast.LENGTH_LONG).show();

                                                Intent mainIntent = new Intent(EditePassword.this,MainActivity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(mainIntent);

                                            } else {
                                                Toast.makeText(EditePassword.this, "Error ..", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(EditePassword.this, "Email Or Passord Not Correct ..", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }else {
                       Toast.makeText(EditePassword.this,"password not matching ..",Toast.LENGTH_LONG).show();
                   }
                }else {


                    Toast.makeText(EditePassword.this,"Please Fill all the fields ..",Toast.LENGTH_LONG).show();
                }

            }
        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.back_menu, menu);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    menu.findItem(R.id.item_logout).setVisible(true);

                } else {
                    menu.findItem(R.id.item_logout).setVisible(false);
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.item_logout) {
            LogOut();
        }
        if (item.getItemId() == R.id.item_back) {
            Intent myaccountintent = new Intent(EditePassword.this, MyAccountActivity.class);
            myaccountintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myaccountintent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        mAuth.signOut();
    }



}
