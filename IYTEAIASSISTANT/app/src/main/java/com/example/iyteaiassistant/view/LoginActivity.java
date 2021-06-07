package com.example.karabukaiassistant.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karabukaiassistant.R;

import controller.database.DatabaseHelper;
import com.example.karabukaiassistant.helper.Session;
import com.example.karabukaiassistant.helper.Validate;

public class LoginActivity extends AppCompatActivity{

    private EditText emailLogin, password;
    TextView forgotPassword;
    private Button signIn;
    private DatabaseHelper databaseHelper = null;
    private Session session;
    private Validate validate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session =new Session(this); // session start
        databaseHelper = new DatabaseHelper(this);
        validate = new Validate(); // validate class

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
                if (validate.validatePassword(password) && validate.validateEmail(emailLogin)
                        && isPasswordMatch(emailLogin,password) && isEmailExist(emailLogin)){
                    Log.d("enter:" , "im here");

                    session.setUserEmail(emailLogin.getText().toString().trim()); // set user session variable

                    Intent i = new Intent(LoginActivity.this, ChatActivity.class); // goes to chat activity
                    startActivity(i);
                }else{
                    Log.d("enter:" , "im else");
                }

            }
        });

    }

    private boolean isPasswordMatch(EditText email, EditText password){
            if(!databaseHelper.getUserPassword(email.getText().toString().trim()).equals(password.getText().toString().trim()))
        { // if password doesn't match
            password.setError("Password mismatch !");
            return false;
        }
        return true;
    }
    private boolean isEmailExist(EditText email) {
        if (!databaseHelper.isEmailExist(email.getText().toString().trim())) { //check email exist in controller.database or not
            email.setError("Email not exist !");
            return false;
        }
        return true;
    }
}
