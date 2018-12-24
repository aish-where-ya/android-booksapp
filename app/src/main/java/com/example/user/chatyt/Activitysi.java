package com.example.user.chatyt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Activitysi extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;

    private Button buttonLogIn;
    private TextView textVieweid;
    private TextView textViewpwd;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    ConstraintLayout mybg;
    AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitysi);
        mybg = (ConstraintLayout) findViewById(R.id.mybg);
        animationDrawable= (AnimationDrawable) mybg.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        editTextEmail=(EditText) findViewById(R.id.Email);
        editTextPassword=(EditText) findViewById(R.id.Password);

        buttonLogIn=(Button) findViewById(R.id.Login);
        textVieweid=(TextView) findViewById(R.id.eid);
        textViewpwd=(TextView) findViewById(R.id.pwd);

        buttonLogIn.setOnClickListener(this);
        //editTextEmail.setFocusedByDefault(true);

    }
    @Override
    public void onClick(View view)
    {
        if(view == buttonLogIn)
        {
            userLogin();
        }

    }
    private void userLogin()
    {
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please ENter your Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {

            Toast.makeText(this,"Please ENter your Password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging In");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.hide();
                    Toast.makeText(Activitysi.this,"Logged In",Toast.LENGTH_SHORT).show();
                    Intent toy2 = new Intent(Activitysi.this,MainActivity.class);
                    startActivity(toy2);
                }
                else
                {
                    Toast.makeText(Activitysi.this,"Incorrect Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
