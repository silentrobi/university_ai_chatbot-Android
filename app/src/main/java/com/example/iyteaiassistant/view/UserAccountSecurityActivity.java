package com.example.iyteaiassistant.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.iyteaiassistant.R;
import controller.database.DatabaseHelper;
import controller.helper.Session;
import controller.helper.Validate;

/* aim is to check if user exist in the system: middle phase of "forgot password" */
public class UserAccountSecurityActivity extends AppCompatActivity  {

    private EditText email;
    private Button next;
    private DatabaseHelper databaseHelper;
    Session session;
    Validate validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_security);

        email = (EditText)  findViewById(R.id.s_email);
        next = (Button) findViewById(R.id.s_next);

        session = new Session("USER", this);
        validate = new Validate();
        databaseHelper= new DatabaseHelper(this);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate.validateEmail(email) && isEmailExist(email)) {
                    session.setPasswordVerifyEmail(email.getText().toString().trim().toLowerCase()); //set session variable
                    Intent i = new Intent(UserAccountSecurityActivity.this, NewPasswordActivity.class);
                    startActivity(i);


                }
            }
        });
    }


    private boolean isEmailExist(EditText email) {

        if (!databaseHelper.isEmailExist(email.getText().toString().trim().toLowerCase())) { //check email exist in controller.database or not
            email.setError("Email not exist !");
            return false;
        }
        return true;
    }

}
