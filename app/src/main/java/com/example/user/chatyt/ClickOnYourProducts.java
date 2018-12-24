package com.example.user.chatyt;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClickOnYourProducts extends AppCompatActivity {

    private ImageView imageView;
    DatabaseReference database,database2,databaseUser;

     String id;
    String uid;
    String name;
    Button markAsSold;
    TextView Description2,Semester2,Department2,Year2,Price2,Contact2,productTitle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_on_your_products);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            uid= user.getUid();
        }
        database = FirebaseDatabase.getInstance().getReference("SellerBookDetails").child(uid);
        database2 = FirebaseDatabase.getInstance().getReference("AllBooks");
        databaseUser = FirebaseDatabase.getInstance().getReference("UserDetails").child(uid);

        imageView = (ImageView) findViewById(R.id.imageViewUpload);
        productTitle2 = (TextView)findViewById(R.id.productTitle2);
        Description2 = (TextView) findViewById(R.id.Description2);
        Semester2 = (TextView) findViewById(R.id.Semester2);
        Department2 = (TextView) findViewById(R.id.Department2);
        Year2 = (TextView) findViewById(R.id.Year2);
        Price2 = (TextView) findViewById(R.id.Price2);
        Contact2 = (TextView) findViewById(R.id.Contact2);
        markAsSold = (Button) findViewById(R.id.markAsSold);


        Intent i= getIntent();
        if(i != null)
        {

            id = getIntent().getStringExtra("id");
            /*Bundle extras  = i.getExtras();
            id = extras.getString("id");
            int pos = extras.getInt("position");*/
            //String id1 = mUploads.get(pos).getSellerId();
            if(!id.isEmpty() )
            {
                //Toast.makeText(getApplicationContext(),""+id,Toast.LENGTH_LONG).show();
                //if(database2.child(id) != null)
                    getDetail(id);


            }
        }

        markAsSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClickOnYourProducts.this);
                builder.setMessage("Are you sure you want to mark this product as sold?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /*Intent toy = new Intent(ClickOnYourProducts.this,YourProducts.class);
                                toy.putExtra("id",id);
                                startActivity(toy);*/
                                dlt(id);
                                Intent toy = new Intent(ClickOnYourProducts.this,YourProducts.class);
                                //toy.putExtra("id",id);
                                finish();
                                startActivity(toy);
                                //database2.child(id).removeValue();
                                //Toast.makeText(getApplicationContext(),"Product Sold",Toast.LENGTH_LONG).show();
                                //database.child(id).removeValue();



                                finish();
                                //Toast.makeText(getApplicationContext(),"Product Sold",Toast.LENGTH_LONG).show();

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

        });



    }



    public void markAsSold()
    {

    }

    private void dlt(String id)
    {

                //Toast.makeText(getApplicationContext(),""+id,Toast.LENGTH_LONG).show();
                database2.child(id).removeValue();
                Toast.makeText(getApplicationContext(),"Product Sold",Toast.LENGTH_LONG).show();
                database.child(id).removeValue();


    }
    private void getDetail(String id) {
        database2.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SellerInfo si = dataSnapshot.getValue(SellerInfo.class);

                if(si!= null)
                {
                    Picasso.with(getBaseContext()).load(si.getImageUri()).into(imageView);

                    productTitle2.setText(si.getTitle());
                    Description2.setText(si.getDescription());
                    Semester2.setText(si.getSemester());
                    Year2.setText(si.getYear());
                    Department2.setText(si.getDepartment());
                    Price2.setText(Integer.toString(si.getPrice()));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation userinfo = dataSnapshot.getValue(UserInformation.class);

                //Picasso.with(getBaseContext()).load(userinfo.getImageUri()).into(imageView);
                Contact2.setText(Long.toString(userinfo.getContactNo()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}