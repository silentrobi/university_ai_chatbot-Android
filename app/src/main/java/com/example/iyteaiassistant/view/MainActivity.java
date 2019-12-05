package com.example.iyteaiassistant.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.iyteaiassistant.R;
import controller.helper.Session;

/* first activity to run when user launch the application */

public class MainActivity extends AppCompatActivity {
    private Button login, register; // buttons
    private Session session; // session
    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate() method is used to Create the activity
                                                        // for the first time and initialize the variables.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // set the view (activity_main xml file)

        session = new Session("USER",this); // initialize session object

        login = (Button)findViewById(R.id.m_login);
        register= (Button)findViewById(R.id.m_register);

        Log.d("user_session: ", session.getUserEmail());
        //test
        if (session.getUserEmail().equals("")) { // if user not lagged in yet
            Log.d("session_user:" , "empty");
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // clicked on login go to LoginActivity page
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // clicked on register go to RegistrationActivity page
                    Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(i);
                }
            });
        }else{ // if user already logged in then go to ChatActivity page
            Log.d("session_user:" , session.getUserEmail());
            Intent i = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(i);
            finish(); // once user move to ChatActivity page user can't comeback to MainActivity page
                        // finish() means activity is finished.
        }


    }

}
