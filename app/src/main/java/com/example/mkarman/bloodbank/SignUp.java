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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    private Button btnsignup;
    private EditText emailfiled,passfiled,repassfield;
    private FirebaseAuth mAuth;
    private ProgressDialog mprogress;
   private DatabaseReference mDatarefer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mprogress = new ProgressDialog(this);
        mDatarefer = FirebaseDatabase.getInstance().getReference().child("User");

        btnsignup = (Button)findViewById(R.id.btnsinup);
        emailfiled = (EditText)findViewById(R.id.etxtemali);
        passfiled = (EditText)findViewById(R.id.etxtpass);
        repassfield = (EditText)findViewById(R.id.edtxtrepass);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSiup();
            }
        });


    }

    private void startSiup() {
        final String email = emailfiled.getText().toString().trim();
        String password = passfiled.getText().toString().trim();
        String repassword = repassfield.getText().toString().trim();

        final Query mQuery = mDatarefer.orderByChild("email").equalTo(email);



           //To check all the filed is not empty
        if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)&& !TextUtils.isEmpty(repassword)){
            if(password.length() <= 5 || repassword.length() <= 5 ) {
                Toast.makeText(SignUp.this, "Password Must Be More Than 6 Characters  ..", Toast.LENGTH_LONG).show();
            }//to check Passwords matching
            else if(!password.equals (repassword)){
                Toast.makeText(SignUp.this, "Password Not Matching   ..", Toast.LENGTH_LONG).show();
            }
            else {
            mprogress.setMessage("Signing up ...");
            mprogress.show();

           mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {

                   if(task.isSuccessful()){

                      String user_id = mAuth.getCurrentUser().getUid();
                      DatabaseReference current_user_db = mDatarefer.child(user_id);
                      current_user_db.child("email").setValue(email);
                      current_user_db.child("image").setValue("default");

                      mprogress.dismiss();
                       Intent mainIntent = new Intent(SignUp.this,MainActivity.class);
                       mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(mainIntent);

                   }
               }
           });}

        }else {
            Toast.makeText(SignUp.this,"Please Fill All The Fields ..",Toast.LENGTH_LONG).show();
        }

    }
    //Menu Code
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
            Intent mainintent = new Intent(SignUp.this,MainActivity.class);
            mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainintent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        mAuth.signOut();
    }

}
