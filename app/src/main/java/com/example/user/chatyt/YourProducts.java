package com.example.user.chatyt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class YourProducts extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , imageAdapter.OnItemClickListener{
    private boolean isUserClickBackButton = false;

    private RecyclerView mRecylerView;
    private imageAdapter mAdapter;
    FirebaseUser user;
    String id,uid;
    private DatabaseReference mDatabaseRef;
    DatabaseReference databaseSeller, databaseUser;

    public List<SellerInfo> mUploads;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
            uid= user.getUid();

        databaseSeller = FirebaseDatabase.getInstance().getReference("SellerBookDetails").child(uid);
        databaseUser = FirebaseDatabase.getInstance().getReference("UserDetails").child(uid);


        mRecylerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("AllBooks");

        databaseSeller.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SellerInfo si = postSnapshot.getValue(SellerInfo.class);
                    mUploads.add(si);
                }
                mAdapter = new imageAdapter(YourProducts.this, mUploads);
                mRecylerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(YourProducts.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(YourProducts.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onItemClick(int Position) {
        Intent i = new Intent(this,ClickOnYourProducts.class);
        //////////////
        /*Bundle extras = new Bundle();
        extras.putString("id",mUploads.get(Position).getSellerId());
        extras.putInt("position",Position);
        i.putExtras(extras);*/
        ///////////////////

        i.putExtra("id",mUploads.get(Position).getSellerId());
        startActivity(i);
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
            Intent toy = new Intent(YourProducts.this,MainActivity.class);
            startActivity(toy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(YourProducts.this,MainActivity.class);
            startActivity(toy);
            /*Intent i = new Intent(this, ChatActivity.class);
            startActivity(i);
            finish();*/

        } else if (id == R.id.sell_books) {
            Toast.makeText(getApplicationContext(),"Sell Books",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(YourProducts.this,SellBooks.class);
            startActivity(toy);
            // setNavigationViewListener();

        } else if (id == R.id.your_products) {
            Toast.makeText(getApplicationContext(),"Your Products",Toast.LENGTH_LONG).show();


        }
        else if(id== R.id.edit_profile)
        {
            Toast.makeText(getApplicationContext(),"Edit Profile",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(YourProducts.this,EditProfile.class);
            startActivity(toy);
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
            Intent toy = new Intent(YourProducts.this,FirstPage.class);
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
