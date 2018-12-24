package com.example.user.chatyt;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.hoang8f.widget.FButton;

public class FirstPage extends AppCompatActivity {
    private FButton SignUp;
    private FButton SignIn;
    ConstraintLayout mybg;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        mybg = (ConstraintLayout) findViewById(R.id.mybg);
        animationDrawable= (AnimationDrawable) mybg.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        SignUp=(FButton) findViewById(R.id.SignUp);
        SignIn=(FButton) findViewById(R.id.SignIn);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toy = new Intent(FirstPage.this,Activitysu.class);
                startActivity(toy);
            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toy1 = new Intent(FirstPage.this,Activitysi.class);
                startActivity(toy1);
            }
        });



    }
}

