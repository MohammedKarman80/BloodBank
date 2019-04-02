package com.example.mkarman.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register extends AppCompatActivity {

    private EditText mname;
    //private EditText mcity;
    private EditText mzone;
    private EditText mage;
    private EditText mphoneno;
    private Spinner mblodtype,mcity;

    private Button submit;
    private DatabaseReference mdatabase;
    private ProgressDialog mprogress;
    private DatabaseReference mDatabase;
    private DatabaseReference mydatabase;
   private FirebaseAuth mAuth;
   private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mydatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()== null){
                    Intent login = new Intent(Register.this,LogInAcitivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        // Blood Type Spinner
        mblodtype = (Spinner) findViewById(R.id.spinner);

        // Initializing a String Array
        String[] plants = new String[]{
                "Select Blood Type",
                "O+",
                "O-",
                "A+",
                "A-",
                "B+",
                "B-",
                "AB+",
                "AB-"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mblodtype.setAdapter(spinnerArrayAdapter);

        //end of blood type spinner code

        // State Spinner
        mcity = (Spinner) findViewById(R.id.edcity);

        // Initializing a String Array
        String[] plants2 = new String[]{
                "Select State",
                "Andra Pradesh",
                "Arunachal Pradesh",
                "Assam",
                "Bihar",
                "Chhattisgarh",
                "Goa",
                "Gujarat",
                "Haryana",
                "Himachal Pradesh",
                "Jammu and Kashmir",
                "Karnataka",
                "Kerala","Maharashtra",
                "Punjab",
                "Rajasthan","Sikkim","Tamil Nadu","Uttar Pradesh","Delhi"
        };

        List<String> plantsList2 = new ArrayList<>(Arrays.asList(plants2));

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, plantsList2) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.RED);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mcity.setAdapter(spinnerArrayAdapter2);



        //end of spinner code


       mname=(EditText)findViewById(R.id.edname);
       // mcity=(EditText)findViewById(R.id.edcity);
        mzone=(EditText)findViewById(R.id.edzone);
        mage=(EditText)findViewById(R.id.edage);
        mphoneno=(EditText)findViewById(R.id.edphone);
        submit=(Button)findViewById(R.id.btn_register);
        mprogress=new ProgressDialog(this);
        String userID = mAuth.getCurrentUser().getUid();

        mdatabase= FirebaseDatabase.getInstance().getReference().child("BloodBank");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startreqister();
            }
        });



    }

    private void startreqister() {



        mprogress.setMessage("Registring .... ");
        mprogress.show();

     String name_val=mname.getText().toString().trim();
        String age_val=mage.getText().toString().trim();
        //String city_val=mcity.getText().toString().trim();
        String zone_val=mzone.getText().toString().trim();
        String phone_val=mphoneno.getText().toString().trim();
        //String phone_val = PhoneNumberUtils.formatNumber(mphoneno.getText().toString().trim());

        String blod_val=mblodtype.getSelectedItem().toString().trim();
        String city_val = mcity.getSelectedItem().toString().trim();

        String wrongval= "Select Blood Type";
        String wrongval2= "Select State";

         //check all the Field nor empty code
        if(!TextUtils.isEmpty(name_val) && !TextUtils.isEmpty(age_val) &&
        !TextUtils.isEmpty(city_val) && !TextUtils.isEmpty(zone_val) &&
        !TextUtils.isEmpty(phone_val) && !TextUtils.isEmpty(blod_val)){
            if(age_val.length() >= 4){
                Toast.makeText(Register.this,"incorect age lenght ..",Toast.LENGTH_LONG).show();
                age_val ="";
                mprogress.dismiss();

            }
           /* if(phone_val.length() >= 8 ) {
               Toast.makeText(Register.this,"Your Phone Number Should Be 10 Digits  ..",Toast.LENGTH_LONG).show();
                phone_val = "";
                mprogress.dismiss();
            }*/
           if(wrongval.equals(blod_val)){
               Toast.makeText(Register.this,"Select Blood ..",Toast.LENGTH_LONG).show();
               blod_val ="";
               mprogress.dismiss();
           }
           if(wrongval2.equals(city_val)){
               Toast.makeText(Register.this,"Select State ..",Toast.LENGTH_LONG).show();
               mprogress.dismiss();
           }

            else
             {

                 String userID = mAuth.getCurrentUser().getUid();
                DatabaseReference newmember = mdatabase.child(userID);

                newmember.child("name").setValue(name_val);
                newmember.child("userID").setValue(userID);
                newmember.child("blod_type").setValue(blod_val);
                newmember.child("age").setValue(age_val);
                newmember.child("city").setValue(city_val);
                newmember.child("zone").setValue(zone_val);
                newmember.child("phoneno").setValue(phone_val);

                mprogress.dismiss();
                Intent mainIntent = new Intent(Register.this,MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }


        }else {
            mprogress.dismiss();
            Toast.makeText(Register.this,"Fill all the Fileds",Toast.LENGTH_LONG).show();
        }





    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.back_menu,menu);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){
                    menu.findItem(R.id.item_logout).setVisible(true);

                }else {
                    menu.findItem(R.id.item_logout).setVisible(false);
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.item_logout){
            LogOut();
        }
        if(item.getItemId() == R.id.item_back){
            Intent mainintent = new Intent(Register.this,MainActivity.class);
            mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainintent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        mAuth.signOut();
    }


}


