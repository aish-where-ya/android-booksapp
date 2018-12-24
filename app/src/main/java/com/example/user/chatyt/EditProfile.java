package com.example.user.chatyt;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button update ;
    EditText NewContactNo,NewName ;
    DatabaseReference databaseReference, databaseSeller, databaseUser,mDatabaseRef;
    FirebaseUser user;
    String uid;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
            uid= user.getUid();

        databaseSeller = FirebaseDatabase.getInstance().getReference("SellerBookDetails").child(uid);
        databaseUser = FirebaseDatabase.getInstance().getReference("UserDetails").child(uid);
        databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");
        databaseReference = FirebaseDatabase.getInstance().getReference("AllBooks");

        update = (Button) findViewById(R.id.update);
        NewContactNo = (EditText) findViewById(R.id.NewContactNo);
        NewName = (EditText) findViewById(R.id.NewName);

        update.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          updateDetails();
                                      }
                                  }

        );
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void updateDetails()
    {
        final long contactNo = Long.parseLong(NewContactNo.getText().toString().trim());
        final String newName = NewName.getText().toString().trim();
        //Toast.makeText(EditProfile.this,"Update Start",Toast.LENGTH_SHORT).show();
        final String id = firebaseAuth.getCurrentUser().getUid();
        //String id = databaseReference.push().getKey();
        //Toast.makeText(EditProfile.this , id, Toast.LENGTH_SHORT).show();
        //UserInformation ui = new UserInformation();
        Map<String ,Object> updatemap = new HashMap<String, Object>();
        Map<String ,Object> updatemap2 = new HashMap<String, Object>();

        updatemap.put("contact_no", contactNo);
        updatemap2.put("name", newName);
        databaseUser.updateChildren(updatemap,null);
        databaseUser.updateChildren(updatemap2,null);

        //databaseReference.child(id).setValue(ui);
        Toast.makeText(EditProfile.this,"Update Successful",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(EditProfile.this,MainActivity.class);
            startActivity(toy);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(EditProfile.this,MainActivity.class);
            startActivity(toy);

        } else if (id == R.id.sell_books) {
            Toast.makeText(getApplicationContext(),"Sell Books",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(EditProfile.this,SellBooks.class);
            startActivity(toy);


        } else if (id == R.id.your_products) {
            Toast.makeText(getApplicationContext(),"Your Products",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(EditProfile.this,YourProducts.class);
            startActivity(toy);


        }
        else if(id== R.id.edit_profile)
        {
            Toast.makeText(getApplicationContext(),"Edit Profile",Toast.LENGTH_LONG).show();
        }
        else if( id == R.id.delete_profile)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete your account?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.setTitle("Warning");
            alert.show();

        }
        else if (id == R.id.logout) {
            Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent toy = new Intent(EditProfile.this,FirstPage.class);
            startActivity(toy);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void deleteUser()
    {
        databaseSeller.removeValue();
        final String email = user.getEmail();
        databaseUser.removeValue();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String id;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SellerInfo si = postSnapshot.getValue(SellerInfo.class);

                    String email2 = si.getEmailId();

                    if(email.equals(email2))
                    {
                        id = si.getSellerId();
                        mDatabaseRef.child(id).removeValue();
                        Toast.makeText(getApplicationContext(),"User Deleted",Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseAuth.getInstance().signOut();
        user.delete();
        Toast.makeText(getApplicationContext(),"User Deleted",Toast.LENGTH_LONG).show();
        finish();
    }
}
