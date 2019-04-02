package com.example.mkarman.bloodbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button RegisterBtn,RaiseRequestBtn,ViewRequestsBtn,aboutAppBtn;
    private DatabaseReference mdatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("BloodBank");
        RegisterBtn = (Button)findViewById(R.id.bu1);
        ViewRequestsBtn=(Button)findViewById(R.id.viewrequestbtn);
        aboutAppBtn = (Button)findViewById(R.id.aboutbtn);
        RaiseRequestBtn = (Button)findViewById(R.id.bu3);

        aboutAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutintent = new Intent(MainActivity.this,AboutApp.class);
                startActivity(aboutintent);

            }
        });

        ViewRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if(firebaseAuth.getCurrentUser() == null){
                            Toast.makeText(MainActivity.this,"Please log in first ..",Toast.LENGTH_LONG).show();
                            Intent login = new Intent(MainActivity.this,LogInAcitivity.class);
                            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(login);

                        }else {
                            Intent login = new Intent(MainActivity.this,ViewRequests.class);
                            startActivity(login);
                        }
                    }
                };

                mAuth.addAuthStateListener(mAuthListener);

            }
        });

        RaiseRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        //if User Not logged in code
                        if(firebaseAuth.getCurrentUser() == null){
                            Toast.makeText(MainActivity.this,"Please log in first ..",Toast.LENGTH_LONG).show();
                            Intent login = new Intent(MainActivity.this,LogInAcitivity.class);
                            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(login);

                        }else {
                            Intent login = new Intent(MainActivity.this,RaiseRequest.class);
                            startActivity(login);
                        }
                    }
                };

                mAuth.addAuthStateListener(mAuthListener);
            }
        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthListener = new FirebaseAuth.AuthStateListener() {

                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if(firebaseAuth.getCurrentUser() == null){
                            Toast.makeText(MainActivity.this,"Please log in first ..",Toast.LENGTH_LONG).show();
                            Intent login = new Intent(MainActivity.this,LogInAcitivity.class);
                            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(login);

                        }else {

                            //final Query mQuery = mdatabase.child("userID").equalTo(userID);

                            mdatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final String userID = mAuth.getCurrentUser().getUid().toString();
                                    if(dataSnapshot.hasChild(userID)){
                                        Toast.makeText(MainActivity.this,"You Are Already Registred Go To Your Account To Update You Information",Toast.LENGTH_LONG).show();
                                    }else {
                                        Intent register = new Intent(MainActivity.this,Register.class);
                                        startActivity(register);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                };

                mAuth.addAuthStateListener(mAuthListener);
            }
        });


    }


    public void btnsearch(View view) {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(MainActivity.this,"Please log in first ..",Toast.LENGTH_LONG).show();
                    Intent login = new Intent(MainActivity.this,LogInAcitivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);

                }else {
                    Intent login = new Intent(MainActivity.this,Search.class);
                    startActivity(login);
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){
                    menu.findItem(R.id.logout).setVisible(true);
                    menu.findItem(R.id.myaccont).setVisible(true);
                    menu.findItem(R.id.login).setVisible(false);

                }else {
                    menu.findItem(R.id.logout).setVisible(false);
                    menu.findItem(R.id.myaccont).setVisible(false);
                    menu.findItem(R.id.login).setVisible(true);
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.login){
            Intent login = new Intent(MainActivity.this,LogInAcitivity.class);
            startActivity(login);
        }
        if(item.getItemId() == R.id.logout){
           LogOut();
        }
        if(item.getItemId() == R.id.exit){
           finish();
            //System.exit(0);
        }
        if (item.getItemId() == R.id.myaccont){
            Intent myaccountintent = new Intent(MainActivity.this , MyAccountActivity.class);
          startActivity(myaccountintent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        mAuth.signOut();
    }
}
