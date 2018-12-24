package com.example.user.chatyt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ChatActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage>adapter;
    ConstraintLayout chat_activity;
    FloatingActionButton fab2;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if(item.getItemId()== R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(chat_activity,"You have been signed out",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }*/
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main_setting,menu);
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== SIGN_IN_REQUEST_CODE)
        {
            if(requestCode== RESULT_OK)
            {
                Snackbar.make(chat_activity,"Successfully signed in. Welcome!",Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else
            {
                Snackbar.make(chat_activity,"We couldn't sign in. Please try again later.",Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chat_activity= (ConstraintLayout) findViewById(R.id.chat_activity);
        fab2 =  (FloatingActionButton)findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference("Chats").push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getApplicationContext(),"User not signed in",Toast.LENGTH_LONG).show();
            //startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        } else {
            Snackbar.make(chat_activity, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
            displayChatMessage();
        }


}


private void displayChatMessage()
{
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Chats");
    ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
    //adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.chat_list_item, mref)
    Query query = FirebaseDatabase.getInstance().getReference("Chats");
//The error said the constructor expected FirebaseListOptions - here you create them:
    FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
            .setQuery(query, ChatMessage.class)
            .setLayout(R.layout.chat_list_item)
            .build();
    //Finally you pass them to the constructor here:
    adapter = new FirebaseListAdapter<ChatMessage>(options) {
        @Override
        protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
            TextView messageText, messageUser,messageTime ;
            messageText= (TextView) v.findViewById(R.id.message_text);
            messageUser= (TextView) v.findViewById(R.id.message_user);
            messageTime= (TextView) v.findViewById(R.id.message_time);

            messageText.setText(model.getMessageText());
            messageUser.setText(model.getMessageUser());
            messageTime.setText(DateFormat.format("dd-mm-yyyy (hh:mm:ss)",model.getMessageTime()));
        }
    };
    listOfMessage.setAdapter(adapter);
}

}