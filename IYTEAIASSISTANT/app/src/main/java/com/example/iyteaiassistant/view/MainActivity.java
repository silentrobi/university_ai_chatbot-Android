package com.example.karabukaiassistant.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.karabukaiassistant.R;

import controller.database.DatabaseHelper;
import com.example.karabukaiassistant.helper.Session;

public class MainActivity extends AppCompatActivity {
    private Button login, register;
    private DatabaseHelper databaseHelper = null; //controller.database
    private Session session; // session
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this); // session start

        login = (Button)findViewById(R.id.m_login);
        register= (Button)findViewById(R.id.m_register);

        Log.d("user_session: ", session.getUserEmail());
        //test
        session.sessionDestroy();
        if (session.getUserEmail().equals("")) { // if user not lagged in yet
            Log.d("session_user:" , "empty");
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(i);
                }
            });
        }else{ // if user already logged in
            Log.d("session_user:" , session.getUserEmail());
            Intent i = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(i);
        }


    }
}
