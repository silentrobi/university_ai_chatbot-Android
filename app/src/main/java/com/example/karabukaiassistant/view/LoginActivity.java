package com.example.karabukaiassistant.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.karabukaiassistant.R;
import controller.database.DatabaseHelper;
import controller.helper.Session;
import controller.helper.Validate;

public class LoginActivity extends AppCompatActivity{

    private EditText emailLogin, password;
    private TextView forgotPassword;
    private Button signIn;
    private DatabaseHelper databaseHelper;
    private Session session;
    private Validate validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session =new Session("USER", this); // session create
        databaseHelper = new DatabaseHelper(this);
        validate = new Validate(); // to validate input fields

        emailLogin= (EditText) findViewById(R.id.l_email);
        password= (EditText) findViewById(R.id.l_password);
        forgotPassword= (TextView) findViewById(R.id.forgot_password);
        signIn= (Button) findViewById(R.id.sign_in);



        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, UserAccountSecurityActivity.class);
                startActivity(i);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( validate.validateEmail(emailLogin)
                        && validate.validatePassword(password)
                        && isEmailExist(emailLogin)
                        && isPasswordMatch(emailLogin,password) ){


                    session.setUserEmail(emailLogin.getText().toString().trim().toLowerCase()); // set user session variable

                    Intent i = new Intent(LoginActivity.this, ChatActivity.class); // goes to chat activity
                    startActivity(i);
                    finish(); // means when user will press back from chat activity system won't show this activity
                }

            }
        });

    }

    private boolean isPasswordMatch(EditText email, EditText password){

        String email_ = email.getText().toString().trim().toLowerCase();
        String password_= password.getText().toString().trim();
        String storedPassword = databaseHelper.getUserPassword(email_);
        if(  storedPassword.equals(password_)) // password exist and if match
        {
            return true;
        }
        password.setError("Password doesn't match!"); // if password doesn't match
        return false;
    }
    private boolean isEmailExist(EditText email) {

        if (databaseHelper.isEmailExist(email.getText().toString().trim().toLowerCase())) { // if email exist
            return true;
        }
        email.setError("Email does not exist!");
        return false;
    }
}
