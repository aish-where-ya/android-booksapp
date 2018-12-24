package com.example.user.chatyt;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class SellBooks extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Spinner spinnerDept;
    Spinner spinnerYr;
    Spinner spinnerSem;
    Button Submit;
    EditText description, Price, Title;
    DatabaseReference databaseAllBooks, databaseUser,databaseSeller, mDatabaseRef;
    Button choose;
    ImageView img;
    ProgressBar progressBar;
    Uri imageUri;
    String emailId, uid,contact;
    StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_books);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            uid= user.getUid();
        }
        databaseAllBooks = FirebaseDatabase.getInstance().getReference("AllBooks");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("AllBooks");

        databaseSeller = FirebaseDatabase.getInstance().getReference("SellerBookDetails").child(uid);
        databaseUser = FirebaseDatabase.getInstance().getReference("UserDetails").child(uid);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        spinnerDept=(Spinner) findViewById(R.id.dept);
        spinnerYr=(Spinner) findViewById(R.id.yr);
        spinnerSem=(Spinner) findViewById(R.id.smstr);
        Submit= (Button) findViewById(R.id.Submit);
        choose= (Button) findViewById(R.id.choose);
        description = (EditText) findViewById(R.id.descr);
        Title = (EditText)findViewById(R.id.editTextTitle);
        Price = (EditText) findViewById(R.id.price);

        img= (ImageView) findViewById(R.id.imageView);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetails();
            }
        });


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
        } else {
            // super.onBackPressed();
            Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(SellBooks.this,MainActivity.class);
            startActivity(toy);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(SellBooks.this,MainActivity.class);
            startActivity(toy);

        } else if (id == R.id.sell_books) {
            Toast.makeText(getApplicationContext(),"Sell Books",Toast.LENGTH_LONG).show();


        } else if (id == R.id.your_products) {
            Toast.makeText(getApplicationContext(),"Your Products",Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Your Products",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(SellBooks.this,YourProducts.class);
            startActivity(toy);


        }
        else if(id== R.id.edit_profile)
        {
            Toast.makeText(getApplicationContext(),"Edit Profile",Toast.LENGTH_LONG).show();
            finish();
            Intent toy = new Intent(SellBooks.this,EditProfile.class);
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
            Intent toy = new Intent(SellBooks.this,FirstPage.class);
            startActivity(toy);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addDetails()
    {
        final String department= spinnerDept.getSelectedItem().toString();
        final String year= spinnerYr.getSelectedItem().toString();
        final String semester= spinnerSem.getSelectedItem().toString();
        final String descr = description.getText().toString().trim();
        final String id = databaseAllBooks.push().getKey();
        final String id2 = id;//databaseSeller.push().getKey();
        final String pric = Price.getText().toString().trim();
        final String title = Title.getText().toString().trim();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            emailId = user.getEmail();
            //contact = user.getContact();
        }

        if(imageUri != null && title!=null && descr!=null && pric!=null)
        {
            final int price = Integer.parseInt(pric);

            String details = System.currentTimeMillis() + '.' + getFileExtension(imageUri);
            final StorageReference fileReference = mStorageRef.child(id+"/"+System.currentTimeMillis() + '.' + getFileExtension(imageUri));
            final StorageReference fileReference2 = mStorageRef.child(id2+"/"+System.currentTimeMillis() + '.' + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            SellerInfo si = new SellerInfo(title,id,department,year,semester,uri.toString(),emailId,descr,price);

                            databaseAllBooks.child(id).setValue(si);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                    Toast.makeText(SellBooks.this,"Upload Successful",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SellBooks.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });



            fileReference2.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);

                    fileReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            SellerInfo si = new SellerInfo(title,id,department,year,semester,uri.toString(),emailId,descr,price);

                            databaseSeller.child(id2).setValue(si);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                    Toast.makeText(SellBooks.this,"Upload Successful",Toast.LENGTH_SHORT).show();




                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SellBooks.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });

        }
        else
        {
            Toast.makeText(this,"Please fill complete details",Toast.LENGTH_SHORT).show();
        }





    }

    private void openFileChooser()
    {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() != null)
        {
            imageUri = data.getData();

            Picasso.with(this).load(imageUri).into(img);

        }
    }

    private String getFileExtension(Uri imageUri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(imageUri));
    }

    public void deleteUser()
    {
        databaseSeller.removeValue();
        final String email = user.getEmail();
        String id;
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
