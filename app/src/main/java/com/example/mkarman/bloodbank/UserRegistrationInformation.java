package com.example.mkarman.bloodbank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRegistrationInformation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private EditText Name,Age,Zone,PhoneNo;
    private Button btnUpdate,btnCancel ;
    private  String userID;
    private DatabaseReference add;
    private Spinner mblodtype,City;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_information);

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("BloodBank");
        mdatabase.keepSynced(true);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent login = new Intent(UserRegistrationInformation.this, LogInAcitivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);

                }
            }
        };


        userID = mAuth.getCurrentUser().getUid();

        Name = (EditText)findViewById(R.id.txtUserName);
       //City = (EditText)findViewById(R.id.txtUserCity);
        Zone = (EditText)findViewById(R.id.txtUserZone);
        Age = (EditText)findViewById(R.id.txtUserAge);
        PhoneNo = (EditText)findViewById(R.id.txtUserPhone);
       // BloodType = (EditText) findViewById(R.id.txtUserBloodTYpe);
        btnUpdate  =(Button)findViewById(R.id.btnUpdate);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        mblodtype = (Spinner) findViewById(R.id.txtUserBloodTYpe);
        City=(Spinner)findViewById(R.id.txtUserCity);
      //blod type Spinner code
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

        // start Stat Spinner

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
        City.setAdapter(spinnerArrayAdapter2);



        //end of spinner code


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = mAuth.getCurrentUser().getUid().toString();

                mdatabase.child(userID).removeValue();
                Toast.makeText(UserRegistrationInformation.this,"Your Information Succfully Deleted",Toast.LENGTH_LONG).show();
                Intent maininteny = new Intent(UserRegistrationInformation.this,MainActivity.class);
                maininteny.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(maininteny);


            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String age = Age.getText().toString().trim();
                String city =City.getSelectedItem().toString().trim();
                String zone = Zone.getText().toString().trim();
                String phoneno = PhoneNo.getText().toString().trim();
                String name = Name.getText().toString().trim();
                //String blod_type = BloodType.getText().toString().trim();
                String blod_type=mblodtype.getSelectedItem().toString().trim();
                if(blod_type.equals("Select Blood Type")){
                    Toast.makeText(UserRegistrationInformation.this, "Please Select Blood Type", Toast.LENGTH_LONG).show();
                }
                if(city.equals("Select State")){
                    Toast.makeText(UserRegistrationInformation.this, "Please Select State", Toast.LENGTH_LONG).show();
                }
                else {
                    //Calling Update Function
                    update(userID, age, blod_type, name, phoneno, zone, city);

                    Toast.makeText(UserRegistrationInformation.this, "Information Uploaded", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent();
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }
        });

        final String userID = mAuth.getCurrentUser().getUid();
       final Query mQuery = mdatabase.orderByChild("userID").equalTo(userID);

       mQuery.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to Firebase
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                    Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                    Age.setText(snapshot.child("age").getValue().toString());
                    Name.setText(snapshot.child("name").getValue().toString());
                    //City.setText(snapshot.child("city").getValue().toString());
                    Zone.setText(snapshot.child("zone").getValue().toString());
                    PhoneNo.setText(snapshot.child("phoneno").getValue().toString());
                  //  mblodtype.setText(snapshot.child("blod_type").getValue().toString());


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
  //Update Function
  private  boolean update(final String id,final String age, final String blod_type, final String name, final String phoneno , final String zone , final String city){

        add =  FirebaseDatabase.getInstance().getReference().child("BloodBank").child(userID);
              Members members = new Members(id,age,blod_type,name,phoneno,zone,city);
              add.setValue(members);
      return true;
   }
    //Menu Code
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
            Intent myacountintent = new Intent(UserRegistrationInformation.this, MyAccountActivity.class);
            myacountintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myacountintent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        mAuth.signOut();
    }

}



