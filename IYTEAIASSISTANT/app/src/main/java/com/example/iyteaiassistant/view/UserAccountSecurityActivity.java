package com.example.karabukaiassistant.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.karabukaiassistant.R;

import controller.database.DatabaseHelper;
import com.example.karabukaiassistant.helper.Session;
import com.example.karabukaiassistant.helper.Validate;

public class UserAccountSecurityActivity extends AppCompatActivity  {
    private EditText email;
    private DatabaseHelper databaseHelper = null;
    Session session = null;
    Validate validate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_security);
        email = (EditText)  findViewById(R.id.s_email);
        Button next = (Button) findViewById(R.id.s_next);
        session = new Session(this);
        validate = new Validate();
        databaseHelper= new DatabaseHelper(this);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate.validateEmail(email) && isEmailExist(email)) {
                    session.setPasswordVerifyEmail(email.getText().toString().trim()); //set session variable
                    Intent i = new Intent(UserAccountSecurityActivity.this, NewPasswordActivity.class);
                    startActivity(i);

                }
            }
        });
    }


    private boolean isEmailExist(EditText email) {
        if (!databaseHelper.isEmailExist(email.getText().toString().trim())) { //check email exist in controller.database or not
            email.setError("Email not exist !");
            return false;
        }
        return true;
    }

}
