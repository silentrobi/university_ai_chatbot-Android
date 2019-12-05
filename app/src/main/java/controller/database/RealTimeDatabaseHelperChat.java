package controller.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Message;
import model.User;

public class RealTimeDatabaseHelperChat implements Encoder{
    private String userEmail;

    private DatabaseReference ref;
    public RealTimeDatabaseHelperChat(){

        ref = FirebaseDatabase.getInstance().getReference("Chat");

    }
    public void setUserEmail(String email){ // user will be identified by email id
        userEmail= email;
    }
    public String getUserEmail(){
        return userEmail;

    }

    public void addMessage(String userEmail, Message message){
        String encodedEmail= encodeEmail(userEmail);
        DatabaseReference usersRef = ref.child("Users").child(encodedEmail); // path in tree: user email
        String key =usersRef.push().getKey(); // generate unique key/id for message
        usersRef.child(key).setValue(message);
    }
    public ArrayList<Message> userChatHistory(String userEmail){
        final ArrayList<Message> messageHistory= new ArrayList<Message>();
        String encodedEmail= encodeEmail(userEmail);
        DatabaseReference userRef= ref.child("Users").child(encodedEmail); //
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageHistory.clear(); // we clear the list when data changes and again add the data in it.
                for(DataSnapshot messageId: dataSnapshot.getChildren()){
                    Message message = messageId.getValue(Message.class);
                    messageHistory.add(message);
                    // check null point error if occurs
                    Log.d("text - type", message.getText()+ " "+ message.getCategory());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return messageHistory;

    }
    public  String encodeEmail(String email) {
        return email.replace(".", ","); // as firebase shows error in storing email using . so replacing it with ","
    }




}
