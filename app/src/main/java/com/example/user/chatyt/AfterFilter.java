package com.example.user.chatyt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AfterFilter extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,imageAdapter.OnItemClickListener{

    private boolean isUserClickBackButton = false;
    private RecyclerView mRecylerView;
    private imageAdapter mAdapter;
    FirebaseUser user;
    String uid;
    private DatabaseReference mDatabaseRef;
    DatabaseReference databaseSeller, databaseUser;

    private List<SellerInfo> mUploads;

    @Override
    public void onItemClick(int Position) {
        Intent i = new Intent(this,ClickOnMainActivity.class);

        i.putExtra("id",mUploads.get(Position).getSellerId());
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent in = getIntent();
        final String option= in.getStringExtra("key1");
        final String choice= in.getStringExtra("key2");



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




        Query query = FirebaseDatabase.getInstance().getReference("AllBooks").orderByChild(""+option).equalTo(""+choice);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    SellerInfo si = postSnapshot.getValue(SellerInfo.class);



                    mUploads.add(si);




                }
                mAdapter = new imageAdapter(AfterFilter.this, mUploads);
                mRecylerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(AfterFilter.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AfterFilter.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //mDatabaseRef.addValueEventListener(valueEventListener);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
        if(!isUserClickBackButton)
        {
            //Toast.makeText(this, "Press back again to sign out",Toast.LENGTH_LONG).show();
            isUserClickBackButton= true;
        }
        else
        {
            super.onBackPressed();
        }
        new CountDownTimer(3000,1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isUserClickBackButton= false;
            }
        }.start();
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
            Intent intent = new Intent(this,FilterActivity.class);
            startActivity(intent);
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

            /*Intent i = new Intent(this, ChatActivity.class);
            startActivity(i);
            finish();*/

        } else if (id == R.id.sell_books) {
            Toast.makeText(getApplicationContext(),"Sell Books",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(AfterFilter.this,SellBooks.class);
            startActivity(toy);
            // setNavigationViewListener();

        } else if (id == R.id.your_products) {
            Toast.makeText(getApplicationContext(),"Your Products",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(AfterFilter.this,YourProducts.class);
            startActivity(toy);

        }
        else if(id== R.id.edit_profile)
        {
            Toast.makeText(getApplicationContext(),"Edit Profile",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(AfterFilter.this,EditProfile.class);
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
                            databaseSeller.removeValue();
                            databaseUser.removeValue();
                            FirebaseAuth.getInstance().signOut();
                            user.delete();
                            Toast.makeText(getApplicationContext(),"User Deleted",Toast.LENGTH_LONG).show();
                            finish();
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
            Intent toy = new Intent(AfterFilter.this,FirstPage.class);
            startActivity(toy);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
