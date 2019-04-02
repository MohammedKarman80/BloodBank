package com.example.mkarman.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewRequests extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference refrence;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Spinner CitySpinner;
    private String  Selected_City;
    private Button btn_search;
    private RecyclerView memberlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        refrence = FirebaseDatabase.getInstance().getReference().child("Requests");
        mDatabase.keepSynced(true);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent login = new Intent(ViewRequests.this, LogInAcitivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);

                }
            }
        };

        //Recyclerview

        memberlist = (RecyclerView) findViewById(R.id.member_list);
        // memberlist.setHasFixedSize(true);
        memberlist.setLayoutManager(new LinearLayoutManager(this));


        // State Spinner
        CitySpinner = (Spinner) findViewById(R.id.city_spinner);

        // Initializing a String Array
        String[] plants = new String[]{
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

        List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, plantsList) {
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
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        CitySpinner.setAdapter(spinnerArrayAdapter);


        CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selected_City = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //end of spinner code

        Selected_City = CitySpinner.getSelectedItem().toString().trim();
        btn_search= (Button)findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                onsearch(Selected_City);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        FirebaseRecyclerAdapter<Request, ViewRequests.MemberViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, ViewRequests.MemberViewHolder>(
                Request.class,
                R.layout.rowiew,
                ViewRequests.MemberViewHolder.class,
                refrence

        ) {
            @Override
            protected void populateViewHolder(MemberViewHolder viewHolder, Request model, int position) {
                viewHolder.setDate(model.getDate());
                viewHolder.setBlod_type(model.getBlod_type());
                viewHolder.setName(model.getName());
                viewHolder.setPhoneno(model.getPhoneno());
                viewHolder.setZone(model.getZone());
            }


        };
        memberlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public MemberViewHolder(View itemView) {
            super(itemView);


            mview = itemView;
        }

        public void setDate(String date) {
            TextView Date = (TextView) mview.findViewById(R.id.txtage);
            Date.setText("Date : " + date);
            Date.setTextSize(16);
        }

        public void setBlod_type(String blod_type) {
            TextView Blod_type = (TextView) mview.findViewById(R.id.txtblodtype);
            Blod_type.setText("Blod Type : " + blod_type);
            Blod_type.setTextSize(16);
        }

        public void setName(String name) {
            TextView Name = (TextView) mview.findViewById(R.id.txtname);
            Name.setText("Name : " + name);
            Name.setTextSize(16);
        }

        public void setPhoneno(String phoneno) {
            TextView Phoneno = (TextView) mview.findViewById(R.id.txtphoneno);
            Phoneno.setText("Phone Number : " + phoneno);
            Phoneno.setTextSize(16);
        }

        public void setZone(String zone) {
            TextView Zone = (TextView) mview.findViewById(R.id.txtcity);
            Zone.setText("City : " + zone);
            Zone.setTextSize(16);
        }
    }


    public void onsearch(final String SelectedCity) {
        // Toast.makeText(Search.this,SelectedCity,Toast.LENGTH_LONG).show();
        String wrongval= "Select State";
        if(wrongval.equals(SelectedCity)){
            Toast.makeText(ViewRequests.this,"Please Select State First ..",Toast.LENGTH_LONG).show();
        }
        else {

            final Query mQuery = refrence.orderByChild("city").equalTo(SelectedCity);

            mQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        FirebaseRecyclerAdapter<Request, ViewRequests.MemberViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, ViewRequests.MemberViewHolder>(
                               Request.class,
                                R.layout.rowiew,
                                ViewRequests.MemberViewHolder.class,
                                mQuery
                        ) {
                            @Override
                            protected void populateViewHolder(ViewRequests.MemberViewHolder viewHolder,Request model, int position) {

                                viewHolder.setDate(model.getDate());
                                viewHolder.setBlod_type(model.getBlod_type());
                                viewHolder.setName(model.getName());
                                viewHolder.setPhoneno(model.getPhoneno());
                                viewHolder.setZone(model.getZone());
                            }
                        };
                        memberlist.setAdapter(firebaseRecyclerAdapter);
                    } else {

                        Toast.makeText(ViewRequests.this, "No Requests Are Available Form " + SelectedCity, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


    }

    //Menu Code

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
            Intent mainintent = new Intent(ViewRequests.this,MainActivity.class);
            mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainintent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        mAuth.signOut();
    }

}
