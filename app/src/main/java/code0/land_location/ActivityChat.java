package code0.land_location;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import static code0.land_location.LoginActivity.user;

public class ActivityChat extends AppCompatActivity {
   // private FirebaseListAdapter<ChatMessage> adapter;
    ChatView mChatView;
    String msgfrom ="momanyi", msgto="joseph", chattype="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        if(database==null)
        {
            database.setPersistenceEnabled(true);
            myRef= database.getReference();
        }
        else {
            myRef= database.getReference();
        }

        Intent intent= getIntent();
        msgto=intent.getStringExtra("receiver");
        final String land_id=intent.getStringExtra("land_id");
        chattype= intent.getStringExtra("chat_type");
        getSupportActionBar().setTitle("Land system chat");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ActivityChat.this, String.valueOf(databaseError), Toast.LENGTH_SHORT).show();
            }
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chat = dataSnapshot.getValue(ChatMessage.class);
                //ChatMessage(String current_user,String messageReceiver, String chatType, long time, String message_text, String message_sender)
                String isme="false";

                    String sender_then=chat.getCurrent_user();
                    String chatType=chat.getChatType();
                    String Message_text=chat.getMessage_text();
                    String Message_sender=chat.getMessage_sender();
                    String MessageReceiver=chat.getMessageReceiver();

                    long Time=chat.getTime();

               // Toast.makeText(ActivityChat.this, chatType, Toast.LENGTH_SHORT).show();
                if(chatType.equals(chattype))
                {

                    if(sender_then.equals(user) || MessageReceiver.equals(user))
                    {


                        if(msgto.equals(MessageReceiver))
                        {

                            if(sender_then.equals(user))
                            {
                                //eg user, user or land_seller,land_seller, or surveyor,surveyou
                                isme="true";
                            }



                            if(isme.equals("true"))
                            {
                                Bitmap myIcon = null;
                                User me = new User(1, sender_then, myIcon);
                                Message message = new Message.Builder()
                                        .setUser(me)
                                        .setRightMessage(true)
                                        .setMessageText(Message_text)
                                        .hideIcon(true)
                                        .build();
                                mChatView.send(message);
                            }
                            else if (isme.equals("false"))
                            {

                                Bitmap yourIcon = null;
                                User you = new User(2,MessageReceiver , yourIcon);
                                Message receivedMessage = new Message.Builder()
                                        .setUser(you)
                                        .setRightMessage(false)
                                        .setMessageText(Message_text)
                                        .hideIcon(true)
                                        .build();
                                mChatView.receive(receivedMessage);
                            }


                        }




                    }

                }










//                Toast.makeText(ActivityChat.this, mesasge, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               // Toast.makeText(ActivityChat.this, "child chnaged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
              //  Toast.makeText(ActivityChat.this, "child removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
              //  Toast.makeText(ActivityChat.this, "Child moved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ActivityChat.this, "Database error!", Toast.LENGTH_SHORT).show();

            }
        });


        mChatView = (ChatView)findViewById(R.id.chat_view);

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan900));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sms= mChatView.getInputText();
                mChatView.setInputText("");
                Date d= new Date();
                ChatMessage send_msg=new ChatMessage(user, msgto, chattype,d.getTime(), sms, msgfrom,land_id);
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(send_msg);
            }

        });










    }}

