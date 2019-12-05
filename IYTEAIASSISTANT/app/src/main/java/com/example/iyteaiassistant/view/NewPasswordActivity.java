package com.example.iyteaiassistant.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.iyteaiassistant.R;

import controller.database.DatabaseHelper;
import com.example.iyteaiassistant.helper.Session;
import com.example.iyteaiassistant.helper.Validate;

public class NewPasswordActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    Session session = null;
    private EditText password, confirm_password;
    private Button create;
    private Validate validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        session = new Session(this);
        validate = new Validate();
        databaseHelper = new DatabaseHelper(this);

        password= (EditText) findViewById(R.id.v_password);
        confirm_password = (EditText) findViewById(R.id.v_confirm_password);
        create =(Button) findViewById(R.id.v_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!session.getPasswordVerifyEmail().equals("")
                        && validate.validatePassword(password)
                        && validate.validatePassword(confirm_password)
                        && isEqual(password,confirm_password)){

                    databaseHelper.updatePassword(session.getPasswordVerifyEmail().trim(), password.getText().toString().trim());
                    session.deleteSessionArrtibute("password_verify_email");
                    Intent i = new Intent(NewPasswordActivity.this, LoginActivity.class); // goes to login activity
                    startActivity(i);



                }
            }

        });

    }


    private boolean isEqual(EditText et1, EditText et2){
        String firstET = et1.getText().toString().trim();
        String secondET= et2.getText().toString().trim();
        if (!firstET.equals(secondET)){
            et2.setError("Password mismatch");
            return false;
        }
        return true;
    }


}
