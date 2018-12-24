package com.example.user.chatyt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activitysu extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword, editTextName, editTextNumber;
    private Button buttonRegister;
    private TextView textViewEmail;
    private TextView textViewPassword;
    //private TextView textViewName;
    //private TextView textViewOriName;
    private TextView textViewNumber;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    String id;
    ConstraintLayout mybg;
    AnimationDrawable animationDrawable;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitysu);
        mybg = (ConstraintLayout) findViewById(R.id.mybg);
        animationDrawable= (AnimationDrawable) mybg.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.Register);
        editTextEmail = (EditText) findViewById(R.id.Email);
        editTextPassword = (EditText) findViewById(R.id.Password);
        editTextName =(EditText) findViewById(R.id.Name);
        editTextNumber=(EditText) findViewById(R.id.Contact_no);
        //textViewEmail = (TextView) findViewById(R.id.email);
        //textViewPassword = (TextView) findViewById(R.id.password);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");

        buttonRegister.setOnClickListener(this);

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email=editTextEmail.getText().toString().trim();
                //String password=editTextPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    editTextEmail.setError("Email id empty");
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please enter a valid email address");
                    return;
                }
            }
        });


    }



    @Override
    public void onClick(View view)
    {

        String email=editTextEmail.getText().toString().trim();
        registeruser(email);

    }
    private void getinfo()
    {

    }
    private void registeruser(String email)
    {
        //String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        String contacts = editTextNumber.getText().toString().trim();

        if(TextUtils.isEmpty(password))
        {

            Toast.makeText(this,"Please Enter your Password",Toast.LENGTH_SHORT).show();
            return;
        }



        progressDialog.setMessage("Registering..");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    getinfo();
                    progressDialog.hide();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null)
                    {
                        id = user.getUid();
                        saveUserInformation();
                    }

                    Toast.makeText(Activitysu.this,"Registered",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),Activitysi.class);
                    startActivity(i);
                }
                else
                {
                    progressDialog.hide();
                    Toast.makeText(Activitysu.this,"Incorrect Details",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserInformation()
    {
        String name = editTextName.getText().toString().trim();
        long contact_no= Long.parseLong(editTextNumber.getText().toString().trim());
        String email_id = editTextEmail.getText().toString().trim();
        //final String id = databaseReference.push().getKey();
        databaseReference.push();
        //final String id = firebaseAuth.getCurrentUser().getUid();
        UserInformation userInformation = new UserInformation(name,contact_no,email_id);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(id).setValue(userInformation);
       // Toast.makeText(this, "Sign Up successful", Toast.LENGTH_LONG).show();
    }
}
