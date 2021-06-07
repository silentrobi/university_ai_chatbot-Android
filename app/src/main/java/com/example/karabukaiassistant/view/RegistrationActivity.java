package com.example.karabukaiassistant.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.karabukaiassistant.R;
import controller.database.DatabaseHelper;
import controller.helper.Validate;
import model.User;


public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, email, password, confirmPassword;
    private Button register;

    private DatabaseHelper databaseHelper = null;
    private Validate validate;

    private User user= null;
    private static final int NULL = -1 ;// for user id: As user id auto increamented we dont need to initialize user id

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews(); // initialize view

        databaseHelper= new DatabaseHelper(this);
        validate = new Validate();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate.validateUserName(userName)
                        || !validate.validateEmail(email)
                        || !validate.validatePassword(password)
                        || !validate.validatePassword(confirmPassword)
                        || !isPasswordEqual(password,confirmPassword))

                {
                    Log.d("ME ", "Im here");
                }else if (!databaseHelper.isEmailExist(email.getText().toString().toLowerCase().trim())) { // if  email not exist
                    String userNameS = userName.getText().toString().trim().toLowerCase(); // lowercase username: not case sensitive
                    String passwordS = password.getText().toString().trim();

                    databaseHelper.addUser(new User(NULL, userNameS, email.getText().toString().toLowerCase().trim(), passwordS)); // add user to SQLite database
                    Log.d("user: ", "new user is added !");

                    Intent i = new Intent(RegistrationActivity.this, MainActivity.class); // go to MainActivity page
                    startActivity(i);

                    for (User cn : databaseHelper.getAllUser()) {
                        String log = "Id: "+cn.getUserId()+" ,Name: " + cn.getUserName() + " ,email: " + cn.getEmail()+  " ,password: " + cn.getPassword();
                        // Writing Contacts to log
                        Log.d("Name: ", log);
                    }
                    finish();  //once finished registering user can't come back to this RegisterActivity page

                }
                else{ // if email exist
                    email.setError("email already exist!");

                }

            }
        });



    }


    private void initViews() {

        userName = (EditText) findViewById(R.id.user_name);
        email = (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        confirmPassword= (EditText) findViewById(R.id.confirm_password);
        register = (Button) findViewById(R.id.register);



    }

    private boolean isPasswordEqual(EditText et1, EditText et2){

        String firstET = et1.getText().toString().trim();
        String secondET= et2.getText().toString().trim();
        if (!firstET.equals(secondET)){
            et2.setError("Password mismatch");
            return false;
        }
        return true;
    }



}
