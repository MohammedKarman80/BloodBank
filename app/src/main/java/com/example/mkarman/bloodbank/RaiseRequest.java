package com.example.mkarman.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RaiseRequest extends AppCompatActivity {

    private EditText mname,mzone,mphoneno;
    private Spinner mblodtype,mcity;
    private Button save;
    private DatabaseReference mdatabase;
    private ProgressDialog mprogress;
    private DatabaseReference mDatabase;
    private DatabaseReference mydatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_request);

        mname=(EditText)findViewById(R.id.edhospitalname);
        mzone=(EditText)findViewById(R.id.edzone);
        mphoneno=(EditText)findViewById(R.id.edphone);
        mcity=(Spinner)findViewById(R.id.statespinner);
        mblodtype=(Spinner)findViewById(R.id.spinner);
        save=(Button)findViewById(R.id.btn_save);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Requests");
        mDatabase.keepSynced(true);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()== null){
                    Intent login = new Intent(RaiseRequest.this,LogInAcitivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        // Blood Type Spinner

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

        mprogress=new ProgressDialog(this);
        String userID = mAuth.getCurrentUser().getUid();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling RaiseRequest Function
                raiseRequest();
            }
        });

    }
    //RaiseRequest Function
    public void raiseRequest(){

        mprogress.setMessage("Registring .... ");
        mprogress.show();

        String name_val=mname.getText().toString().trim();
        String zone_val=mzone.getText().toString().trim();
        String phone_val=mphoneno.getText().toString().trim();
        String date= DateFormat.getDateInstance().format(new Date());


        String blod_val=mblodtype.getSelectedItem().toString().trim();
        String city_val = mcity.getSelectedItem().toString().trim();

        String wrongval= "Select Blood Type";
        String wrongval2= "Select State";

        if(!TextUtils.isEmpty(name_val) &&
                !TextUtils.isEmpty(city_val) && !TextUtils.isEmpty(zone_val) &&
                !TextUtils.isEmpty(phone_val) && !TextUtils.isEmpty(blod_val)){

            if(wrongval.equals(blod_val)){
                Toast.makeText(RaiseRequest.this,"Select Blood ..",Toast.LENGTH_LONG).show();
                blod_val ="";
                mprogress.dismiss();
            }
            if(wrongval2.equals(city_val)){
                Toast.makeText(RaiseRequest.this,"Select State ..",Toast.LENGTH_LONG).show();
                mprogress.dismiss();
            }

            else
            {

                String userID = mAuth.getCurrentUser().getUid();
                DatabaseReference newmember = mdatabase.child(userID);

                newmember.child("name").setValue(name_val);
                newmember.child("userID").setValue(userID);
                newmember.child("blod_type").setValue(blod_val);
                newmember.child("city").setValue(city_val);
                newmember.child("zone").setValue(zone_val);
                newmember.child("phoneno").setValue(phone_val);
                newmember.child("date").setValue(date);

                mprogress.dismiss();
                Intent mainIntent = new Intent(RaiseRequest.this,MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }


        }else {
            mprogress.dismiss();
            Toast.makeText(RaiseRequest.this,"Fill all the Fileds",Toast.LENGTH_LONG).show();
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
            Intent mainintent = new Intent(RaiseRequest.this,MainActivity.class);
            mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainintent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        mAuth.signOut();
    }


}
