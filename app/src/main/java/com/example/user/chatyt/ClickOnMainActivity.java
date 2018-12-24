package com.example.user.chatyt;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.MoreObjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClickOnMainActivity extends AppCompatActivity {

    private ImageView imageView;
    DatabaseReference database,databaseUser;

    String id,uid;
    String name;
    String email;
    long contactNo;
    Button callSeller;
    TextView Description,Semester,Department,Year,Price1,Contact1,productTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_on_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            uid= user.getUid();
        }
        database = FirebaseDatabase.getInstance().getReference("AllBooks");
        databaseUser = FirebaseDatabase.getInstance().getReference("UserDetails");

        imageView = (ImageView) findViewById(R.id.imageViewUpload);
        productTitle = (TextView) findViewById(R.id.productTitle);
        Description = (TextView) findViewById(R.id.Description);
        Semester = (TextView) findViewById(R.id.Semester);
        Department = (TextView) findViewById(R.id.Department);
        Year = (TextView) findViewById(R.id.Year);
        Contact1 = (TextView) findViewById(R.id.Contact1);
        Price1 = (TextView) findViewById(R.id.Price1);
        callSeller = (Button) findViewById(R.id.callbutton);

        if(getIntent() != null )
        {
            id = getIntent().getStringExtra("id");
            if(!id.isEmpty())
            {

                getDetail(id);

            }
        }

        callSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactNo!= 0)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+contactNo));
                    startActivity(intent);
                }

            }
        });


    }

    private void getDetail(String id) {
        database.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SellerInfo si = dataSnapshot.getValue(SellerInfo.class);

                Picasso.with(getBaseContext()).load(si.getImageUri()).into(imageView);

                productTitle.setText(si.getTitle());
                Description.setText(si.getDescription());
                Semester.setText(si.getSemester());
                Year.setText(si.getYear());
                Department.setText(si.getDepartment());
                Price1.setText(Integer.toString(si.getPrice()));
                email = si.getEmailId();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserInformation userinfo = postSnapshot.getValue(UserInformation.class);

                    String email2 = userinfo.getEmail_id();

                    if(email.equals(email2))
                    {
                        contactNo = userinfo.getContactNo();
                    }

                }


                //Picasso.with(getBaseContext()).load(userinfo.getImageUri()).into(imageView);
                if(contactNo!= 0)
                    Contact1.setText(Long.toString(contactNo));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}